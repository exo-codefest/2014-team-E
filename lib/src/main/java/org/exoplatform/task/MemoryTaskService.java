package org.exoplatform.task;

import org.exoplatform.task.model.Project;

import java.util.HashMap;
import java.util.Map;

public class MemoryTaskService implements TaskService {

    private Map<String, Project> list = new HashMap<String, Project>();

    @Override
    public void createProject(Project p) {
        list.put(p.getName(), p);
    }

    @Override
    public Project getProject(String name) {
        return list.get(name);
    }

}
