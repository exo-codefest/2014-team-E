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
<%
    PortletURL allProjectURL = renderResponse.createRenderURL();
    allProjectURL.setParameter("view", "projects");

    PortletURL dashboard = renderResponse.createRenderURL();
    dashboard.setParameter("view", "dashboard");
%>
<div class="header">
  <h4 class="header-app"><a class="pull-right" href="<%=dashboard.toString()%>"><i class="icon-th icon-white"></i></a>MasterTask</h4>
  <ul class="breadcrumb">
    <li><a href="<%=allProjectURL.toString()%>">Projects</a> <span class="divider"><i class="uiIconMiniArrowRight"></i></span></li>
    <% if (currentProject != null) {
        PortletURL projectURL = renderResponse.createRenderURL();
        projectURL.setParameter("view", "issues");
        projectURL.setParameter("projectId", currentProject.getId());
    %>
    <li><a class="active" href="<%=projectURL.toString()%>"><%=currentProject.getName()%></a>
    <% } %>
  </ul>
</div>