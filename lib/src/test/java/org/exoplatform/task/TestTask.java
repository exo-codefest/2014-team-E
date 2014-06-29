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

    public void testFindingTask() {
        addTask(testProject.getId(), "foo task", "root", Priority.BLOCKER, Status.OPEN);
        addTask(testProject.getId(), "bar task", "john", Priority.MAJOR, Status.INPROGRESS);
        addTask(testProject.getId(), "zoo task", "root", Priority.MINOR, Status.RESOLVED);
        addTask(testProject.getId(), "zzee task", "root", Priority.BLOCKER, Status.RESOLVED);
        addTask(testProject.getId(), "dido task", "john", Priority.BLOCKER, Status.REFUSED);
        Query q = new Query();
        q.setAssignee("root");
        List<Task> list = service.findTasks(q, 0, -1);
        assertEquals(3, list.size());

        q.setTitle("foo");
        list = service.findTasks(q, 0, -1);
        assertEquals(1, list.size());

        q.setTitle("bar");
        list = service.findTasks(q, 0, -1);
        assertEquals(0, list.size());

        q = new Query();
        q.setPriority(Priority.BLOCKER);
        list = service.findTasks(q, 0, -1);
        assertEquals(3, list.size());

        q = new Query();
        q.setAssignee("root");
        q.setStatus(Status.OPEN);
        list = service.findTasks(q, 0, -1);
        assertEquals(1, list.size());

        q.setStatus(Status.RESOLVED);
        list = service.findTasks(q, 0, -1);
        assertEquals(2, list.size());

        q.setStatus(Status.INPROGRESS);
        list = service.findTasks(q, 0, -1);
        assertEquals(0, list.size());
    }

    protected void addTask(String projectId, String title, String assignee, Priority priority, Status status) {
        Task task = new Task(projectId, title, priority, assignee);
        task.setStatus(status);
        service.addTask(task);
    }
}
