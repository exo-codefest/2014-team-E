package org.exoplatform.task;

import org.exoplatform.task.model.Project;
import org.exoplatform.task.model.Task;

import java.util.HashMap;
import java.util.Map;

public class MemoryTaskService implements TaskService {

    private Map<String, Project> list = new HashMap<String, Project>();

    @Override
    public void createProject(Project p) {
        list.put(p.getId(), p);
    }

    @Override
    public Project getProject(String id) {
        return list.get(id);
    }

    @Override
    public void addTask(Task t) {
        
    }

}
