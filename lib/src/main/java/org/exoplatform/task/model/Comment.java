package org.exoplatform.task.model;

import java.util.Date;
import java.util.UUID;

/**
 * @author <a href="trongtt@gmail.com">Trong Tran</a>
 * @version $Revision$
 */
public class Comment implements Activity {

    private String id;
    
    public String text;

    public Comment(String text) {
        this.id = UUID.randomUUID().toString();
        this.text = text;
    }

    public String getId() {
        return id;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public Date getDate() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getText() {
        return text;
    }
}
