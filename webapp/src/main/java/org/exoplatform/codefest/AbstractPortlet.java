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

import org.exoplatform.commons.utils.ListAccess;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.portal.application.PortalRequestContext;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.organization.User;
import org.exoplatform.task.TaskService;
import org.exoplatform.task.model.Comment;
import org.exoplatform.task.model.Priority;
import org.exoplatform.task.model.Project;
import org.exoplatform.task.model.Status;
import org.exoplatform.task.model.Task;
import org.exoplatform.web.application.ApplicationMessage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.portlet.ResourceURL;

/**
 * @author <a href="trongtt@gmail.com">Trong Tran</a>
 * @version $Revision$
 */
public abstract class AbstractPortlet extends GenericPortlet {
    public static final String PARAM_OBJECT_TYPE = "objectType";
    public static final String PARAM_ACTION = "action";
    public static final String PARAM_OBJECT_ID = "objectId";

    public static final String OBJECT_TYPE_COMMENT = "comment";
    public static final String OBJECT_TYPE_TASK = "task";

    TaskService service;
    OrganizationService orgService;

    @Override
    public void init() throws PortletException {
        super.init();
        ExoContainer container = ExoContainerContext.getCurrentContainer();
        orgService = (OrganizationService)container.getComponentInstanceOfType(OrganizationService.class);
        service = (TaskService)container.getComponentInstanceOfType(TaskService.class);
    }

    public void pushNotification(String message) {
        PortalRequestContext ctx = org.exoplatform.portal.webui.util.Util.getPortalRequestContext();
        ctx.getUIApplication().getUIPopupMessages().addMessage(new ApplicationMessage(message, null));
    }

