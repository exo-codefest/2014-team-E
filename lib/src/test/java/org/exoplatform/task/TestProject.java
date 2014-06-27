package org.exoplatform.task;

import org.exoplatform.task.model.Project;

import java.util.List;

public class TestProject extends AbstractTest {     
    
    public void testProjectCreation() throws Exception {
        Project p = new Project("root", "gatein", "my own gatein");
        service.createProject(p);

        List<Project> projects = service.getProjectsByUser("root");
        assertTrue(projects.size() == 1);
        assertEquals("my own gatein", projects.get(0).getDesc());

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
