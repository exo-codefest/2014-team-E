package org.exoplatform.task;

import java.util.List;

import org.exoplatform.task.model.Project;
import org.exoplatform.task.model.Query;
import org.exoplatform.task.model.Task;

public class TestComment extends AbstractTest {

    public void testCreateTask() throws TaskServiceException {
        Project p = new Project(username, "gatein", "my own gatein");
        service.createProject(p);

        Task task = new Task(p.getId(), "hi john");
        service.addTask(task);

        List<Task> findTasks = service.findTasks(new Query("john"), 0, 10);
        assertEquals(1, findTasks.size());
    }
}
