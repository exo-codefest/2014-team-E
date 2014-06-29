<%@page import="org.exoplatform.task.model.Status"%>
<%@ page import="java.util.List" %>
<%@ page import="org.exoplatform.task.model.Task" %>
<%@ page import="java.util.Collections" %>
<%@ page import="org.exoplatform.task.model.Project" %>
<%@ page import="javax.portlet.ResourceURL" %>
<%@ page import="org.exoplatform.codefest.AbstractPortlet" %>
<%@ page import="java.util.Map" %>
<%@ page import="org.exoplatform.services.organization.User" %>
<%@ page import="org.exoplatform.task.model.Priority" %>
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
<div>
  <%
    ResourceURL filterTaskURL = renderResponse.createResourceURL();
    filterTaskURL.setParameter(AbstractPortlet.PARAM_OBJECT_TYPE, AbstractPortlet.OBJECT_TYPE_TASK_LIST);
    filterTaskURL.setParameter("projectId", project.getId());
  %>
  <div class="filterTask contain-btn text-right" filterURL="<%=filterTaskURL%>">
    <div class="inline-block">
      <div class="btn-group btn-priority filterTaskPriority">
        <button class="btn dropdown-toggle" data-toggle="dropdown"><span class="value">All Priority</span> <span class="caret"></span></button>
        <ul class="dropdown-menu text-left">
          <li><a class="filterPriority" href="javascript:void(0);">All Priority</a></li>
          <%for(Priority priority : Priority.values()){
          %>
          <li><a class="filterPriority" eXoValue="<%=priority.priority()%>" href="javascript:void(0);"><%=priority.getLabel()%></a></li>
          <%}%>
        </ul>
      </div>
      <div class="btn-group btn-status filterTaskStatus">
        <button class="btn dropdown-toggle" data-toggle="dropdown"><span class="value">All Status</span> <span class="caret"></span></button>
        <ul class="dropdown-menu text-left">
          <li><a class="filterStatus" href="javascript:void(0);">All Status</a></li>
          <%for(Status status : Status.values()){
          %>
          <li><a class="filterStatus" eXoValue="<%=status.status()%>" href="javascript:void(0);"><%=status.getLabel()%></a></li>
          <%}%>
        </ul>
      </div>
    </div>
    <div class="inline-block relative-block">
      <input type="text" class="filterTaskTitle span4" placeholder="Task title"/>
      <a class="search-icon" href="javascript:void(0);"><i class="icon-search"></i></a>
    </div>
  </div>
</div>
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
                    <input type="text" id="inputName" name="title" placeholder="Quick add new task here">
                    <button type="submit" class="btn hide">Create</button>
                </div>
            </div>
        </fieldset>
    </form>
</div>
<%if(tasks.size() == 0){%>
<div class="alert alert-info" id="">
  <i class="uiIconInfo"></i> No task is available, please create one!
</div>
<%} else {%>
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
  <% for(Task task : tasks) {
          PortletURL detailURL = renderResponse.createRenderURL();
          detailURL.setParameter("view", "detail");
          detailURL.setParameter("taskId", task.getId());
          %>
    <tr>
      <td><input type="checkbox" name="objectId" value="<%=task.getId()%>"/></td>
      <td class="title"><a href="<%=detailURL.toString()%>" <% if(task.getStatus().equals(Status.RESOLVED) ||  task.getStatus().equals(Status.REFUSED)) {%> class="done"<%}%>><%=task.getTitle()%></a></td>
      <td class="text-center contain-btn">
        <%--<a href="<%=detailURL.toString()%>"><%=task.getPriority()%></a>--%>
        <%
          ResourceURL changePriorityURL = renderResponse.createResourceURL();
          changePriorityURL.setParameter(AbstractPortlet.PARAM_OBJECT_TYPE, AbstractPortlet.OBJECT_TYPE_TASK);
          changePriorityURL.setParameter(AbstractPortlet.PARAM_OBJECT_ID, task.getId());
          changePriorityURL.setParameter(AbstractPortlet.PARAM_ACTION, "updatePriority");
        %>
        <div class="btn-group btn-priority text-left" url="<%=changePriorityURL%>">
          <button class="btn dropdown-toggle" data-toggle="dropdown"><span class="value"><%=task.getPriority().getLabel()%></span> <span class="caret"></span></button>
          <ul class="dropdown-menu text-left">
            <%for(Priority priority : Priority.values()){
            %>
            <li><a class="change-priority" priority="<%=priority.priority()%>" href="javascript:void(0);"><%=priority.getLabel()%></a></li>
            <%}%>
          </ul>
        </div>
      </td>
      <td class="text-center contain-btn">
        <%
          ResourceURL changeStatusURL = renderResponse.createResourceURL();
          changeStatusURL.setParameter(AbstractPortlet.PARAM_OBJECT_TYPE, AbstractPortlet.OBJECT_TYPE_TASK);
          changeStatusURL.setParameter(AbstractPortlet.PARAM_OBJECT_ID, task.getId());
          changeStatusURL.setParameter(AbstractPortlet.PARAM_ACTION, "updateStatus");
        %>
        <div class="btn-group btn-status text-left" url-changeStatus="<%=changeStatusURL%>">
          <button class="btn dropdown-toggle" data-toggle="dropdown"><span class="value"><%=task.getStatus().getLabel()%></span> <span class="caret"></span></button>
          <ul class="dropdown-menu text-left">
            <%for(Status status : Status.values()){
            %>
            <li><a class="change-status" status="<%=status.status()%>" href="javascript:void(0);"><%=status.getLabel()%></a></li>
            <%}%>
          </ul>
        </div>
      </td>
      <td class="text-center contain-btn">
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
        <div class="btn-group btn-assign text-left" url="<%=assignURL%>">
          <button class="btn dropdown-toggle" data-toggle="dropdown"><span class="value"><%=assignee%></span> <span class="caret"></span></button>
          <ul class="dropdown-menu text-left">
            <%for(String username : usersInProject.keySet()){
              User u = usersInProject.get(username);
            %>
            <li><a class="change-assignee" assignee="<%=username%>" href="javascript:void(0);"><%=u.getFullName()%></a></li>
            <%}%>
          </ul>
        </div>
      </td>
    </tr>
  <%}%>
  </tbody>
</table>
<%
	ResourceURL nextURL = (ResourceURL)renderRequest.getAttribute("nextURL");
	if (nextURL != null) {
%>
<a class="load-more-task" href="javascript:void(0);" nextURL="<%=nextURL%>"><span>Load more</span></a>
<%} %>    
<button class="btn btn-primary" type="submit" name="action" value="delete">Delete</button>
</form>
<%}%>