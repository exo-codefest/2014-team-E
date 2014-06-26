<%@ page import="javax.portlet.PortletURL" %>
<%@include file="includes/header.jsp" %>
<div class="media">
    <div class="media-body">
        <%
            PortletURL url = renderResponse.createRenderURL();
            url.setParameter("view", "issues");
            url.setParameter("projectId", "1");
        %>
        <h4 class="media-heading"><a href="<%=url.toString()%>">Project 1</a></h4>
        <div class="media">
            Project description here
        </div>
    </div>
    <div class="media-body">
        <h4 class="media-heading"><a href="<%=url.toString()%>">Project 2</a></h4>
        <div class="media">
            Project description here
        </div>
    </div>
    <div class="media-body">
        <h4 class="media-heading"><a href="<%=url.toString()%>">Project 3</a></h4>
        <div class="media">
            Project description here
        </div>
    </div>
    <div class="media-body">
        <h4 class="media-heading"><a href="<%=url.toString()%>">Project 4</a></h4>
        <div class="media">
            Project description here
        </div>
    </div>
    <div class="media-body">
        <h4 class="media-heading"><a href="<%=url.toString()%>">Project 5</a></h4>
        <div class="media">
            Project description here
        </div>
    </div>
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
                    <input type="text" id="inputName" placeholder="Name of project">
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">Description</label>
                <div class="controls">
                    <textarea rows="3"></textarea>
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