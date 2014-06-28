package org.exoplatform.task.model;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author <a href="trongtt@gmail.com">Trong Tran</a>
 * @version $Revision$
 */
public class Task implements Activity {
    private String id;

    private String projectId;
    
    private String reporter;
    
    private String assignee;
    
    private Status status = Status.OPEN;
    
    private String title;

    private Priority priority = Priority.UNDEFINED;

    private Set<String> labels = new HashSet<String>();
    
    private long createdDate;
    
    private long modifedDate;

    public Task() {
    }
    
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

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Set<String> getLabels() {
        return Collections.unmodifiableSet(labels);
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
        return new Date(createdDate);
    }

    @Override
    public Date getModifiedDate() {
        return new Date(modifedDate);
    }

    public void setModifedDate(long modifedDate) {
        this.modifedDate = modifedDate;
    }

    public void setCreatedDate(long createdDate) {
        this.createdDate = createdDate;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }
}
