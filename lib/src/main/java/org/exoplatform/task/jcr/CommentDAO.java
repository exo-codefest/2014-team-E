package org.exoplatform.task.jcr;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.exoplatform.services.jcr.impl.core.query.QueryImpl;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.task.JCRTaskService;
import org.exoplatform.task.TaskServiceException;
import org.exoplatform.task.Utils;
import org.exoplatform.task.model.Comment;

public class CommentDAO {
    private static final Log log = ExoLogger.getLogger(CommentDAO.class);

    private JCRTaskService taskService;

    public CommentDAO(JCRTaskService taskService) {
        this.taskService = taskService;
    }

    public Comment getCommentById(String id) {
        if (id == null) {
            return null;
        }

        try {
            Node node = getCommentNode(id);
            if (node != null) {
                return buildComment(node);
            } else {
                return null;
            }
        } catch (RepositoryException e) {
            log.error(e);
            throw new RuntimeException(e);
        }
    }

    public List<Comment> getCommentByTask(String taskId, int offset, int limit) {
        List<Comment> comments = new LinkedList<Comment>();

        if (taskId != null) {
            try {
                List<Node> nodes = getCommentNode(null, taskId, offset, limit);
                for (Node node : nodes) {
                    comments.add(buildComment(node));
                }
            } catch (RepositoryException e) {
                log.error(e);
                throw new RuntimeException(e);
            }
        }

        return comments;
    }

    public Comment addComment(Comment c) throws TaskServiceException {
        try {
            Node taskNode = taskService.getTaskDAO().getTaskNode(c.getTaskId());

            if (taskNode == null) {
                throw new TaskServiceException(TaskServiceException.NON_EXITS_PROJECT, "Can't create comment. Task "
                        + c.getTaskId() + " not found");
            }

            c.setCreatedDate(new Date());
            c.setId(UUID.randomUUID().toString());
            Node commentNode = taskNode.addNode(c.getId(), "exo:comment");
            setCommentProperties(commentNode, c);

            taskNode.save();
            return buildComment(commentNode);
        } catch (RepositoryException e) {
            log.error(e);
            throw new RuntimeException(e);
        }
    }

    public Comment removeComment(String commentId) {
        Comment comment = null;
        try {
            Node commentNode = getCommentNode(commentId);
            if (commentNode != null) {
                comment = buildComment(commentNode);

                Session session = commentNode.getSession();
                commentNode.remove();
                session.save();
            }
            return comment;
        } catch (RepositoryException e) {
            log.error(e);
            throw new RuntimeException(e);
        }
    }

    public Comment updateComment(Comment comment) throws TaskServiceException {
        try {
            Node commentNode = getCommentNode(comment.getId());
            if (commentNode == null) {
                throw new TaskServiceException(TaskServiceException.NON_EXITS_COMMENT, "Can't update non-exists comment: "
                        + comment.getId());
            }
            Node taskNode = taskService.getTaskDAO().getTaskNode(comment.getTaskId());
            if (taskNode == null) {
                throw new TaskServiceException(TaskServiceException.NON_EXITS_TASK, "Can't update comment to non-exists task: "
                        + comment.getTaskId());
            }

            if (!commentNode.getProperty("exo:taskId").getString().equals(comment.getTaskId())) {
                throw new RuntimeException("Don't allow to change taskId of comment");
            }
            
            comment.setModifiedDate(System.currentTimeMillis());
            setCommentProperties(commentNode, comment);
            commentNode.getSession().save();
            
            return comment;
        } catch (RepositoryException e) {
            log.error(e);
            throw new RuntimeException(e);
        }
    }

    private void setCommentProperties(Node commentNode, Comment c) throws RepositoryException {
        commentNode.setProperty("exo:id", c.getId());
        commentNode.setProperty("exo:taskId", c.getTaskId());
        commentNode.setProperty("exo:owner", c.getAuthor());
        commentNode.setProperty("exo:text", c.getText());
        Date createdDate = c.getCreatedDate();
        if (createdDate != null) {
            commentNode.setProperty("exo:createdDate", createdDate.getTime());
        }
        Date modifedDate = c.getModifiedDate();
        if (modifedDate != null) {
            commentNode.setProperty("exo:modifiedDate", modifedDate.getTime());
        }
    }

    private Comment buildComment(Node node) throws RepositoryException {
        if (node == null) {
            return null;
        }

        Comment comment = new Comment();

        PropertyIterator iter = node.getProperties("exo:*");
        while (iter.hasNext()) {
            Property p = iter.nextProperty();
            String name = p.getName();

            if (name.equals("exo:id")) {
                comment.setId(p.getString());
            } else if (name.equals("exo:taskId")) {
                comment.setTaskId(p.getString());
            } else if (name.equals("exo:owner")) {
                comment.setAuthor(p.getString());
            } else if (name.equals("exo:text")) {
                comment.setText(p.getString());
            } else if (name.equals("exo:createdDate")) {
                comment.setCreatedDate(new Date(p.getLong()));
            } else if (name.equals("exo:modifedDate")) {
                comment.setModifiedDate(p.getLong());
            }
        }
        return comment;
    }
    
    private Node getCommentNode(String id) throws RepositoryException {
        List<Node> nodes = getCommentNode(id, null, 0, -1);
        if (nodes.size() > 0) {
            return nodes.get(0);                
        } else {
            return null;
        }
    }

    private List<Node> getCommentNode(String id, String taskId, int offset, int limit) throws RepositoryException {
        StringBuilder sql = new StringBuilder("select * from exo:comment where jcr:path like '");
        sql.append(JCRTaskService.USERS_PATH).append("/%/").append(JCRTaskService.APP_PATH);
        sql.append("/%/");

        if (taskId != null) {
            sql.append(Utils.queryEscape(taskId)).append("/");
        } else {
            sql.append("%/");
        }
        if (id != null) {
            sql.append(Utils.queryEscape(id)).append("' ");
        } else {
            sql.append("%' order by exo:createdDate DESC");
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
