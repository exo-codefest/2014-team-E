package org.exoplatform.task;

import java.util.List;

import org.exoplatform.task.model.Priority;
import org.exoplatform.task.model.Query;
import org.exoplatform.task.model.Status;
import org.exoplatform.task.model.Task;

public class TestTask extends AbstractTest {

    public void testCreateTask() throws TaskServiceException {
        Task task = new Task(testProject.getId(), "hi john");
        task.setAssignee("john");
        task.setPriority(Priority.BLOCKER);
        task.setStatus(Status.OPEN);
        service.addTask(task);

        List<Task> findTasks = service.findTasks(new Query("john"), 0, 10);
        assertEquals(1, findTasks.size());
        Task t = findTasks.get(0);
        assertEquals("hi john", t.getTitle());
        assertEquals(Priority.BLOCKER, t.getPriority());
        assertEquals(Status.OPEN, t.getStatus());
    }

    
}
