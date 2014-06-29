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

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import java.util.HashSet;
import java.util.Set;

import org.exoplatform.task.model.Comment;
import org.exoplatform.task.model.Priority;
import org.exoplatform.task.model.Project;
import org.exoplatform.task.model.Status;
import org.exoplatform.task.model.Task;

/**
 * @author <a href="trongtt@gmail.com">Trong Tran</a>
 * @version $Revision$
 */
// TODO: Rename the portlet class to MasterTask
public class GitMaster extends AbstractPortlet {

    @Override
    protected void processProject(ActionRequest request, ActionResponse response) {
        response.setRenderParameter("view", "projects");
        String action = request.getParameter("action");

        String user = request.getRemoteUser();
        if(user == null) {
            pushNotification("You need to log-in before performing any action");
            return;
        }

        if("create".equals(action)) {
            String name = request.getParameter("name");
            String description = request.getParameter("description");
            String memberships = request.getParameter("memberships");

            Project project = new Project(user, name, description);
            Set<String> set = new HashSet<String>();
            if(memberships != null && memberships.length() > 0) {
                String[] arr = memberships.split(",");
                for(String s : arr) {
                    set.add(s);
                }
            }
            project.setMemberships(set);
            try {
                service.createProject(project);
            } catch (IllegalArgumentException ex) {
                pushNotification("You need to enter valid information");
            }
            return;
        }

        if("delete".equals(action)) {
            String id = request.getParameter("objectId");
            //String projectId = String.format("%s/%s", user, id);
            service.removeProject(id);
        }

        if ("edit".equals(action)) {
            String projectId = request.getParameter("projectId");
            Project p = service.getProject(projectId);
            p.setName(request.getParameter("name"));
            p.setDesc(request.getParameter("description"));
            String memberships = request.getParameter("memberships");
            Set<String> set = new HashSet<String>();
            if(memberships != null && memberships.length() > 0) {
                String[] arr = memberships.split(",");
                for(String s : arr) {
                    set.add(s);
                }
            }
            p.setMemberships(set);
            service.updateProject(p);
        }

        if("share".equals(action)) {
            String projectId = request.getParameter("projectId");
            String groupId = request.getParameter("group");
            String membershipType = request.getParameter("membershipType");

            String membership = membershipType + ":" + groupId;
            Project project = service.getProject(projectId);
            
            Set<String> memberships = new HashSet<String>();
            memberships.addAll(project.getMemberships());
            memberships.add(membership);
            project.setMemberships(memberships);
            service.updateProject(project);
        }

        if("unshare".equals(action)) {
            String projectId = request.getParameter("projectId");
            String membership = request.getParameter("membership");

            Project project = service.getProject(projectId);
            Set<String> memberships = new HashSet<String>();
            memberships.addAll(project.getMemberships());
            memberships.remove(membership);
            project.setMemberships(memberships);
            service.updateProject(project);
        }
    }

    @Override
    protected void processTask(ActionRequest request, ActionResponse response) {
        String action = request.getParameter("action");
        String projectId = request.getParameter("projectId");

        response.setRenderParameter("view", "issues");
        if(projectId != null) {
            response.setRenderParameter("projectId", projectId);
        }

        String user = request.getRemoteUser();
        if(user == null) {
            return;
        }

        if("create".equals(action)) {
            String title = request.getParameter("title");
            if(title == null || title.trim().isEmpty()) {
                return;
            }
            Task task = new Task(projectId, title);
            task.setStatus(Status.OPEN);
            task.setPriority(Priority.UNDEFINED);
            task.setReporter(user);
            task.setCreatedDate(System.currentTimeMillis());
            service.addTask(task);

            response.setRenderParameter("view", "issues");

            return;
        }

        if("delete".equals(action)) {
            String[] arr =  request.getParameterValues("objectId");
            if (arr != null) {
                for (String taskId : arr) {
                    service.removeTask(taskId);
                }
            }
            return;
        }

        if("update".equals(action)) {
            String taskId = request.getParameter("objectId");
            response.setRenderParameter("view", "detail");
            response.setRenderParameter("taskId", taskId);

            String title = request.getParameter("title");
            if(title == null || title.trim().isEmpty()) {
                this.pushNotification("Task title can NOT be empty");
                return;
            }

            String priority = request.getParameter("priority");
            String status = request.getParameter("status");
            String assignee = request.getParameter("assignee");
            String labels = request.getParameter("labels");

            Task task = service.getTask(taskId);
            task.setTitle(title);
            task.setAssignee(assignee);
            Set<String> set = new HashSet<String>();
            for(String s : labels.split(",")) {
                set.add(s);
            }
            task.setLabels(set);
            task.setPriority(Priority.getPriority(Integer.parseInt(priority)));
            task.setStatus(Status.getStatus(Integer.parseInt(status)));

            service.updateTask(task);
        }

        if("assign".equals(action)) {
            String taskId = request.getParameter("objectId");
            String assignee = request.getParameter("assignee");
            Task task = service.getTask(taskId);

            response.setRenderParameter("view", "detail");
            response.setRenderParameter("taskId", taskId);

            task.setAssignee(assignee);
            service.updateTask(task);
        }

        if("unassign".equals(action)) {
            String taskId = request.getParameter("objectId");
            String assignee = request.getParameter("assignee");
            Task task = service.getTask(taskId);

            response.setRenderParameter("view", "detail");
            response.setRenderParameter("taskId", taskId);

            task.setAssignee(assignee);
            service.updateTask(task);
        }

        if("update-title".equals(action)) {
            String taskId = request.getParameter("objectId");
            String title = request.getParameter("title");
            if(title == null || title.trim().isEmpty()) {
                this.pushNotification("Task title can NOT be empty");
                return;
            }

            response.setRenderParameter("view", "detail");
            response.setRenderParameter("taskId", taskId);

            Task task = service.getTask(taskId);
            task.setTitle(title);
            service.updateTask(task);
        }
    }

    @Override
    protected void processComment(ActionRequest request, ActionResponse response) {
        String taskId = request.getParameter("taskId");
        Task task = service.getTask(taskId);
        String action = request.getParameter("action");

        response.setRenderParameter("view", "detail");
        response.setRenderParameter("taskId", taskId);

        String user = request.getRemoteUser();
        if(user == null) {
            return;
        }

        if("create".equals(action)) {
            String comment = request.getParameter("comment");
            if(comment != null && !comment.isEmpty()) {
                Comment cmt = new Comment(user, comment);
                cmt.setTaskId(taskId);
                service.addComment(cmt);
            }
        } else if("delete".equals(action)) {
            String commentId = request.getParameter("commentId");
            service.removeComment(commentId);
        } else if("update".equals(action)) {
            String commentId = request.getParameter("commentId");
            String text = request.getParameter("comment");
            
            Comment comment = service.getCommentById(commentId);
            if (comment != null) {
                comment.setText(text);
                service.updateComment(comment);
            }
        }        
    }
}
