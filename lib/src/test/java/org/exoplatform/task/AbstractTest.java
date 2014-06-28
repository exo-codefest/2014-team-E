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
import org.exoplatform.services.organization.impl.mock.DummyOrganizationService;
import org.exoplatform.task.model.Project;

@ConfiguredBy({
    @ConfigurationUnit(scope = ContainerScope.PORTAL, path = "conf/portal/team_e/task_configuration.xml"),
    @ConfigurationUnit(scope = ContainerScope.PORTAL, path = "conf/portal-configuration.xml"),
    @ConfigurationUnit(scope = ContainerScope.PORTAL, path = "conf/exo.portal.component.test.jcr-configuration.xml"),
    @ConfigurationUnit(scope = ContainerScope.PORTAL, path = "conf/portal/team_e/jcr_configuration.xml")
  })
public abstract class AbstractTest extends AbstractKernelTest {
    protected final String username = "root";

    protected TaskService service;

    @Override
    protected void setUp() throws Exception {        
        super.setUp();
//        this.service = new MemoryTaskService(new DummyOrganizationService());
        this.service = getService(TaskService.class);
    }

    protected Session getSession() throws Exception {
        SessionProviderService providerService = getService(SessionProviderService.class);
        RepositoryService repoService = getService(RepositoryService.class);
        ManageableRepository currentRepo = repoService.getDefaultRepository();
        Session session = providerService.getSystemSessionProvider(null).getSession("team_e", currentRepo);
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
}
