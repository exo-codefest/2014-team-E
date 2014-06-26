<%@ page import="javax.portlet.PortletURL" %>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<portlet:defineObjects/>
<div class="row-fluid header">
    <div class="navbar">
        <div class="navbar-inner">
            <ul class="nav">
                <%
                PortletURL projectsViewURL = renderResponse.createRenderURL();
                projectsViewURL.setParameter("view", "projects");
                %>
                <li class="active"><a href="<%=projectsViewURL.toString()%>">Project</a></li>
            </ul>
        </div>
    </div>
</div>