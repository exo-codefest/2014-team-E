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
<div class="media">
    <% if(tasks.size() == 0) {%>
      <div class="media-body">
          <div>There is no issue in this project. Please create one.</div>
      </div>
    <%} else {
        for(Task task : tasks) {
            PortletURL detailURL = renderResponse.createRenderURL();
            detailURL.setParameter("view", "detail");
            detailURL.setParameter("taskId", "taskID");

            PortletURL deleteURL = renderResponse.createActionURL();
            deleteURL.setParameter("objectType", "task");
            deleteURL.setParameter("action", "delete");
            deleteURL.setParameter("projectId", project.getId());
            deleteURL.setParameter("objectId", task.getId());
            %>
            <div class="media-body">
                <div class="pull-left">
                    <input type="checkbox" name="taskId" value="<%=task.getId()%>"/>
                </div>
                <h4 class="media-heading">
                    <a href="<%=detailURL.toString()%>"><%=task.getTitle()%></a>
                    <a href="javascript:void(0);"><i class="icon-pencil"></i></a>
                    <a href="<%=deleteURL.toString()%>"><i class="icon-trash"></i></a>
                </h4>
            </div>
            <%
        }
    }%>
</div>
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