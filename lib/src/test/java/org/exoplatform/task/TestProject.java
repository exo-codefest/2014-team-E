package org.exoplatform.task;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.exoplatform.task.model.Project;

public class TestProject extends AbstractTest {     
    
    public void testProjectCreation() throws Exception {
        Project p = new Project("root", "calendar", "test project creation");
        service.createProject(p);

        List<Project> projects = service.getProjectsByUser("root");
        assertTrue(projects.size() == 2);
        //project list is ordered in decrease of createdDate
        assertEquals("test project creation", projects.get(0).getDesc());

        p = new Project(null, null, null);
        try {
            service.createProject(p);
            fail("It should not save EMPTY project successfully");
        } catch (IllegalArgumentException e) {
        }
    }

    public void testSharingProject() {
        Project p = new Project("mary", "maryCal", "");
        Set<String> groupId = new HashSet<String>();
        groupId.add("/platform/administrators");
        p.setMemberships(groupId);
        
        service.createProject(p);
        
        List<Project> projects = service.getProjectsByUser("mary");
        assertEquals(1, projects.size());
        
        projects = service.getProjectsByUser("root");
        assertEquals(2, projects.size());
    }
}
