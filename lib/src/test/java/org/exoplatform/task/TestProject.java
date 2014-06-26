package org.exoplatform.task;

import org.exoplatform.services.organization.impl.mock.DummyOrganizationService;
import org.exoplatform.task.model.Project;

import junit.framework.TestCase;

public class TestProject extends TestCase {
    private TaskService service = new MemoryTaskService(new DummyOrganizationService());

    public void testProjectCreation() throws Exception {
        Project p = new Project("root", "gatein", "my own gatein");
        service.createProject(p);

        Project project = service.getProject("root/gatein");
        assertNotNull(project);
        assertEquals("my own gatein", project.getDesc());
    }

    public void testFindingProject() {
    }
}
