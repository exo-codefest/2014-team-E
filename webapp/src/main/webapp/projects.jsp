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
<div class="media">
    <%if(projects.size() == 0) {%>
    <div class="media-body">
        There are no project of your account, please create one!
    </div>
    <%} else {
        for(Project project : projects) {
            PortletURL projectURL = renderResponse.createRenderURL();
            projectURL.setParameter("view", "issues");
            projectURL.setParameter("projectId", project.getId());

            PortletURL deleteAction = renderResponse.createActionURL();
            deleteAction.setParameter("objectType", "project");
            deleteAction.setParameter("action", "delete");
            deleteAction.setParameter("objectId", String.valueOf(project.getId()));
        %>
            <div class="media-body">
                <h4 class="media-heading">
                    <a href="<%=projectURL.toString()%>"><%=project.getName()%></a>
                    <a href="javascript:void(0);"><i class="icon-pencil"></i></a>
                    <a href="<%=deleteAction.toString()%>"><i class="icon-trash"></i></a>
                </h4>
                <div class="media">
                    <%= project.getDesc()%>
                </div>
            </div>
        <%}
    }%>
</div>
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