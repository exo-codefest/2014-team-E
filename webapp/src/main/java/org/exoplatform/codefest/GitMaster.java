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

import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.task.MemoryTaskService;
import org.exoplatform.task.TaskService;
import org.exoplatform.task.TaskServiceException;
import org.exoplatform.task.model.Project;
import org.exoplatform.task.model.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

/**
 * @author <a href="trongtt@gmail.com">Trong Tran</a>
 * @version $Revision$
 */
public class GitMaster extends GenericPortlet {

    TaskService service;

    @Override
    public void init() throws PortletException {
        super.init();
        ExoContainer container = ExoContainerContext.getCurrentContainer();
        service = (TaskService)container.getComponentInstanceOfType(TaskService.class);
    }

    @Override
    protected void doView(RenderRequest request, RenderResponse response) throws PortletException, IOException {
        String user = request.getRemoteUser();
        //TODO: process if user is not logged in

        //Load recent project
        List<Project> recentProjects = service.getProjectsByUser(user);
        request.setAttribute("recentProjects", recentProjects);

        String view = request.getParameter("view");
        if (view != null) {
            if (view.equals("issues")) {

                String projectId = request.getParameter("projectId");

                Project project = service.getProject(projectId);
                List<Task> tasks = service.getTasksByProject(projectId);
                request.setAttribute("project", project);
                request.setAttribute("tasks", tasks);

                render("/issues.jsp", request, response);
                return;
            }

            if(view.equals("detail")) {
                render("/detail.jsp", request, response);
                return;
            }
        }

        //. Projects view
        List<Project> projects;
        if(user != null) {
            projects = service.getProjectsByUser(user);
        } else {
            projects = new ArrayList<Project>();
        }
        request.setAttribute("projects", projects);

        render("/projects.jsp", request, response);
    }

    public void render(String template, RenderRequest request, RenderResponse response) throws PortletException, IOException {
        PortletContext context = getPortletContext();
        context.getRequestDispatcher("/includes/begin.jsp").include(request, response);
        context.getRequestDispatcher(template).include(request, response);
        context.getRequestDispatcher("/includes/end.jsp").include(request, response);
    }

    @Override
    public void processAction(ActionRequest request, ActionResponse response) throws PortletException, IOException {
        String objectType = request.getParameter("objectType");
        String action = request.getParameter("action");

        if("project".equals(objectType)) {
            this.processProjectAction(action, request, response);
        } else if("task".equals(objectType)) {
            this.processTaskAction(action, request, response);
        }

        return;
    }

    protected void processProjectAction(String action, ActionRequest request, ActionResponse response) {
        response.setRenderParameter("view", "projects");

        String user = request.getRemoteUser();
        if(user == null) {
            //TODO: process if user not logged in
            return;
        }

        if("create".equals(action)) {
            String name = request.getParameter("name");
            String description = request.getParameter("description");

            Project project = new Project(user, name, description);
            try {
                service.createProject(project);
            } catch (TaskServiceException ex) {
                //TODO: need handle this error
                ex.printStackTrace();
            }

            return;
        }

        if("delete".equals(action)) {
            String id = request.getParameter("objectId");
            //String projectId = String.format("%s/%s", user, id);
            service.removeProject(id);
        }
    }

    protected void processTaskAction(String action, ActionRequest request, ActionResponse response) {
        String projectId = request.getParameter("projectId");
        String title = request.getParameter("title");

        response.setRenderParameter("view", "issues");
        response.setRenderParameter("projectId", projectId);

        String user = request.getRemoteUser();
        if(user == null) {
            return;
        }

        if("create".equals(action)) {
            Task task = new Task(projectId, title);
            service.addTask(task);
            return;
        }

        if("delete".equals(action)) {
            String taskId =  request.getParameter("objectId");
            service.removeTask(taskId);
            return;
        }
    }
}
