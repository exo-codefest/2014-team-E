<%@ page import="javax.portlet.PortletURL" %>
<%@ page import="org.exoplatform.task.model.Project" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Collections" %>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<portlet:defineObjects/>
<%
    Project currentProject = (Project)renderRequest.getAttribute("project");
    List<Project> recentProjects = (List<Project>)renderRequest.getAttribute("recentProjects");
    if(recentProjects == null) {
        recentProjects = Collections.emptyList();
    }
%>
<div class="row-fluid header">
    <div class="uiGrayLightBox clearfix">
        <ul class="nav nav-pills">
            <li class="dropdown">
                <a class="dropdown-toggle" id="project" role="button" data-toggle="dropdown" href="#"><%=(currentProject == null ? "All projects" : currentProject.getName())%> <b class="caret"></b></a>
                <ul id="menu1" class="dropdown-menu" role="menu" aria-labelledby="drop4">
                    <%if(recentProjects.size() > 0) {
                        for(Project project : recentProjects) {
                            PortletURL projectURL = renderResponse.createRenderURL();
                            projectURL.setParameter("view", "issues");
                            projectURL.setParameter("projectId", project.getId());
                        %>
                            <li role="presentation"><a role="menuitem" tabindex="-1" href="<%=projectURL.toString()%>"><%=project.getName()%></a></li>
                        <%}%>
                            <li role="presentation" class="divider"></li>
                    <%}%>
                    <li role="presentation">
                        <%
                            PortletURL allProjectURL = renderResponse.createRenderURL();
                            allProjectURL.setParameter("view", "projects");
                        %>
                        <a role="menuitem" tabindex="-1" href="<%=allProjectURL.toString()%>">View All project</a>
                    </li>
                </ul>
            </li>
        </ul>
    </div>
</div>