    @Override
    public void serveResource(ResourceRequest request, ResourceResponse response) throws PortletException, IOException {
        String objectType = request.getParameter(PARAM_OBJECT_TYPE);
        String action = request.getParameter(PARAM_ACTION);

        if(OBJECT_TYPE_COMMENT.equals(objectType)) {
            try {
                serveComment(action, request, response);
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
        } else if(OBJECT_TYPE_TASK.equals(objectType)) {
            try {
                serveTask(action, request, response);
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
        } else {
            super.serveResource(request, response);
        }
    }

    protected void serveTask(String action, ResourceRequest request, ResourceResponse response) throws PortletException, IOException, JSONException {
        String user = request.getRemoteUser();
        PrintWriter out = response.getWriter();

        JSONObject result = new JSONObject();
        result.put("code", 400);
        result.put("message", "Bad Request");
        result.put("data", "");

        if("updateStatus".equals(action)) {
            String taskId = request.getParameter(PARAM_OBJECT_ID);
            String status = request.getParameter("status");

            Task task = service.getTask(taskId);
            task.setStatus(Status.getStatus(Integer.parseInt(status)));

            task = service.updateTask(task);

            JSONObject json = new JSONObject();
            json.put("id", task.getId());
            json.put("title", task.getTitle());
            json.put("created", task.getCreatedDate());
            json.put("assignee", task.getAssignee());
            JSONArray array = new JSONArray();
            for(String l : task.getLabels()) {
                array.put(l);
            }
            json.put("labels", array);
            json.put("modified", task.getModifiedDate());
            json.put("priority", task.getPriority().priority());
            json.put("priorityName", task.getPriority().getLabel());
            json.put("status", task.getStatus().status());
            json.put("statusName", task.getStatus().getLabel());
            json.put("reporter", task.getReporter());

            result.put("code", 200);
            result.put("message", "successfully");
            result.put("data", json);
        }
        if("updatePriority".equals(action)) {
            String taskId = request.getParameter(PARAM_OBJECT_ID);
            String priority = request.getParameter("priority");

            Task task = service.getTask(taskId);
            task.setPriority(Priority.getPriority(Integer.parseInt(priority)));

            task = service.updateTask(task);

            JSONObject json = new JSONObject();
            json.put("id", task.getId());
            json.put("title", task.getTitle());
            json.put("created", task.getCreatedDate());
            json.put("assignee", task.getAssignee());
            JSONArray array = new JSONArray();
            for(String l : task.getLabels()) {
                array.put(l);
            }
            json.put("labels", array);
            json.put("modified", task.getModifiedDate());
            json.put("priority", task.getPriority().priority());
            json.put("priorityName", task.getPriority().getLabel());
            json.put("status", task.getStatus().status());
            json.put("statusName", task.getStatus().getLabel());
            json.put("reporter", task.getReporter());

            result.put("code", 200);
            result.put("message", "successfully");
            result.put("data", json);
        }

        if("assign".equals(action)) {
            String taskId = request.getParameter(PARAM_OBJECT_ID);
            String assingee = request.getParameter("assignee");

            User u = null;
            try {
                u = orgService.getUserHandler().findUserByName(assingee);
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                if (u == null) {
                    result.put("code", 406);
                    result.put("message", "User is not exists");
                    result.put("data", "");
                }
            }

            if(u != null) {
                Task task = service.getTask(taskId);
                task.setAssignee(u.getUserName());

                task = service.updateTask(task);

                JSONObject json = new JSONObject();
                json.put("id", task.getId());
                json.put("title", task.getTitle());
                json.put("created", task.getCreatedDate());
                json.put("assignee", task.getAssignee());
                json.put("assigneeName", u.getFullName());
                JSONArray array = new JSONArray();
                for(String l : task.getLabels()) {
                    array.put(l);
                }
                json.put("labels", array);
                json.put("modified", task.getModifiedDate());
                json.put("priority", task.getPriority().priority());
                json.put("priorityName", task.getPriority().getLabel());
                json.put("status", task.getStatus().status());
                json.put("statusName", task.getStatus().getLabel());
                json.put("reporter", task.getReporter());

                result.put("code", 200);
                result.put("message", "successfully");
                result.put("data", json);
            }
        }

        out.print(result.toString());
    }
    protected void serveComment(String action, ResourceRequest request, ResourceResponse response) throws PortletException, IOException, JSONException {
        String user = request.getRemoteUser();
        PrintWriter out = response.getWriter();

        DateFormat df = new SimpleDateFormat("dd/MMM/yyyy h:mm a");

        JSONObject result = new JSONObject();
        result.put("code", 400);
        result.put("message", "Bad Request");
        result.put("data", "");

        if("get".equals(action)) {
            String taskId = request.getParameter("taskId");
            String offsetParam = request.getParameter("offset");
            String limitParam = request.getParameter("limit");

            int offset = 0;
            int limit = 10;
            try {
                offset = Integer.parseInt(offsetParam);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            try {
                limit = Integer.parseInt(limitParam);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            List<Comment> comments = service.getCommentByTask(taskId, offset, limit);
            //Collections.reverse(comments);

            String nextURL = "";
            if(comments.size() >= limit) {
                ResourceURL url = response.createResourceURL();
                url.setParameter(PARAM_OBJECT_TYPE, OBJECT_TYPE_COMMENT);
                url.setParameter(PARAM_ACTION, "get");
                url.setParameter("taskId", taskId);
                url.setParameter("offset", String.valueOf(offset + limit));
                url.setParameter("limit", String.valueOf(limit));

                nextURL = url.toString();
            }

            JSONObject json = new JSONObject();
            json.put("nextURL", nextURL);

            JSONArray array = new JSONArray();
            for(Comment cmt : comments) {
                JSONObject c = new JSONObject();
                c.put("id", cmt.getId());
                c.put("taskId", cmt.getTaskId());
                c.put("author", cmt.getAuthor());
                c.put("created", df.format(cmt.getCreatedDate()));
                c.put("text", cmt.getText());

                ResourceURL delete = response.createResourceURL();
                delete.setParameter(PARAM_OBJECT_TYPE, OBJECT_TYPE_COMMENT);
                delete.setParameter(PARAM_ACTION, "delete");
                delete.setParameter(PARAM_OBJECT_ID, cmt.getId());

                c.put("deleteURL", delete.toString());

                ResourceURL update = response.createResourceURL();
                update.setParameter(PARAM_OBJECT_TYPE, OBJECT_TYPE_COMMENT);
                update.setParameter(PARAM_ACTION, "update");
                update.setParameter(PARAM_OBJECT_ID, cmt.getId());

                c.put("updateURL", update.toString());

                array.put(c);
            }
            json.put("comments", array);

            result.put("code", 200);
            result.put("message", "OK");
            result.put("data", json);

        } else if ("delete".equals(action)) {
            String commentId = request.getParameter(PARAM_OBJECT_ID);
            service.removeComment(commentId);

            result.put("code", 200);
            result.put("message", "OK");
            result.put("data", "");

        } else if("update".equals(action)) {
            String commentId = request.getParameter("commentId");
            String text = request.getParameter("comment");
            Comment comment = service.getCommentById(commentId);
            comment.setText(text);
            comment = service.updateComment(comment);

            JSONObject json = new JSONObject();
            json.put("id", comment.getId());
            json.put("taskId", comment.getTaskId());
            json.put("author", comment.getAuthor());
            json.put("created", df.format(comment.getCreatedDate()));
            json.put("text", comment.getText());

            ResourceURL delete = response.createResourceURL();
            delete.setParameter(PARAM_OBJECT_TYPE, OBJECT_TYPE_COMMENT);
            delete.setParameter(PARAM_ACTION, "delete");
            delete.setParameter(PARAM_OBJECT_ID, comment.getId());

            json.put("deleteURL", delete.toString());

            ResourceURL update = response.createResourceURL();
            update.setParameter(PARAM_OBJECT_TYPE, OBJECT_TYPE_COMMENT);
            update.setParameter(PARAM_ACTION, "update");
            update.setParameter(PARAM_OBJECT_ID, comment.getId());

            json.put("updateURL", update.toString());

            result.put("code", 200);
            result.put("message", "update successfully");
            result.put("data", json);

        } else if("create".equals(action)) {
            String comment = request.getParameter("comment");
            String taskId = request.getParameter("taskId");
            Task task = service.getTask(taskId);

            Comment c = null;
            if(comment != null && !comment.isEmpty()) {
                Comment cmt = new Comment(user, comment);
                cmt.setTaskId(task.getId());
                c = service.addComment(cmt);
            }

            if(c != null) {
                JSONObject json = new JSONObject();
                json.put("id", c.getId());
                json.put("taskId", c.getTaskId());
                json.put("author", c.getAuthor());
                json.put("created", df.format(c.getCreatedDate()));
                json.put("text", c.getText());

                ResourceURL delete = response.createResourceURL();
                delete.setParameter(PARAM_OBJECT_TYPE, OBJECT_TYPE_COMMENT);
                delete.setParameter(PARAM_ACTION, "delete");
                delete.setParameter(PARAM_OBJECT_ID, c.getId());

                json.put("deleteURL", delete.toString());

                ResourceURL update = response.createResourceURL();
                update.setParameter(PARAM_OBJECT_TYPE, OBJECT_TYPE_COMMENT);
                update.setParameter(PARAM_ACTION, "update");
                update.setParameter(PARAM_OBJECT_ID, c.getId());

                json.put("updateURL", update.toString());

                result.put("code", 201);
                result.put("message", "Created successfully");
                result.put("data", json);
            } else {
                result.put("code", 500);
                result.put("message", "can not add comment");
                result.put("data", "");
            }
        }

        out.println(result.toString());
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
            if (view.equals("dashboard")) {
                render("/dashboard.jsp", request, response);
                return;
            }

            if (view.equals("issues")) {

                String projectId = request.getParameter("projectId");

                Project project = service.getProject(projectId);
                List<Task> tasks = service.getTasksByProject(projectId, 0, -1);
                request.setAttribute("project", project);
                request.setAttribute("tasks", tasks);
                Collections.sort(tasks, new Comparator<Task>() {
                    @Override
                    public int compare(Task o1, Task o2) {
                        return o2.getCreatedDate().compareTo(o1.getCreatedDate());
                    }
                });

                //. Load all user of this project
                Set<String> membershipts = project.getMemberships();
                Map<String, User> users = new HashMap<String, User>();
                try {
                    User u = orgService.getUserHandler().findUserByName(user);
                    users.put(u.getUserName(), u);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                for(String group : membershipts) {
                    try {
                        ListAccess<User> list = orgService.getUserHandler().findUsersByGroupId(group);
                        int size = list.getSize();
                        for(User u : list.load(0, size)) {
                            users.put(u.getUserName(), u);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                request.setAttribute("usersInProject", users);

                render("/issues.jsp", request, response);
                return;
            }

            if(view.equals("detail")) {
                String taskId = request.getParameter("taskId");
                Task task = service.getTask(taskId);
                String projectId = task.getProjectId();
                Project project = service.getProject(projectId);

                //. Load all user of this project
                Set<String> membershipts = project.getMemberships();
                Map<String, User> users = new HashMap<String, User>();
                try {
                    User u = orgService.getUserHandler().findUserByName(user);
                    users.put(u.getUserName(), u);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                for(String group : membershipts) {
                    try {
                        ListAccess<User> list = orgService.getUserHandler().findUsersByGroupId(group);
                        int size = list.getSize();
                        for(User u : list.load(0, size)) {
                            users.put(u.getUserName(), u);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                //. Load comment of task
                List<Comment> comments = service.getCommentByTask(taskId, 0, 10);
                Collections.reverse(comments);

                //. Link for load more comments
                String linkLoadMore = "";
                if(comments.size() >= 10) {
                    ResourceURL moreCommentURL = response.createResourceURL();
                    moreCommentURL.setParameter(PARAM_OBJECT_TYPE, OBJECT_TYPE_COMMENT);
                    moreCommentURL.setParameter(PARAM_ACTION, "get");
                    moreCommentURL.setParameter("taskId", task.getId());
                    moreCommentURL.setParameter("offset", "10");
                    moreCommentURL.setParameter("limit", "10");

                    linkLoadMore = moreCommentURL.toString();
                }

                request.setAttribute("project", project);
                request.setAttribute("task", task);
                request.setAttribute("comments", comments);
                request.setAttribute("moreCommentURL", linkLoadMore);
                request.setAttribute("usersInProject", users);


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

        //. Load all group and membership
        Collection groups = null;
        try {
            groups = orgService.getGroupHandler().getAllGroups();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if(groups == null) {
            groups = Collections.emptyList();
        }
        request.setAttribute("userGroups", groups);

        String action = request.getParameter("action");
        if (action != null) {
            request.setAttribute("_project", service.getProject(request.getParameter("objectId")));
        }
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
        String controller = request.getParameter("objectType");

        if("project".equals(controller)) {
            this.processProject(request, response);
        } else if("task".equals(controller)) {
            this.processTask(request, response);
        } else if("comment".equals(controller)) {
            this.processComment(request, response);
        }
    }

    protected abstract void processProject(ActionRequest request, ActionResponse response);

    protected abstract void processTask(ActionRequest request, ActionResponse response);

    protected abstract void processComment(ActionRequest request, ActionResponse response);
}
