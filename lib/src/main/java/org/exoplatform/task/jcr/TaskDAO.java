package org.exoplatform.task.jcr;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Value;

import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.exoplatform.services.jcr.impl.core.query.QueryImpl;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.task.JCRTaskService;
import org.exoplatform.task.TaskServiceException;
import org.exoplatform.task.Utils;
import org.exoplatform.task.model.Priority;
import org.exoplatform.task.model.Query;
import org.exoplatform.task.model.Task;

public class TaskDAO {
    private static final Log log = ExoLogger.getLogger(TaskDAO.class);

    private JCRTaskService taskService;

    public TaskDAO(JCRTaskService taskService) {
        this.taskService = taskService;
    }

    public void addTask(Task t) throws TaskServiceException {
        try {
            Node projectNode = taskService.getProjectDAO().getProjectNode(t.getProjectId());

            if (projectNode == null) {
                throw new TaskServiceException(TaskServiceException.NON_EXITS_PROJECT, "Can't create task. Project "
                        + t.getProjectId() + " not found");
            }

            t.setCreatedDate(System.currentTimeMillis());
            t.setId(UUID.randomUUID().toString());
            Node taskNode = projectNode.addNode(t.getId(), "exo:task");
            setTaskProperties(taskNode, t);

            projectNode.save();
        } catch (RepositoryException e) {
            log.error(e);
            throw new RuntimeException(e);
        }
    }

    public Task removeTask(String id) {
        Task deletedTask = null;
        try {
            Node task = getTaskNode(id);
            if (task != null) {
                deletedTask = buildTask(task);

                Session session = task.getSession();
                task.remove();
                session.save();
            }
            return deletedTask;
        } catch (RepositoryException e) {
            log.error(e);
            throw new RuntimeException(e);
        }
    }

    public void updateTask(Task t) throws TaskServiceException {
        try {
            Node taskNode = getTaskNode(t.getId());
            if (taskNode == null) {
                throw new TaskServiceException(TaskServiceException.NON_EXITS_TASK, "Can't update non-exists task: " + t.getId());
            }
            Node projectNode = taskService.getProjectDAO().getProjectNode(t.getProjectId());
            if (projectNode == null) {
                throw new TaskServiceException(TaskServiceException.NON_EXITS_PROJECT, "Can't update task to non-exists project: "
                        + t.getProjectId());
            }
            
            t.setModifedDate(System.currentTimeMillis());
            setTaskProperties(taskNode, t);
            taskNode.getSession().save();
        } catch (RepositoryException e) {
            log.error(e);
            throw new RuntimeException(e);
        }        
    }

    public Task getTask(String id) {
        if (id == null) {
            return null;
        }

        try {
            Node node = getTaskNode(id);
            return buildTask(node);
        } catch (RepositoryException e) {
            log.error(e);
            throw new RuntimeException(e);
        }
    }

    public List<Task> getTaskByProject(String projectId, int offset, int limit) {
        List<Task> tasks = new LinkedList<Task>();

        if (projectId != null) {
            try {
                Node project = taskService.getProjectDAO().getProjectNode(projectId);
                if (project != null) {
                    NodeIterator nodeIter = project.getNodes();

                    while (nodeIter.hasNext()) {
                        tasks.add(buildTask(nodeIter.nextNode()));
                    }
                }
            } catch (RepositoryException e) {
                log.error(e);
                throw new RuntimeException(e);
            }
        }

        return tasks;
    }

    public List<Task> findTasks(Query query, int offset, int limit) {
        List<Task> tasks = new LinkedList<Task>();

        try {
            List<Node> nodes = getTaskNode(query, offset, limit);
            for (Node node : nodes) {
                tasks.add(buildTask(node));
            }
            return tasks;
        } catch (RepositoryException e) {
            log.error(e);
            throw new RuntimeException(e);
        }
    }

