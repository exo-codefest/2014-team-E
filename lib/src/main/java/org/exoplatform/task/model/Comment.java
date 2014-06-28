package org.exoplatform.task.model;

import java.util.Date;

/**
 * @author <a href="trongtt@gmail.com">Trong Tran</a>
 * @version $Revision$
 */
public class Comment implements Activity {

    private String id;
    private String taskId;
    private String author;
    private String text;
    private Date createdDate;
    private long modifiedDate;

    public Comment() {
        this(null, null);
    }

    public Comment(String author, String text) {
        this.text = text;
        this.author = author;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public Date getCreatedDate() {
        return this.createdDate;
    }

    public void setModifiedDate(long modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    @Override
    public Date getModifiedDate() {
        return new Date(modifiedDate);
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}