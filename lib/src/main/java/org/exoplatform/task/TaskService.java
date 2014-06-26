package org.exoplatform.task;

import org.exoplatform.task.model.Project;
import org.exoplatform.task.model.Task;

public interface TaskService {
    
    public void createProject(Project p);

    public Project getProject(String id);

    public void addTask(Task t);
}
