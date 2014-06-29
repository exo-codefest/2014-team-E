<%@page import="org.exoplatform.task.model.Status"%>
<%@ page import="java.util.List" %>
<%@ page import="org.exoplatform.task.model.Task" %>
<%@ page import="java.util.Collections" %>
<%@ page import="org.exoplatform.task.model.Project" %>
<%@ page import="javax.portlet.ResourceURL" %>
<%@ page import="org.exoplatform.codefest.AbstractPortlet" %>
<%@ page import="java.util.Map" %>
<%@ page import="org.exoplatform.services.organization.User" %>
<%@include file="includes/header.jsp" %>
<%
    PortletURL url;
    Project project = (Project)renderRequest.getAttribute("project");
    List<Task> tasks = (List<Task>)renderRequest.getAttribute("tasks");
    if(tasks == null) {
        tasks = Collections.emptyList();
    }

    PortletURL deleteURL = renderResponse.createActionURL();
    deleteURL.setParameter("objectType", "task");
    deleteURL.setParameter("projectId", project.getId());

  Map<String, User> usersInProject = (Map<String, User>)renderRequest.getAttribute("usersInProject");
%>
<div class="uiGrayLightBox clearfix toolbar-table">
    <h5 class="pull-left">Manage Tasks</h5>
    <form class="form-inline pull-right" action="<portlet:actionURL />" method="POST" >
        <fieldset>
            <div>
                <input type="hidden" name="objectType" value="task"/>
                <input type="hidden" name="action" value="create"/>
                <input type="hidden" name="projectId" value="<%=project.getId()%>"/>
            </div>
            <div class="control-group mgB0">
                <div class="controls">
                    <input type="text" id="inputName" name="title" placeholder="Title of task">
                    <button type="submit" class="btn hide">Create</button>
                </div>
            </div>
        </fieldset>
    </form>
</div>
<form action="<%=deleteURL.toString() %>" method="post">
<table class="table table-hover table-task">
  <thead>
    <tr>
      <th><input type="checkbox" id="select_all_tasks"></th>
      <th>Task</th>
      <th class="text-center">Priority</th>
      <th class="text-center">Status</th>
      <th class="text-center w45">Assignee</th>
    </tr>
  </thead>
  <tbody>
  <% if(tasks.size() == 0) {%>
    <tr>
      <td colspan="5">
        <div class="alert alert-info" id="">
            <i class="uiIconInfo"></i> No project is available, please create one!
        </div>
      </td>
    </tr>
  <%} else {
      for(Task task : tasks) {
          PortletURL detailURL = renderResponse.createRenderURL();
          detailURL.setParameter("view", "detail");
          detailURL.setParameter("taskId", task.getId());
          %>
    <tr>
      <td><input type="checkbox" name="objectId" value="<%=task.getId()%>"/></td>
      <td><a href="<%=detailURL.toString()%>" <% if(task.getStatus().equals(Status.RESOLVED) ||  task.getStatus().equals(Status.REFUSED)) {%> class="done"<%}%>><%=task.getTitle()%></a></td>
      <td class="text-center"><a href="<%=detailURL.toString()%>"><%=task.getPriority()%></a></td>
      <td class="text-center">
        <%
          ResourceURL changeStatusURL = renderResponse.createResourceURL();
          changeStatusURL.setParameter(AbstractPortlet.PARAM_OBJECT_TYPE, AbstractPortlet.OBJECT_TYPE_TASK);
          changeStatusURL.setParameter(AbstractPortlet.PARAM_OBJECT_ID, task.getId());
          changeStatusURL.setParameter(AbstractPortlet.PARAM_ACTION, "updateStatus");
        %>
        <div class="btn-group btn-status" url-changeStatus="<%=changeStatusURL%>">
          <button class="btn dropdown-toggle" data-toggle="dropdown"><span class="value"><%=task.getStatus()%></span> <span class="caret"></span></button>
          <ul class="dropdown-menu">
            <%for(Status status : Status.values()){
            %>
            <li><a class="change-status" status="<%=status.status()%>" href="javascript:void(0);"><%=status.name()%></a></li>
            <%}%>
          </ul>
        </div>
      </td>
      <td class="text-center">
        <%--<%=task.getAssignee()%>--%>
        <%
          ResourceURL assignURL = renderResponse.createResourceURL();
          assignURL.setParameter(AbstractPortlet.PARAM_OBJECT_TYPE, AbstractPortlet.OBJECT_TYPE_TASK);
          assignURL.setParameter(AbstractPortlet.PARAM_OBJECT_ID, task.getId());
          assignURL.setParameter(AbstractPortlet.PARAM_ACTION, "assign");

          String assignee = task.getAssignee();
          if(assignee == null || assignee.isEmpty()) {
            assignee = "Unassigned";
          } else if(usersInProject.containsKey(assignee)) {
            User u = usersInProject.get(assignee);
            assignee = u.getFullName();
          }
        %>
        <div class="btn-group btn-assign" url="<%=assignURL%>">
          <button class="btn dropdown-toggle" data-toggle="dropdown"><span class="value"><%=assignee%></span> <span class="caret"></span></button>
          <ul class="dropdown-menu">
            <%for(String username : usersInProject.keySet()){
              User u = usersInProject.get(username);
            %>
            <li><a class="change-assignee" assignee="<%=username%>" href="javascript:void(0);"><%=u.getFullName()%></a></li>
            <%}%>
          </ul>
        </div>
      </td>
    </tr>
        <%}
    }%>
  </tbody>
</table>
<button class="btn btn-primary" type="submit" name="action" value="delete">Delete</button>
</form>
