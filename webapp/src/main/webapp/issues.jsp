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
%>
<table class="table table-hover">
  <thead>
    <tr>
      <th>#</th>
      <th>Task</th>
      <th>Action</th>
    </tr>
  </thead>
  <tbody>
  <% if(tasks.size() == 0) {%>
    <tr>
      <td colspan="3">There are no project of your account, please create one!</td>
    </tr>
  <%} else {
      for(Task task : tasks) {
          PortletURL detailURL = renderResponse.createRenderURL();
          detailURL.setParameter("view", "detail");
          detailURL.setParameter("taskId", task.getId());

          PortletURL deleteURL = renderResponse.createActionURL();
          deleteURL.setParameter("objectType", "task");
          deleteURL.setParameter("action", "delete");
          deleteURL.setParameter("projectId", project.getId());
          deleteURL.setParameter("objectId", task.getId());
          %>
    <tr>
      <td><input type="checkbox" name="taskId" value="<%=task.getId()%>"/></td>
      <td><a href="<%=detailURL.toString()%>"><%=task.getTitle()%></a></td>
      <td><a href="<%=deleteURL.toString()%>"><i class="icon-trash"></i></a></td>
    </tr>
        <%}
    }%>
  </tbody>
</table>

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