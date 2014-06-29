<%@ page import="javax.portlet.ResourceURL" %>
<%@page import="org.exoplatform.task.model.Status"%>
<%@ page import="org.exoplatform.task.model.Priority" %>
<%@ page import="org.exoplatform.codefest.AbstractPortlet" %>
<%@ page import="javax.portlet.PortletURL" %>
<%@ page import="org.exoplatform.task.model.Project" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Collections" %>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<portlet:defineObjects/>
<%
    Project currentProject = (Project)renderRequest.getAttribute("project");
    List<Project> recentProjects = (List<Project>)renderRequest.getAttribute("recentProjects");
    String currentView = (String)renderRequest.getParameter("view");
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
  <h4 class="header-app"><a class="pull-right" href="<%=dashboard.toString()%>" title="My Dashboard"><i class="icon-th icon-white"></i></a>MasterTask</h4>
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
  <%
   if (currentView == null || "projects".equals(currentView)) {
  %>
  	<%--<input type="text" class="filterProject" placeholder="Filter by Project Name"></input>--%>
  <%
  	} else if (currentView.equals("issues")) {
  	 Project project = (Project)renderRequest.getAttribute("project");
  	 ResourceURL filterTaskURL = renderResponse.createResourceURL();
  	 filterTaskURL.setParameter(AbstractPortlet.PARAM_OBJECT_TYPE, AbstractPortlet.OBJECT_TYPE_TASK_LIST);
  	 filterTaskURL.setParameter("projectId", project.getId());
  %>
  <div class="filterTask contain-btn" filterURL="<%=filterTaskURL%>">
   <div class="btn-group btn-priority filterTaskPriority">
     <button class="btn dropdown-toggle" data-toggle="dropdown"><span class="value">All Priority</span> <span class="caret"></span></button>
     <ul class="dropdown-menu">
       <li><a class="filterPriority" href="javascript:void(0);">All Priority</a></li>
       <%for(Priority priority : Priority.values()){
       %>
       <li><a class="filterPriority" eXoValue="<%=priority.priority()%>" href="javascript:void(0);"><%=priority.getLabel()%></a></li>
       <%}%>
     </ul>
   </div>
   <div class="btn-group btn-status filterTaskStatus">
     <button class="btn dropdown-toggle" data-toggle="dropdown"><span class="value">All Status</span> <span class="caret"></span></button>
     <ul class="dropdown-menu">
       <li><a class="filterStatus" href="javascript:void(0);">All Status</a></li>
       <%for(Status status : Status.values()){
       %>
       <li><a class="filterStatus" eXoValue="<%=status.status()%>" href="javascript:void(0);"><%=status.getLabel()%></a></li>
       <%}%>
     </ul>
   </div>
  	<input type="text" class="filterTaskTitle" placeholder="Task title"></input>
  </div>
  <%
  	}
  %>
</div>