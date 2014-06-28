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

import org.exoplatform.task.TaskServiceException;
import org.exoplatform.task.model.Comment;
import org.exoplatform.task.model.Project;
import org.exoplatform.task.model.Status;
import org.exoplatform.task.model.Task;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

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
            //Project p = new Project(user, request.getParameter("name"), request.getParameter("description"));
            //p.setId(projectId);
            try {
                service.updateProject(p);
            } catch (TaskServiceException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        if("share".equals(action)) {
            String projectId = request.getParameter("projectId");
            String groupId = request.getParameter("group");
            String membershipType = request.getParameter("membershipType");

            String membership = groupId + ":" + membershipType;
            Project project = service.getProject(projectId);
            Set<String> memberships = new HashSet<String>();
            memberships.addAll(project.getMemberships());
            if(memberships == null) {
                memberships = new HashSet<String>();
            }
            memberships.add(membership);
            project.setMemberships(memberships);
            try {
                service.updateProject(project);
            } catch (TaskServiceException ex) {
                ex.printStackTrace();
            }
        }

        if("unshare".equals(action)) {
            String projectId = request.getParameter("projectId");
            String membership = request.getParameter("membership");

            Project project = service.getProject(projectId);
            Set<String> memberships = new HashSet<String>();
            memberships.addAll(project.getMemberships());
            memberships.remove(membership);
            project.setMemberships(memberships);
            try {
                service.updateProject(project);
            } catch (TaskServiceException ex) {
                ex.printStackTrace();
            }
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
            Task task = new Task(projectId, title);
            task.setStatus(Status.OPEN.toString());
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

        if("assign".equals(action)) {
            String taskId = request.getParameter("objectId");
            String assignee = request.getParameter("assignee");
            Task task = service.getTask(taskId);

            response.setRenderParameter("view", "detail");
            response.setRenderParameter("taskId", taskId);

            List<String> assignees = task.getAssignee();
            if(assignees == null) {
                assignees = new LinkedList<String>();
            }
            if(assignees.contains(assignee)) {
                return;
            } else {
                assignees.add(assignee);
                task.setAssignee(assignees);
                service.updateTask(task);
            }
        }

        if("unassign".equals(action)) {
            String taskId = request.getParameter("objectId");
            String assignee = request.getParameter("assignee");
            Task task = service.getTask(taskId);

            response.setRenderParameter("view", "detail");
            response.setRenderParameter("taskId", taskId);

            List<String> assignees = task.getAssignee();
            if(assignees == null) {
                assignees = new LinkedList<String>();
            }
            if(!assignees.contains(assignee)) {
                return;
            } else {
                assignees.remove(assignee);
                task.setAssignee(assignees);
                service.updateTask(task);
            }
        }

        if("update-title".equals(action)) {
            String taskId = request.getParameter("objectId");
            String title = request.getParameter("title");

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
                List<Comment> comments = task.getComments();
                if(comments == null) {
                    comments = new LinkedList<Comment>();
                }
                comments.add(cmt);
                task.setComments(comments);

                service.updateTask(task);
            }
        } else if("delete".equals(action)) {
            //TODO: process delete comment by service
            String commentId = request.getParameter("commentId");
            List<Comment> comments = task.getComments();
            if(comments == null) {
                comments = new LinkedList<Comment>();
            }

            Comment delete = null;
            for(Comment comment : comments) {
                if(comment.getId().equals(commentId)) {
                    delete = comment;
                    break;
                }
            }
            comments.remove(delete);

            task.setComments(comments);

            service.updateTask(task);
        } else if("update".equals(action)) {
            //TODO: process update comment by service
            String commentId = request.getParameter("commentId");
            String text = request.getParameter("comment");
            List<Comment> comments = task.getComments();
            if(comments == null) {
                comments = new LinkedList<Comment>();
            }

            for(Comment comment : comments) {
                if(comment.getId().equals(commentId)) {
                    comment.setText(text);
                    break;
                }
            }
            task.setComments(comments);
            service.updateTask(task);
        }        
    }
}
