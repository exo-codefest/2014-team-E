package org.exoplatform.task;

import java.util.List;

import org.exoplatform.task.model.Project;
import org.exoplatform.task.model.Task;

public interface TaskService {

    /**
     * Return owner and shared projects
     * @param username
     */
    public List<Project> getProjectsByUser(String username);

    /**
     * create project <br/>
     * throw exception if duplicated, owner doesn't exits, fail validation
     * @param p
     */
    public void createProject(Project p) throws TaskServiceException;

    public void updateProject(Project p) throws TaskServiceException;

    public Project getProject(String id);

    public void deleteProject(String id);

    public void addTask(Task t);
}
