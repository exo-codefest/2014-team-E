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

    @Override
    public Date getDate() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getProjectId() {
        return projectId;
    }
}
