package org.exoplatform.task;

import org.exoplatform.task.model.Project;

public class TestProject extends AbstractTest {     
    
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
