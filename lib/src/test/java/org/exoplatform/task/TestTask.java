package org.exoplatform.task;

import java.util.List;

import org.exoplatform.component.test.AbstractKernelTest;
import org.exoplatform.component.test.ConfigurationUnit;
import org.exoplatform.component.test.ConfiguredBy;
import org.exoplatform.component.test.ContainerScope;
import org.exoplatform.task.model.Project;
import org.exoplatform.task.model.Task;

@ConfiguredBy({
    @ConfigurationUnit(scope = ContainerScope.PORTAL, path = "conf/configuration.xml")
  })
public class TestTask extends AbstractKernelTest {
    private final String username = "root";
    
    private TaskService service;

    @Override
    protected void setUp() throws Exception {        
        super.setUp();
        this.service = (TaskService)getContainer().getComponentInstanceOfType(TaskService.class);
    }
    
    @Override
    protected void tearDown() throws Exception {
        List<Project> projects = this.service.getProjectsByUser(username);
        for (Project p : projects) {
            this.service.removeProject(p.getId());
        }
        super.tearDown();
    }

    public void testCreateTask() throws TaskServiceException {
        Project p = new Project(username, "gatein", "my own gatein");
        service.createProject(p);

        Task task = new Task(p.getId(), "hi john");
        service.addTask(task);

        List<Task> findTasks = service.findTasks(new Query("john"));
        assertEquals(1, findTasks.size());
    }

    public void testCreateTaskWithNonExistingProject() throws TaskServiceException {
        Project p = new Project(username, "gatein", "my own gatein");
        service.createProject(p);

        Project project = service.getProject("root/gatein");
        assertNotNull(project);
        assertEquals("my own gatein", project.getDesc());
    }
}
