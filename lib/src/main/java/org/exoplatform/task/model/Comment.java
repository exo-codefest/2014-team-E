package org.exoplatform.task.model;

import java.util.Date;

/**
 * @author <a href="trongtt@gmail.com">Trong Tran</a>
 * @version $Revision$
 */
public class Comment implements Activity {

    public String text;

    @Override
    public Date getDate() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getText() {
        return text;
    }
}
