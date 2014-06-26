package org.exoplatform.task;

import org.exoplatform.task.model.Project;

import junit.framework.TestCase;

public class TestProject extends TestCase {
    private TaskService service = new MemoryTaskService();

    public void testProjectCreation() {
        Project p = new Project("id_1", "pro_1", "desc_1");
        service.createProject(p);

        Project project = service.getProject("id_1");
        assertNotNull(project);
        assertEquals("desc_1", project.getDesc());
    }
}
