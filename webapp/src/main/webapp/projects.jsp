<%@ page import="javax.portlet.PortletURL" %>
<%@ page import="java.util.List" %>
<%@ page import="org.exoplatform.task.model.Project" %>
<%@ page import="java.util.Collections" %>
<%@include file="includes/header.jsp" %>
<%
    List<Project> projects = null;
    try {
        projects = (List<Project>)renderRequest.getAttribute("projects");
    } catch (Throwable ex) {}

    if(projects == null) {
        projects = Collections.EMPTY_LIST;
    }
%>
<table class="table table-hover">
  <thead>
    <tr>
      <th>Project Name</th>
      <th>Description</th>
      <th>Action</th>
    </tr>
  </thead>
  <tbody>
  <%if(projects.size() == 0) {%>
    <tr>
      <td colspan="3">There are no project of your account, please create one!</td>
    </tr>
  <%} else {
        for(Project project : projects) {
            PortletURL projectURL = renderResponse.createRenderURL();
            projectURL.setParameter("view", "issues");
            projectURL.setParameter("projectId", project.getId());

            PortletURL deleteAction = renderResponse.createActionURL();
            deleteAction.setParameter("objectType", "project");
            deleteAction.setParameter("action", "delete");
            deleteAction.setParameter("objectId", String.valueOf(project.getId()));

            PortletURL editURL = renderResponse.createRenderURL();
            editURL.setParameter("objectType", "project");
            editURL.setParameter("action", "edit");
            editURL.setParameter("objectId", String.valueOf(project.getId()));
            
        %>
    <tr>
      <td><a href="<%=projectURL.toString()%>"><%=project.getName()%></a></td>
      <td><%= project.getDesc()%></td>
      <td>
        <a href="<%=editURL.toString()%>"><i class="icon-pencil"></i></a>
        <a href="<%=deleteAction.toString()%>"><i class="icon-trash"></i></a>
      </td>
    </tr>
        <%}
    }%>
  </tbody>
</table>

<%
Project p = (Project)renderRequest.getAttribute("_project");
if (p != null) {
%>
<div>
    <form action="<portlet:actionURL />" method="POST" class="form-horizontal">
        <fieldset>
            <legend>Edit project</legend>
            <div>
                <input type="hidden" name="objectType" value="project"/>
                <input type="hidden" name="action" value="edit"/>
                <input type="hidden" name="projectId" value="<%=p.getId()%>"/>
            </div>
            <div class="control-group">
                <label class="control-label" for="inputName">Project name</label>
                <div class="controls">
                    <input type="text" name="name" id="inputName" placeholder="Name of project" value="<%=p.getName()%>">
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">Description</label>
                <div class="controls">
                    <textarea name="description" rows="3"><%=p.getDesc()%></textarea>
                </div>
            </div>
            <div class="control-group">
                <div class="controls">
                    <button type="submit" class="btn">Update</button>
                </div>
            </div>
        </fieldset>
    </form>
</div>
<%    
} else {
%>
<div>
    <form action="<portlet:actionURL />" method="POST" class="form-horizontal">
        <fieldset>
            <legend>Create new project</legend>
            <div>
                <input type="hidden" name="objectType" value="project"/>
                <input type="hidden" name="action" value="create"/>
            </div>
            <div class="control-group">
                <label class="control-label" for="inputName">Project name</label>
                <div class="controls">
                    <input type="text" name="name" id="inputName" placeholder="Name of project">
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">Description</label>
                <div class="controls">
                    <textarea name="description" rows="3"></textarea>
                </div>
            </div>
            <div class="control-group">
                <div class="controls">
                    <button type="submit" class="btn">Create</button>
                </div>
            </div>
        </fieldset>
    </form>
</div>
<%}%>