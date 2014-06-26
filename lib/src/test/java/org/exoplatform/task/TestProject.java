package org.exoplatform.task;

import org.exoplatform.task.model.Project;

import junit.framework.TestCase;

public class TestProject extends TestCase {
    private TaskService service = new MemoryTaskService();

    public void testProjectCreation() {
        Project p = new Project("root", "gatein", "my own gatein");
        service.createProject(p);

        Project project = service.getProject("root/gatein");
        assertNotNull(project);
        assertEquals("my own gatein", project.getDesc());
    }

    public void testFindingProject() {
    }
}
