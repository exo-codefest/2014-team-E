package org.exoplatform.task.jcr;

import java.util.List;

import org.exoplatform.task.JCRTaskService;
import org.exoplatform.task.model.Query;
import org.exoplatform.task.model.Task;

public class TaskDAO {
    private JCRTaskService taskService;

    public TaskDAO(JCRTaskService taskService) {
        this.taskService = taskService;
    }

    /**
     * @param t
     */
    public void addTask(Task t) {
        // TODO Auto-generated method stub
        
    }

    /**
     * @param id
     * @return
     */
    public Task removeTask(String id) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @param t
     */
    public void updateTask(Task t) {
        // TODO Auto-generated method stub
        
    }

    /**
     * @param id
     * @return
     */
    public Task getTask(String id) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @param projectId
     * @param offset
     * @param limit
     * @return
     */
    public List<Task> getTaskByProject(String projectId, int offset, int limit) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @param query
     * @param offset
     * @param limit
     * @return
     */
    public List<Task> findTasks(Query query, int offset, int limit) {
        // TODO Auto-generated method stub
        return null;
    }
}
