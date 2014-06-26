package org.exoplatform.task;

import org.exoplatform.task.model.Project;

import junit.framework.TestCase;

public class TestProject extends TestCase {
    private TaskService service = new MemoryTaskService();

    public void testProjectCreation() {
        Project p = new Project("pro_1", "dec_1");
        service.createProject(p);

        service.getProject("pro_1");
    }
}
