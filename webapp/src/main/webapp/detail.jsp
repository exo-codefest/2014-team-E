<%@ page import="org.exoplatform.task.model.Task" %>
<%@ page import="org.exoplatform.task.model.Comment" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.Set" %>
<%@ page import="java.util.LinkedList" %>
<%@ page import="java.util.HashSet" %>
<%@include file="includes/header.jsp" %>
<%
    Project project = (Project)renderRequest.getAttribute("project");
    Set<String> membership = project.getMemberships();
    if(membership == null) {
        membership = new HashSet<String>();
    }
    membership.add(project.getOwner());

    Task task = (Task)renderRequest.getAttribute("task");
    List<Comment> comments = null;
    if(task != null) {
        comments = task.getComments();
    }
    if(comments == null) {
        comments = Collections.emptyList();
    }

    List<String> assignees = task.getAssignee();
    if(assignees == null) {
        assignees = Collections.emptyList();
    }

    DateFormat df = new SimpleDateFormat("dd/MM/YYYY HH:mm");
%>
<div>
    <h2><%=task.getTitle()%></h2>
    <div>
        <div class="assignee">
            <span>Assignee: </span>
            <%if(assignees.size() == 0){ %>
            <%--<span class="label">none</span>--%>
            <%} else {
                for(String assignee : assignees) {
                    PortletURL unassignURL = renderResponse.createActionURL();
                    unassignURL.setParameter("objectType", "task");
                    unassignURL.setParameter("action", "unassign");
                    unassignURL.setParameter("objectId", task.getId());
                    unassignURL.setParameter("assignee", assignee);
                %>
                    <span class="label"><%=assignee%><a class="close" href="<%=unassignURL.toString()%>">&times;</a></span>
                <%}
            }%>
            <div class="btn-group">
                <a class="btn dropdown-toggle" data-toggle="dropdown" href="#">
                    <i class="icon-plus-sign"></i>
                </a>
                <ul class="dropdown-menu">
                    <%for(String member : membership){%>
                        <li>
                            <%
                                PortletURL assignURL = renderResponse.createActionURL();
                                assignURL.setParameter("objectType", "task");
                                assignURL.setParameter("action", "assign");
                                assignURL.setParameter("objectId", task.getId());
                                assignURL.setParameter("assignee", member);
                            %>
                            <a href="<%=assignURL.toString()%>"><%=member%></a>
                        </li>
                    <%}%>
                </ul>
            </div>
        </div>
        <div>
            <span>Label: </span>
            <%if(task.getLabels() != null && task.getLabels().size() > 0) {%>
                <ul>
                    <%for(String label : task.getLabels()) {%>
                        <li><%=label%></li>
                    <%}%>
                </ul>
            <%} else {%>
                <span>none</span>
            <%}%>
        </div>
    </div>

    <ul>
        <%for(Comment comment : comments) {%>
        <li>
            <%
                PortletURL deleteCommentURL =renderResponse.createActionURL();
                deleteCommentURL.setParameter("objectType", "comment");
                deleteCommentURL.setParameter("action", "delete");
                deleteCommentURL.setParameter("taskId", task.getId());
                deleteCommentURL.setParameter("commentId", comment.getId());
            %>
            <div class="comment">
                <div class="header">
                    <%=(comment.getAuthor() == null ? "anonymous" : comment.getAuthor())%> comment at <%=(df.format(comment.getCreatedDate() == null ? new Date() : comment.getCreatedDate()) )%>
                    <a class="action" action="edit" href="javascript:void(0);"><i class="icon-pencil"></i></a>
                    <a href="<%=deleteCommentURL.toString()%>"><i class="icon-trash"></i></a>
                </div>
                <div class="body"><%=comment.getText()%></div>
                <div class="edit" style="display: none">
                    <form action="<portlet:actionURL />" method="POST" class="form-horizontal">
                        <div>
                            <input type="hidden" name="objectType" value="comment"/>
                            <input type="hidden" name="action" value="update"/>
                            <input type="hidden" name="taskId" value="<%=task.getId()%>"/>
                            <input type="hidden" name="commentId" value="<%=comment.getId()%>"/>
                        </div>
                        <div class="control-group">
                            <div class="controls">
                                <textarea name="comment" rows="3"><%=comment.getText()%></textarea>
                            </div>
                        </div>
                        <div class="control-group">
                            <div class="controls">
                                <button type="submit" class="btn">Update</button>
                                <button type="reset" class="btn">Cancel</button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </li>
        <%}%>
        <li>
            <form action="<portlet:actionURL />" method="POST" class="form-horizontal">
                <div>
                    <input type="hidden" name="objectType" value="comment"/>
                    <input type="hidden" name="action" value="create"/>
                    <input type="hidden" name="taskId" value="<%=task.getId()%>"/>
                </div>
                <div class="control-group">
                    <div class="controls">
                        <textarea name="comment" rows="3"></textarea>
                    </div>
                </div>
                <div class="control-group">
                    <div class="controls">
                        <button type="submit" class="btn">Comment</button>
                    </div>
                </div>
            </form>
        </li>
    </ul>
</div>