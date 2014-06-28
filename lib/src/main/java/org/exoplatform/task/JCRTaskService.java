package org.exoplatform.task;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import java.util.List;

import org.exoplatform.container.xml.InitParams;
import org.exoplatform.container.xml.ValueParam;
import org.exoplatform.services.jcr.RepositoryService;
import org.exoplatform.services.jcr.core.ManageableRepository;
import org.exoplatform.services.jcr.ext.app.SessionProviderService;
import org.exoplatform.services.jcr.ext.common.SessionProvider;
import org.exoplatform.services.jcr.ext.distribution.DataDistributionManager;
import org.exoplatform.services.jcr.ext.distribution.DataDistributionMode;
import org.exoplatform.services.jcr.ext.distribution.DataDistributionType;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.task.jcr.CommentDAO;
import org.exoplatform.task.jcr.ProjectDAO;
import org.exoplatform.task.jcr.TaskDAO;
import org.exoplatform.task.model.Comment;
import org.exoplatform.task.model.Project;
import org.exoplatform.task.model.Query;
import org.exoplatform.task.model.Task;

public class JCRTaskService implements TaskService {
    public static final String USERS_PATH = "/Users";
    
    public static final String APP_PATH = "ApplicationData/taskApplication";

    public static final String SHARED_PATH = "/exo:applications/taskApplication/sharedProjects/groups";
    
    public String workspace = "team_e";

    private ProjectDAO projectDAO;

    private TaskDAO taskDAO;
    
    private CommentDAO commentDAO;

    private SessionProviderService sessionService;

    private DataDistributionManager dataDistributionManager;
    
    private RepositoryService repositoryService;

    private static final Log log = ExoLogger.getLogger(JCRTaskService.class);

    public JCRTaskService(InitParams params, OrganizationService orgService, SessionProviderService sessionService, RepositoryService repositoryService,
            DataDistributionManager dataDistributionManager) {
        if (params != null) {
            ValueParam param = params.getValueParam("workspace");
            if (param != null) {
                workspace = param.getValue();
            }
        }
        
        this.projectDAO = new ProjectDAO(this, orgService);
        this.taskDAO = new TaskDAO(this);
        this.commentDAO = new CommentDAO(this);
        this.sessionService = sessionService;
        this.dataDistributionManager = dataDistributionManager;
        this.repositoryService = repositoryService;
    }

    public ProjectDAO getProjectDAO() {
        return projectDAO;
    }

    public TaskDAO getTaskDAO() {
        return taskDAO;
    }
    
    public CommentDAO getCommentDAO() {
        return commentDAO;
    }

    @Override
    public Project getProject(String id) {
        return projectDAO.getProject(id);
    }

    @Override
    public List<Project> getProjectsByUser(String username) {
        return projectDAO.getProjectsByUser(username);
    }

    @Override
    public Project createProject(Project p) throws TaskServiceException {
        return projectDAO.createProject(p);
    }

    @Override
    public void updateProject(Project p) throws TaskServiceException {
        projectDAO.updateProject(p);
    }

    @Override
    public void removeProject(String id) {
        projectDAO.removeProject(id);
    }

    public Task addTask(Task t) throws TaskServiceException {
        return taskDAO.addTask(t);
    }

    @Override
    public Task removeTask(String id) {
        return taskDAO.removeTask(id);
    }

    @Override
    public Task updateTask(Task t) throws TaskServiceException {
        return taskDAO.updateTask(t);
    }

    @Override
    public Task getTask(String id) {
        return taskDAO.getTask(id);
    }

    @Override
    public List<Task> getTasksByProject(String projectId, int offset, int limit) {
        return taskDAO.getTaskByProject(projectId, offset, limit);
    }

    @Override
    public List<Task> findTasks(Query query, int offset, int limit) {
        return taskDAO.findTasks(query, offset, limit);
    }

    public Node getOrCreateUserHome(String username) {
        String path = String.format("%s/%s/%s", USERS_PATH, username, APP_PATH);
        return getOrCreateNode(path);
    }

    public Node getOrCreateSharedHome(String groupId) {
        String path = String.format("%s%s", SHARED_PATH, groupId);

        Node node = getOrCreateNode(path);
        try {
            node.addMixin("mix:referenceable");
            return node;
        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        }
    }
    
    public Node getOrCreateNode(String path) {
        try {
            Session session = getSession();
            
            DataDistributionType type = dataDistributionManager.getDataDistributionType(DataDistributionMode.NONE);
            return type.getOrCreateDataNode(session.getRootNode(), path);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public Session getSession() throws RepositoryException {
        ManageableRepository repo = repositoryService.getCurrentRepository();
        SessionProvider sessionProvider = sessionService.getSystemSessionProvider(null); 
        return sessionProvider.getSession(workspace, repo);
    }

    @Override
    public Comment getCommentById(String id) {
        return commentDAO.getCommentById(id);
    }

    @Override
    public List<Comment> getCommentByTask(String taskId, int offset, int limit) {
        return commentDAO.getCommentByTask(taskId, offset, limit);
    }

    @Override
    public Comment addComment(Comment c) throws TaskServiceException {
        return commentDAO.addComment(c);
    }

    @Override
    public Comment removeComment(String commentId) {
        return commentDAO.removeComment(commentId);
    }

    @Override
    public Comment updateComment(Comment c) {
        return commentDAO.updateComment(c);
    }
}
