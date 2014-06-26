package org.exoplatform.task;

import org.exoplatform.task.model.Project;

import java.util.ArrayList;
import java.util.List;

public class MemoryTaskService implements TaskService {

    private List<Project> list = new ArrayList<Project>();

    @Override
    public void createProject(Project p) {
        list.add(p);
    }

    @Override
    public void getProject(String string) {
        // TODO Auto-generated method stub
        
    }

}
