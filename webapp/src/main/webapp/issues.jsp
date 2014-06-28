<%@ page import="java.util.List" %>
<%@ page import="org.exoplatform.task.model.Task" %>
<%@ page import="java.util.Collections" %>
<%@ page import="org.exoplatform.task.model.Project" %>
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
%>
<form action="<%=deleteURL.toString() %>" method="post">
<table class="table table-hover">
  <thead>
    <tr>
      <th>#</th>
      <th>Task</th>
      <th>Status</th>
      <th>Priority</th>
      <th>Assignee</th>
    </tr>
  </thead>
  <tbody>
  <% if(tasks.size() == 0) {%>
    <tr>
      <td colspan="5">There are no project of your account, please create one!</td>
    </tr>
  <%} else {
      for(Task task : tasks) {
          PortletURL detailURL = renderResponse.createRenderURL();
          detailURL.setParameter("view", "detail");
          detailURL.setParameter("taskId", task.getId());

          //
          deleteURL.setParameter("action", "delete");
          deleteURL.setParameter("objectId", task.getId());
          %>
    <tr>
      <td><input type="checkbox" name="objectId" value="<%=task.getId()%>"/></td>
      <td><a href="<%=detailURL.toString()%>"><%=task.getTitle()%></a></td>
      <td><a href="<%=detailURL.toString()%>"><%=task.getStatus()%></a></td>
      <td><a href="<%=detailURL.toString()%>"><%=task.getPriority()%></a></td>
      <td><a href="<%=deleteURL.toString()%>"><%=(task.getAssignee() == null ? "Unassigned" : task.getAssignee())%></a></td>
    </tr>
        <%}
    }%>
  </tbody>
</table>
<button class="btn btn-primary" type="submit" name="action" value="delete">Delete</button>
</form>

<div>
    <form action="<portlet:actionURL />" method="POST" class="form-horizontal">
        <fieldset>
            <legend>Add new Task</legend>
            <div>
                <input type="hidden" name="objectType" value="task"/>
                <input type="hidden" name="action" value="create"/>
                <input type="hidden" name="projectId" value="<%=project.getId()%>"/>
            </div>
            <div class="control-group">
                <div class="controls">
                    <input type="text" id="inputName" name="title" placeholder="Title of task">
                    <button type="submit" class="btn">Create</button>
                </div>
            </div>
        </fieldset>
    </form>
</div>