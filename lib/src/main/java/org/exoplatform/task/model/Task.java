package org.exoplatform.task.model;

import java.util.Date;
import java.util.List;

/**
 * @author <a href="trongtt@gmail.com">Trong Tran</a>
 * @version $Revision$
 */
public class Task implements Activity {
    public String title;

    public List<Comment> comments;

    private String projectId;

    @Override
    public Date getDate() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getProjectId() {
        return projectId;
    }
}
