package org.exoplatform.task;

import org.exoplatform.task.model.Project;

public interface TaskService {
    public void createProject(Project p);

    public void getProject(String string);
}
