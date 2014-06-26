package org.exoplatform.task.model;

import java.util.Date;
import java.util.List;

/**
 * @author <a href="trongtt@gmail.com">Trong Tran</a>
 * @version $Revision$
 */
public class Task implements Activity {
    private String id;

    private String projectId;
    
    private String reporter;
    
    private List<String> assignee;
    
    private String status;
    
    public String title;

    public List<Comment> comments;

    private List<String> labels;

    public String getId() {
        return id;
    }

    public String getReporter() {
        return reporter;
    }

    public void setReporter(String reporter) {
        this.reporter = reporter;
    }

    public List<String> getAssignee() {
        return assignee;
    }

    public void setAssignee(List<String> assignee) {
        this.assignee = assignee;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    @Override
    public Date getDate() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getProjectId() {
        return projectId;
    }
}
