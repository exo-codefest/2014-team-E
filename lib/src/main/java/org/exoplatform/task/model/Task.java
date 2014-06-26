package org.exoplatform.task.model;

import java.util.Date;
import java.util.List;
import java.util.Set;

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

    private Set<String> labels;

    public Task(String projectId, String title) {
        this.projectId = projectId;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Set<String> getLabels() {
        return labels;
    }

    public void setLabels(Set<String> labels) {
        this.labels = labels;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getProjectId() {
        return projectId;
    }

    @Override
    public Date getCreatedDate() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Date getModifiedDate() {
        // TODO Auto-generated method stub
        return null;
    }
}
