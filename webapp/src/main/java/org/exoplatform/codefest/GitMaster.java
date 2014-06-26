/*
 * Copyright (C) 2014 eXo Platform SAS.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.exoplatform.codefest;

import org.exoplatform.task.MemoryTaskService;
import org.exoplatform.task.TaskService;
import org.exoplatform.task.model.Project;

import java.io.IOException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

/**
 * @author <a href="trongtt@gmail.com">Trong Tran</a>
 * @version $Revision$
 */
public class GitMaster extends GenericPortlet {

    TaskService service  = new MemoryTaskService();

    @Override
    protected void doView(RenderRequest request, RenderResponse response) throws PortletException, IOException {
        String view = request.getParameter("view");
        if (view != null) {
            if (view.equals("issues")) {
                getPortletContext().getRequestDispatcher("/issues.jsp").include(request, response);
                return;
            }

            if(view.equals("detail")) {
                getPortletContext().getRequestDispatcher("/detail.jsp").include(request, response);
                return;
            }
        }

        getPortletContext().getRequestDispatcher("/projects.jsp").include(request, response);
    }

    @Override
    public void processAction(ActionRequest request, ActionResponse response) throws PortletException, IOException {
        String objectType = request.getParameter("objectType");
        String action = request.getParameter("action");

        if("project".equals(objectType)) {
            this.processProjectAction(action, request, response);
        }

        return;
    }

    protected void processProjectAction(String action, ActionRequest request, ActionResponse response) {
        if("create".equals(action)) {
            String name = request.getParameter("name");
            String description = request.getParameter("description");

            String user = request.getRemoteUser();
            if(user == null) {
                //TODO: process if user not logged in
                return;
            }
            Project project = new Project(user, name, description);
            service.createProject(project);

            response.setRenderParameter("view", "projects");
        }
    }
}
