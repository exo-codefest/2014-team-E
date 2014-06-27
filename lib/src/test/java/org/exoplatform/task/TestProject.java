package org.exoplatform.task;

import javax.jcr.Session;

import java.util.List;

import org.exoplatform.component.test.AbstractKernelTest;
import org.exoplatform.component.test.ConfigurationUnit;
import org.exoplatform.component.test.ConfiguredBy;
import org.exoplatform.component.test.ContainerScope;
import org.exoplatform.services.jcr.RepositoryService;
import org.exoplatform.services.jcr.core.ManageableRepository;
import org.exoplatform.services.jcr.ext.app.SessionProviderService;
import org.exoplatform.task.model.Project;

@ConfiguredBy({
    @ConfigurationUnit(scope = ContainerScope.PORTAL, path = "conf/portal/org.exoplatform.addons.codefest.team_e.service_configuration.xml"),
    @ConfigurationUnit(scope = ContainerScope.PORTAL, path = "conf/portal-configuration.xml"),
    @ConfigurationUnit(scope = ContainerScope.PORTAL, path = "conf/exo.portal.component.test.jcr-configuration.xml"),
    @ConfigurationUnit(scope = ContainerScope.PORTAL, path = "conf/portal/team_e/jcr_configuration.xml")
  })
public class TestProject extends AbstractKernelTest {
    private final String username = "root";
    
    private TaskService service;
    
    @Override
    protected void setUp() throws Exception {        
        super.setUp();
        this.service = getService(TaskService.class);
    }
    
    private Session getSession() throws Exception {
        SessionProviderService providerService = getService(SessionProviderService.class);
        RepositoryService repoService = getService(RepositoryService.class);        
        ManageableRepository currentRepo = repoService.getDefaultRepository();
        Session session = providerService.getSystemSessionProvider(null).getSession(currentRepo.getConfiguration().getDefaultWorkspaceName(),
                                    currentRepo);
        return session;
    }
    
    @SuppressWarnings("unchecked")
    protected <T> T getService(Class<T> t) {
        return (T)getContainer().getComponentInstanceOfType(t);
    }
    
    @Override
    protected void tearDown() throws Exception {        
        List<Project> projects = this.service.getProjectsByUser(username);
        for (Project p : projects) {
            this.service.removeProject(p.getId());
        }
        super.tearDown();
    }    
    
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
