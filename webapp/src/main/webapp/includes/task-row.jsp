<%@ page import="javax.portlet.ResourceURL" %>
<%@ page import="org.exoplatform.task.model.Task" %>
<%@page import="org.exoplatform.task.model.Status"%>
<%@ page import="org.exoplatform.task.model.Priority" %>
<%@ page import="org.exoplatform.codefest.AbstractPortlet" %>
<%@ page import="org.exoplatform.services.organization.User" %>
<%@ page import="javax.portlet.PortletURL" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.Collections" %>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<portlet:defineObjects/>
<%
	Map<String, User> usersInProject = (Map<String, User>)resourceRequest.getAttribute("usersInProject");
	List<Task> tasks = (List<Task>)resourceRequest.getAttribute("tasks");
	for(Task task : tasks) {
      PortletURL detailURL = resourceResponse.createRenderURL();
      detailURL.setParameter("view", "detail");
      detailURL.setParameter("taskId", task.getId());
      %>
<tr>
  <td><input type="checkbox" name="objectId" value="<%=task.getId()%>"/></td>
  <td class="title"><a href="<%=detailURL.toString()%>" <% if(task.getStatus().equals(Status.RESOLVED) ||  task.getStatus().equals(Status.REFUSED)) {%> class="done"<%}%>><%=task.getTitle()%></a></td>
  <td class="text-center contain-btn">
    <%--<a href="<%=detailURL.toString()%>"><%=task.getPriority()%></a>--%>
    <%
      ResourceURL changePriorityURL = resourceResponse.createResourceURL();
      changePriorityURL.setParameter(AbstractPortlet.PARAM_OBJECT_TYPE, AbstractPortlet.OBJECT_TYPE_TASK);
      changePriorityURL.setParameter(AbstractPortlet.PARAM_OBJECT_ID, task.getId());
      changePriorityURL.setParameter(AbstractPortlet.PARAM_ACTION, "updatePriority");
    %>
    <div class="btn-group btn-priority" url="<%=changePriorityURL%>">
      <button class="btn dropdown-toggle" data-toggle="dropdown"><span class="value"><%=task.getPriority().getLabel()%></span> <span class="caret"></span></button>
      <ul class="dropdown-menu">
        <%for(Priority priority : Priority.values()){
        %>
        <li><a class="change-priority" priority="<%=priority.priority()%>" href="javascript:void(0);"><%=priority.getLabel()%></a></li>
        <%}%>
      </ul>
    </div>
  </td>
  <td class="text-center contain-btn">
    <%
      ResourceURL changeStatusURL = resourceResponse.createResourceURL();
      changeStatusURL.setParameter(AbstractPortlet.PARAM_OBJECT_TYPE, AbstractPortlet.OBJECT_TYPE_TASK);
      changeStatusURL.setParameter(AbstractPortlet.PARAM_OBJECT_ID, task.getId());
      changeStatusURL.setParameter(AbstractPortlet.PARAM_ACTION, "updateStatus");
    %>
    <div class="btn-group btn-status" url-changeStatus="<%=changeStatusURL%>">
      <button class="btn dropdown-toggle" data-toggle="dropdown"><span class="value"><%=task.getStatus().getLabel()%></span> <span class="caret"></span></button>
      <ul class="dropdown-menu">
        <%for(Status status : Status.values()){
        %>
        <li><a class="change-status" status="<%=status.status()%>" href="javascript:void(0);"><%=status.getLabel()%></a></li>
        <%}%>
      </ul>
    </div>
  </td>
  <td class="text-center contain-btn">
    <%--<%=task.getAssignee()%>--%>
    <%
      ResourceURL assignURL = resourceResponse.createResourceURL();
      assignURL.setParameter(AbstractPortlet.PARAM_OBJECT_TYPE, AbstractPortlet.OBJECT_TYPE_TASK);
      assignURL.setParameter(AbstractPortlet.PARAM_OBJECT_ID, task.getId());
      assignURL.setParameter(AbstractPortlet.PARAM_ACTION, "assign");

      String assignee = task.getAssignee();
      if(assignee == null || assignee.isEmpty()) {
        assignee = "Unassigned";
      } else if(usersInProject.containsKey(assignee)) {
        User u = usersInProject.get(assignee);
        assignee = u.getFullName();
      }
    %>
    <div class="btn-group btn-assign" url="<%=assignURL%>">
      <button class="btn dropdown-toggle" data-toggle="dropdown"><span class="value"><%=assignee%></span> <span class="caret"></span></button>
      <ul class="dropdown-menu">
        <%for(String username : usersInProject.keySet()){
          User u = usersInProject.get(username);
        %>
        <li><a class="change-assignee" assignee="<%=username%>" href="javascript:void(0);"><%=u.getFullName()%></a></li>
        <%}%>
      </ul>
    </div>
  </td>
</tr>
<%}%>
<nextURL><%=resourceRequest.getAttribute("nextURL")%>