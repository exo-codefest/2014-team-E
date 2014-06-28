package org.exoplatform.task;

import org.exoplatform.task.model.Project;

import java.util.List;

public class TestProject extends AbstractTest {     
    
    public void testProjectCreation() throws Exception {
        Project p = new Project("root", "calendar", "test project creation");
        service.createProject(p);

        List<Project> projects = service.getProjectsByUser("root");
        assertTrue(projects.size() == 2);
        assertEquals("test project creation", projects.get(1).getDesc());

        p = new Project(null, null, null);
        try {
            service.createProject(p);
            fail("It should not save EMPTY project successfully");
        } catch (IllegalArgumentException e) {
        }
    }

    public void testFindingProject() {
        
    }
}
