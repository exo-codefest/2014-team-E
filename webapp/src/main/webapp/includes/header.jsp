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

%>
<div class="header">
  <ul class="breadcrumb">
    <li><a href="<%=allProjectURL.toString()%>">Projects</a> <span class="divider">/</span></li>
    <% if (currentProject != null) {
        PortletURL projectURL = renderResponse.createRenderURL();
        projectURL.setParameter("view", "issues");
        projectURL.setParameter("projectId", currentProject.getId());
    %>
    <li><a class="active" href="<%=projectURL.toString()%>"><%=currentProject.getName()%></a>
    <% } %>
  </ul>
</div>