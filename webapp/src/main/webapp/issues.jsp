<%@include file="includes/header.jsp" %>
<%
    PortletURL url;
%>
<form action="#" method="POST">
<div class="">
    <input class="btn" type="submit" name="action" value="Delete">
</div>
<div class="media">
    <div class="media-body">
        <%
            url = renderResponse.createRenderURL();
            url.setParameter("view", "detail");
            url.setParameter("issueId", "1");
        %>
        <div class="pull-left">
            <input type="checkbox">
        </div>
        <h4 class="media-heading"><a href="<%=url.toString()%>">Issue 1</a></h4>
        <div class="media">
            Some info about issue 1
        </div>
    </div>
    <div class="media-body">
        <%
            url = renderResponse.createRenderURL();
            url.setParameter("view", "detail");
            url.setParameter("issueId", "1");
        %>
        <div class="pull-left">
            <input type="checkbox">
        </div>
        <h4 class="media-heading"><a href="<%=url.toString()%>">Issue 1</a></h4>
        <div class="media">
            Some info about issue 1
        </div>
    </div>
    <div class="media-body">
        <%
            url = renderResponse.createRenderURL();
            url.setParameter("view", "detail");
            url.setParameter("issueId", "1");
        %>
        <div class="pull-left">
            <input type="checkbox">
        </div>
        <h4 class="media-heading"><a href="<%=url.toString()%>">Issue 1</a></h4>
        <div class="media">
            Some info about issue 1
        </div>
    </div>
    <div class="media-body">
        <%
            url = renderResponse.createRenderURL();
            url.setParameter("view", "detail");
            url.setParameter("issueId", "1");
        %>
        <div class="pull-left">
            <input type="checkbox">
        </div>
        <h4 class="media-heading"><a href="<%=url.toString()%>">Issue 1</a></h4>
        <div class="media">
            Some info about issue 1
        </div>
    </div>
    <div class="media-body">
        <%
            url = renderResponse.createRenderURL();
            url.setParameter("view", "detail");
            url.setParameter("issueId", "1");
        %>
        <div class="pull-left">
            <input type="checkbox">
        </div>
        <h4 class="media-heading"><a href="<%=url.toString()%>">Issue 1</a></h4>
        <div class="media">
            Some info about issue 1
        </div>
    </div>
    <div class="media-body">
        <%
            url = renderResponse.createRenderURL();
            url.setParameter("view", "detail");
            url.setParameter("issueId", "1");
        %>
        <div class="pull-left">
            <input type="checkbox">
        </div>
        <h4 class="media-heading"><a href="<%=url.toString()%>">Issue 1</a></h4>
        <div class="media">
            Some info about issue 1
        </div>
    </div>
    <div class="media-body">
        <%
            url = renderResponse.createRenderURL();
            url.setParameter("view", "detail");
            url.setParameter("issueId", "1");
        %>
        <div class="pull-left">
            <input type="checkbox">
        </div>
        <h4 class="media-heading"><a href="<%=url.toString()%>">Issue 1</a></h4>
        <div class="media">
            Some info about issue 1
        </div>
    </div>
    <div class="media-body">
        <%
            url = renderResponse.createRenderURL();
            url.setParameter("view", "detail");
            url.setParameter("issueId", "1");
        %>
        <div class="pull-left">
            <input type="checkbox">
        </div>
        <h4 class="media-heading"><a href="<%=url.toString()%>">Issue 1</a></h4>
        <div class="media">
            Some info about issue 1
        </div>
    </div>
</div>
</form>
<div>
    <form class="form-horizontal">
        <fieldset>
            <legend>Add new Task</legend>
            <div class="control-group">
                <div class="controls">
                    <input type="text" id="inputName" placeholder="Title of task">
                    <button type="submit" class="btn">Create</button>
                </div>
            </div>
            <div class="control-group">
                <div class="controls">

                </div>
            </div>
        </fieldset>
    </form>
</div>