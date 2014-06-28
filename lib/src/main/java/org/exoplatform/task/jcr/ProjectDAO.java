package org.exoplatform.task.jcr;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Value;
import javax.jcr.query.Query;
import javax.jcr.query.QueryResult;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.services.organization.Membership;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.security.MembershipEntry;
import org.exoplatform.task.JCRTaskService;
import org.exoplatform.task.TaskServiceException;
import org.exoplatform.task.Utils;
import org.exoplatform.task.model.Project;

public class ProjectDAO {
    private static final Log log = ExoLogger.getLogger(ProjectDAO.class);

    private JCRTaskService taskService;
    private OrganizationService orgService;

    public ProjectDAO(JCRTaskService taskService, OrganizationService orgService) {
        this.taskService = taskService;
        this.orgService = orgService;
    }

    public Project getProject(String id) {
        if (id == null) {
            return null;
        }

        try {
            Node node = getProjectNode(id);
            return buildProject(node);
        } catch (RepositoryException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public List<Project> getProjectsByUser(String username) {
        if (username == null) {
            return Collections.emptyList();
        }

        List<Project> projects = new LinkedList<Project>();

        // private projects
        Node userHome = taskService.getOrCreateUserHome(username);
        try {
            NodeIterator userProjects = userHome.getNodes();
            while (userProjects.hasNext()) {
                projects.add(buildProject(userProjects.nextNode()));
            }

            // shared projects
            Collection<?> memberships = orgService.getMembershipHandler().findMembershipsByUser(username);
            for (Object membership : memberships) {
                Membership m = (Membership) membership;
                Node sharedHome = taskService.getOrCreateSharedHome(new MembershipEntry(m.getGroupId(), m.getMembershipType())
                        .toString());
                PropertyIterator iter = sharedHome.getReferences();
                while (iter.hasNext()) {
                    projects.add(buildProject(iter.nextProperty().getParent()));
                }
            }

            return projects;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public void createProject(Project p) throws TaskServiceException {
        if (p.getName() == null || p.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Project name can NOT be empty");
        }

        try {
            if (orgService.getUserHandler().findUserByName(p.getOwner()) == null) {
                throw new TaskServiceException(TaskServiceException.NON_EXITS_OWNER, "Non exists owner: " + p.getOwner());
            }
        } catch (Exception e) {
            throw new TaskServiceException(e.getMessage(), e);
        }

        try {
            Node userHome = taskService.getOrCreateUserHome(p.getOwner());
            Node projectNode = userHome.addNode(p.getId(), "exo:project");
            setProperties(projectNode, p);
            userHome.save();

            // TODO: share project
        } catch (RepositoryException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public void updateProject(Project p) throws TaskServiceException {
        Node userHome = taskService.getOrCreateUserHome(p.getOwner());

        try {
            if (userHome.hasNode(p.getId())) {
                throw new TaskServiceException(TaskServiceException.NON_EXITS_PROJECT, "Can't update. Non exists: " + p.getId());
            }
            
            Node project = userHome.getNode(p.getId());
            //Don't allow to change the owner
            if (p.getOwner().equals(project.getProperty("exo:owner"))) {
                setProperties(project, p);
                project.save();
                
                //TODO share project
            }
        } catch (RepositoryException e) {
            log.error(e);
            throw new RuntimeException(e);
        }
    }

    public void removeProject(String id) {
        try {
            Node project = getProjectNode(id);
            if (project != null) {
                Session session = project.getSession();
                project.remove();
                session.save();
            }
        } catch (RepositoryException e) {
            log.error(e);
            throw new RuntimeException(e);
        }
        
    }

    public Node getProjectNode(String id) throws RepositoryException {
        StringBuilder sql = new StringBuilder("select * from exo:project where jcr:path like '");
        sql.append(JCRTaskService.USERS_PATH).append("/%/").append(JCRTaskService.APP_PATH);
        sql.append("/").append(Utils.queryEscape(id)).append("'");

        Session session = taskService.getSession();
        Query query = session.getWorkspace().getQueryManager().createQuery(sql.toString(), Query.SQL);

        QueryResult result = query.execute();
        NodeIterator projects = result.getNodes();
        if (projects.hasNext()) {
            return projects.nextNode();
        } else {
            return null;        
        }
    }

    private void setProperties(Node projectNode, Project p) throws RepositoryException {
        projectNode.setProperty("exo:id", p.getId());
        projectNode.setProperty("exo:name", p.getName());
        projectNode.setProperty("exo:owner", p.getOwner());
        projectNode.setProperty("exo:description", p.getDesc());
        Set<String> memberships = p.getMemberships();
        projectNode.setProperty("exo:memberships", memberships.toArray(new String[memberships.size()]));
        projectNode.setProperty("exo:dateCreated", p.getDateCreated());
    }

    private Project buildProject(Node node) throws RepositoryException {
        Project project = new Project();
        PropertyIterator iter = node.getProperties("exo:*");
        while (iter.hasNext()) {
            Property p = iter.nextProperty();
            String name = p.getName();

            if (name.equals("exo:id")) {
                project.setId(p.getString());
            } else if (name.equals("exo:name")) {
                project.setName(p.getString());
            } else if (name.equals("exo:owner")) {
                project.setOwner(p.getString());
            } else if (name.equals("exo:description")) {
                project.setDesc(p.getString());
            } else if (name.equals("exo:memberships")) {
                Set<String> memberships = new HashSet<String>();
                for (Value membership : p.getValues()) {
                    memberships.add(membership.getString());
                }
                project.setMemberships(memberships);
            } else if (name.equals("exo:dateCreated")) {
                project.setDateCreated(p.getLong());
            }
        }
        return project;
    }
}
