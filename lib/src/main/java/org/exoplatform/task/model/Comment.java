package org.exoplatform.task.model;

import java.util.Date;
import java.util.UUID;

/**
 * @author <a href="trongtt@gmail.com">Trong Tran</a>
 * @version $Revision$
 */
public class Comment implements Activity {

    private String id;
    
    private String text;
    private String author;
    private Date createdDate;


    public Comment(String author, String text) {
        this.id = UUID.randomUUID().toString();
        this.text = text;
        this.author = author;
        this.createdDate = new Date();
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

    @Override
    public Date getCreatedDate() {
        return this.createdDate;
    }

    @Override
    public Date getModifiedDate() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
