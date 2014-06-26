package org.exoplatform.task;

import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.organization.UserHandler;
import org.exoplatform.task.model.Project;
import org.exoplatform.task.model.Task;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MemoryTaskService implements TaskService {

    private Map<String, Project> list = new HashMap<String, Project>();
    
    private UserHandler userHandler;

    public MemoryTaskService(OrganizationService service) {
        this.userHandler = service.getUserHandler();
    }

    @Override
    public Project getProject(String id) {
        return list.get(id);
    }

    @Override
    public List<Project> getProjectsByUser(String username) {
        if (username == null) {
            return Collections.emptyList();
        }
        
        List<Project> results = new LinkedList<Project>();
        for (Project p : list.values()) {
            if (username.equals(p.getOwner()) || p.getMemberships().contains(username)) {
                results.add(p);
            }
        }
        return results;
    }

    @Override
    public void createProject(Project p) throws TaskServiceException {        
        if (getProject(p.getId()) != null) {
            throw new TaskServiceException(TaskServiceException.DUPLICATED, "Duplicated : " + p.getId());
        }
        
        try {
            if (userHandler.findUserByName(p.getOwner()) == null) {
                throw new TaskServiceException(TaskServiceException.NON_EXITS_OWNER, "Non exists owner: " + p.getOwner());
            }
        } catch (Exception e) {
            throw new TaskServiceException(e.getMessage(), e);
        }
        
        list.put(p.getId(), p);
    }
    
    @Override
    public void updateProject(Project p) throws TaskServiceException {
        Project project = getProject(p.getId());
        if (project == null) {
            throw new TaskServiceException(TaskServiceException.NON_EXITS_PROJECT, "Can't update. Non exists: " + p.getId());
        }
        
        project.setDesc(p.getDesc());
        project.setMemberships(p.getMemberships());
        list.put(p.getId(), p);
    }

    @Override
    public void deleteProject(String id) {
        list.remove(id);
    }

    @Override
    public void addTask(Task t) {
        
    }
}