    private void setTaskProperties(Node taskNode, Task t) throws RepositoryException {
        taskNode.setProperty("exo:id", t.getId());
        taskNode.setProperty("exo:projectId", t.getProjectId());
        taskNode.setProperty("exo:reporter", t.getReporter());
        taskNode.setProperty("exo:assignee", t.getAssignee());
        taskNode.setProperty("exo:title", t.getTitle());
        taskNode.setProperty("exo:status", t.getStatus());
        taskNode.setProperty("exo:priority", t.getPriority().priority());
        Set<String> labels = t.getLabels();
        taskNode.setProperty("exo:labels", labels.toArray(new String[labels.size()]));
        Date createdDate = t.getCreatedDate();
        if (createdDate != null) {
            taskNode.setProperty("exo:createdDate", createdDate.getTime());
        }
        Date modifedDate = t.getCreatedDate();
        if (modifedDate != null) {
            taskNode.setProperty("exo:modifiedDate", modifedDate.getTime());
        }
    }

    private Task buildTask(Node node) throws RepositoryException {
        if (node == null) {
            return null;
        }

        Task task = new Task();

        PropertyIterator iter = node.getProperties("exo:*");
        while (iter.hasNext()) {
            Property p = iter.nextProperty();
            String name = p.getName();

            if (name.equals("exo:id")) {
                task.setId(p.getString());
            } else if (name.equals("exo:projectId")) {
                task.setProjectId(p.getString());
            } else if (name.equals("exo:reporter")) {
                task.setReporter(p.getString());
            } else if (name.equals("exo:assignee")) {
                task.setAssignee(p.getString());
            } else if (name.equals("exo:title")) {
                task.setTitle(p.getString());
            } else if (name.equals("exo:status")) {
                task.setStatus(p.getString());
            } else if (name.equals("exo:priority")) {
                task.setPriority(Priority.getPriority((int) p.getLong()));
            } else if (name.equals("exo:labels")) {
                Set<String> label = new HashSet<String>();
                for (Value a : p.getValues()) {
                    label.add(a.getString());
                }
                task.setLabels(label);
            }
        }
        return task;
    }

    private Node getTaskNode(String id) throws RepositoryException {
        Query query = new Query();
        query.setId(id);
        List<Node> nodes = getTaskNode(query, 0, -1);

        if (!nodes.isEmpty()) {
            return nodes.get(0);
        } else {
            return null;
        }
    }

    private List<Node> getTaskNode(Query query, int offset, int limit) throws RepositoryException {
        StringBuilder sql = new StringBuilder("select * from exo:task where jcr:path like '");
        sql.append(JCRTaskService.USERS_PATH).append("/%/").append(JCRTaskService.APP_PATH);

        if (query.getProjectId() != null) {
            sql.append("/").append(Utils.queryEscape(query.getProjectId()));
        } else {
            sql.append("/%");
        }
        sql.append("/");
        if (query.getId() != null) {
            sql.append(Utils.queryEscape(query.getId())).append("' ");
        } else {
            sql.append("%' ");
        }

        if (query.getTitle() != null) {
            sql.append(" and contains(exo:title, '").append(Utils.queryEscape(query.getTitle()));
            sql.append("') ");
        }
        if (query.getAssignee() != null) {
            sql.append(" and contains(exo:assignee, '").append(Utils.queryEscape(query.getAssignee()));
            sql.append("') ");
        }
        if (query.getReporter() != null) {
            sql.append(" and contains(exo:reporter, '").append(Utils.queryEscape(query.getReporter()));
            sql.append("') ");
        }
        if (query.getDateCreated() != 0) {
            sql.append(" exo:dateCreated >= ").append(query.getDateCreated());
        }

        Session session = taskService.getSession();
        QueryImpl jcrQuery = (QueryImpl) session.getWorkspace().getQueryManager()
                .createQuery(sql.toString(), javax.jcr.query.Query.SQL);
        if (limit >= 0) {
            jcrQuery.setOffset(offset);
            jcrQuery.setLimit(limit);
        }
        NodeIterator results = jcrQuery.execute().getNodes();

        List<Node> nodes = new LinkedList<Node>();
        while (results.hasNext()) {
            nodes.add(results.nextNode());
        }
        return nodes;
    }
}
