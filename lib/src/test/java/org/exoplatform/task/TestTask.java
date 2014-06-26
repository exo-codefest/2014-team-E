package org.exoplatform.task;

import org.exoplatform.services.organization.impl.mock.DummyOrganizationService;
import org.exoplatform.task.model.Project;
import org.exoplatform.task.model.Task;

import java.util.List;

import junit.framework.TestCase;

public class TestTask extends TestCase {
    private TaskService service = new MemoryTaskService(new DummyOrganizationService());

    public void testCreateTask() throws TaskServiceException {
        Project p = new Project("root", "gatein", "my own gatein");
        service.createProject(p);

        Task task = new Task(p.getId(), "hi john");
        service.addTask(task);

        List<Task> findTasks = service.findTasks(new Query("john"));
        assertEquals(1, findTasks.size());
    }

    public void testCreateTaskWithNonExistingProject() throws TaskServiceException {
        Project p = new Project("root", "gatein", "my own gatein");
        service.createProject(p);

        Project project = service.getProject("root/gatein");
        assertNotNull(project);
        assertEquals("my own gatein", project.getDesc());
    }
}
