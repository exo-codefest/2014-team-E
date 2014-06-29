<%@page import="org.exoplatform.task.model.Status"%>
<%@ page import="java.util.List" %>
<%@ page import="org.exoplatform.task.model.Task" %>
<%@ page import="java.util.Collections" %>
<%@ page import="org.exoplatform.task.model.Project" %>
<%@ page import="javax.portlet.ResourceURL" %>
<%@ page import="org.exoplatform.codefest.AbstractPortlet" %>
<%@ page import="java.util.Map" %>
<%@ page import="org.exoplatform.services.organization.User" %>
<%@include file="includes/header.jsp" %>
<%
List<Task> tasks = (List<Task>)renderRequest.getAttribute("open");
%>
<div class="dashboard">
  <div class="column" eXoStatus="1">
    <div class="state">OPEN</div>
    <div class="sortable">
      <% for(Task t : tasks) {
          ResourceURL changeStatusURL = renderResponse.createResourceURL();
          changeStatusURL.setParameter(AbstractPortlet.PARAM_OBJECT_TYPE, AbstractPortlet.OBJECT_TYPE_TASK);
          changeStatusURL.setParameter(AbstractPortlet.PARAM_OBJECT_ID, t.getId());
          changeStatusURL.setParameter(AbstractPortlet.PARAM_ACTION, "updateStatus");
        
      %>
      <div class="task draggable" statusURL="<%=changeStatusURL%>"><%=t.getTitle() %></div>
      <%} %>
    </div>
  </div>
  <div class="column" eXoStatus="2">
    <div class="state">IN PROGRESS</div>
    <div class="sortable">
      <% tasks = (List<Task>)renderRequest.getAttribute("inprogress"); %>
      <% for(Task t : tasks) {
          ResourceURL changeStatusURL = renderResponse.createResourceURL();
          changeStatusURL.setParameter(AbstractPortlet.PARAM_OBJECT_TYPE, AbstractPortlet.OBJECT_TYPE_TASK);
          changeStatusURL.setParameter(AbstractPortlet.PARAM_OBJECT_ID, t.getId());
          changeStatusURL.setParameter(AbstractPortlet.PARAM_ACTION, "updateStatus");
      %>
      <div class="task draggable" statusURL="<%=changeStatusURL%>"><%=t.getTitle() %></div>
      <%} %>
    </div>
  </div>
  <div class="column" eXoStatus="4">
    <div class="state">WON'T FIX</div>
    <div class="sortable done">
      <% tasks = (List<Task>)renderRequest.getAttribute("refused"); %>
      <% for(Task t : tasks) {
          ResourceURL changeStatusURL = renderResponse.createResourceURL();
          changeStatusURL.setParameter(AbstractPortlet.PARAM_OBJECT_TYPE, AbstractPortlet.OBJECT_TYPE_TASK);
          changeStatusURL.setParameter(AbstractPortlet.PARAM_OBJECT_ID, t.getId());
          changeStatusURL.setParameter(AbstractPortlet.PARAM_ACTION, "updateStatus");
      %>
      <div class="task draggable" statusURL="<%=changeStatusURL%>"><%=t.getTitle() %></div>
      <%} %>
    </div>
  </div>
  <div class="column" eXoStatus="3">
    <div class="state">RESOLVED</div>
    <div class="sortable done">
      <% tasks = (List<Task>)renderRequest.getAttribute("resolved"); %>
      <% for(Task t : tasks) {
          ResourceURL changeStatusURL = renderResponse.createResourceURL();
          changeStatusURL.setParameter(AbstractPortlet.PARAM_OBJECT_TYPE, AbstractPortlet.OBJECT_TYPE_TASK);
          changeStatusURL.setParameter(AbstractPortlet.PARAM_OBJECT_ID, t.getId());
          changeStatusURL.setParameter(AbstractPortlet.PARAM_ACTION, "updateStatus");
      %>
      <div class="task draggable" statusURL="<%=changeStatusURL%>"><%=t.getTitle() %></div>
      <%} %>
    </div>
  </div>
</div>