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
  <a class="btn btn-primary" href="<%=allProjectURL.toString()%>">Projects</a>
  <% if (currentProject != null) { out.print(" > " + currentProject.getName()); } %>
</div>