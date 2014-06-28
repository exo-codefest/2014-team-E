package org.exoplatform.task;

import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.organization.UserHandler;
import org.exoplatform.task.model.Comment;
import org.exoplatform.task.model.Project;
import org.exoplatform.task.model.Query;
import org.exoplatform.task.model.Task;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MemoryTaskService implements TaskService {

    private Map<String, Project> list = new HashMap<String, Project>();
    
    private Map<String, Task> tasks = new HashMap<String, Task>();
    private Map<String, Comment> comments = new HashMap<String, Comment>();

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
            return Collections.<Project>emptyList();
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
    public Project createProject(Project p) throws TaskServiceException {
        if (p.getName() == null || p.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Project name can NOT be empty");
        }

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

        p.setDateCreated(System.currentTimeMillis());
        list.put(p.getId(), p);
        return p;
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
    public void removeProject(String id) {
        list.remove(id);
    }

    public Task addTask(Task t) {
        //Generate ID
        t.setId(UUID.randomUUID().toString());
        tasks.put(t.getId(), t);
        return t;
    }

    @Override
    public Task removeTask(String id) {
        return tasks.remove(id);
    }

    @Override
    public Task updateTask(Task t) {
        tasks.put(t.getId(), t);
        return t;
    }

    @Override
    public Task getTask(String id) {
        return tasks.get(id);
    }

    @Override
    public List<Task> getTasksByProject(String projectId,int offset, int limit) {
        if(projectId == null) {
            return Collections.<Task>emptyList();
        }

        List<Task> tasks = new LinkedList<Task>();
        for(Task task : this.tasks.values()) {
            if(task.getProjectId().equals(projectId)) {
                tasks.add(task);
            }
        }        
        if (limit >= 0) {
            return Utils.subList(tasks, offset, limit); 
        } else {
            return tasks;
        }
    }

    @Override
    public List<Task> findTasks(Query query, int offset, int limit) {
        List<Task> t = new ArrayList<Task>();

        for (Task task : tasks.values()) {
           if (task.getTitle().contains(query.getTitle())) {
               t.add(task);
           }
        }
        if (limit >=0) {
            return Utils.subList(t, offset, limit);
        } else {
            return t;
        }
    }

    @Override
    public Comment getCommentById(String id) {
        return comments.get(id);
    }
    

    /**
     * @see org.exoplatform.task.TaskService#getCommentByTask(java.lang.String, int, int)
     */
    @Override
    public List<Comment> getCommentByTask(String taskId, int offset, int limit) {
        List<Comment> cmts = new LinkedList<Comment>();
        for(Comment c : this.comments.values()) {
            if(c.getTaskId().equals(taskId)) {
                cmts.add(c);
            }
        }

        Collections.sort(cmts, new Comparator<Comment>() {
            @Override
            public int compare(Comment o1, Comment o2) {
                if(o2.getCreatedDate() == null) {
                    if(o1.getCreatedDate() == null) {
                        return 0;
                    } else {
                        return -1;
                    }
                } else if(o1.getCreatedDate() == null) {
                    return 1;
                }
                return o2.getCreatedDate().compareTo(o1.getCreatedDate());
            }
        });
        Collections.reverse(cmts);

        int size = cmts.size();
        if(size < offset) {
            return Collections.emptyList();
        }
        int end = offset + limit;
        if(end > size) {
            end = size;
        }

        return cmts.subList(offset, end);
    }

    @Override
    public Comment addComment(Comment comment) {
        comment.setId(UUID.randomUUID().toString());
        comments.put(comment.getId(), comment);
        return comment;
    }

    @Override
    public Comment removeComment(String commentId) {
        return comments.remove(commentId);
    }

    @Override
    public Comment updateComment(Comment comment) {
        comments.put(comment.getId(), comment);
        return comment;
    }
}
