<%@ page import="org.exoplatform.task.model.Task" %>
<%@ page import="org.exoplatform.task.model.Comment" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.Set" %>
<%@ page import="java.util.LinkedList" %>
<%@ page import="java.util.HashSet" %>
<%@ page import="java.util.Map" %>
<%@ page import="org.exoplatform.services.organization.User" %>
<%@ page import="org.exoplatform.task.model.Status" %>
<%@include file="includes/header.jsp" %>
<%
  Project project = (Project) renderRequest.getAttribute("project");
  Set<String> membership = new HashSet<String>();
  membership.addAll(project.getMemberships());
  membership.add(project.getOwner());

  Task task = (Task) renderRequest.getAttribute("task");
  List<Comment> comments = null;
  if (task != null) {
    comments = task.getComments();
  }
  if (comments == null) {
    comments = Collections.emptyList();
  }

  Map<String, User> usersInProject = (Map<String, User>)renderRequest.getAttribute("usersInProject");

  DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
%>
<div class="container-fluid detail-view">
  <div class="row-fluid task-edit" style="display: none">
    <form id="form-edit-task" action="<portlet:actionURL />" method="POST" class="form-horizontal">
      <div>
        <input type="hidden" name="objectType" value="task"/>
        <input type="hidden" name="action" value="update"/>
        <input type="hidden" name="objectId" value="<%=task.getId()%>"/>
      </div>
      <div class="control-group">
        <label class="control-label" for="inputTitle">Title</label>
        <div class="controls">
          <input type="text" name="title" id="inputTitle" placeholder="Task title" value="<%=task.getTitle()%>">
        </div>
      </div>
      <div class="control-group">
        <label class="control-label" for="inputStatus">Status:</label>

        <div class="controls">
          <select name="status" id="inputStatus">
            <% for(Status status : Status.values()) {%>
              <option value="<%=status.name()%>" <%=(status.name().equals(task.getStatus()) ? "selected = \"selected\"" : "")%>><%=status.name()%></option>
            <%}%>
          </select>
        </div>
      </div>
      <div class="control-group">
        <label class="control-label" for="inputAssignee">Assignee:</label>

        <div class="controls">
          <select id="inputAssignee" name="assignee">
            <option value="">Select user</option>
            <%for(String username : usersInProject.keySet()) {
              User user = usersInProject.get(username);
            %>
              <option value="<%=username%>"><%=user.getFullName()%></option>
            <%}%>
          </select>
        </div>
      </div>
      <div class="control-group control-labels">
        <label class="control-label" for="inputLabel">Label</label>

        <div class="controls">
          <div class="list-labels">
            <%
              Set<String> labels = task.getLabels();
              StringBuilder labelVals = new StringBuilder();
              if(labels == null || labels.size() == 0) {%>

              <%} else {
                for(String label : labels) {
                  if(labelVals.length() > 0) {
                    labelVals.append(',');
                  }
                  labelVals.append(label);
                %>
                  <span class="task-label"><span class="badge"><%=label%></span> <a class="close" href="javascript:void(0);">&times;</a></span>
                <%}
              }
            %>
          </div>
          <input type="hidden" name="labels" value="<%=labelVals%>"/>
          <input type="text" name="label" id="inputLabel" placeholder="Input then click add for add new label" value="" class="span6">
          <button type="button" class="btn" name="add-label">Add</button>
        </div>
      </div>
      <div class="control-group">
        <div class="controls">
          <button type="submit" class="btn btn-primary">Update</button>
          <button type="reset" name="cancel-edit" class="btn">Cancel</button>
        </div>
      </div>
    </form>
  </div>
  <div class="row-fluid task-detail">
    <div class="span10">
      <h2><%=task.getTitle()%></h2>
    </div>
    <div class="span2">
      <a class="uiActionWithLabel pull-right edit" href="javascript:void(0);"><i class="uiIconEditMini uiIconLightGray"></i>Edit</a>
    </div>
    <div class="span12">
      <span class="label label-info"><%=task.getStatus()%></span>
      <span><strong><%=(task.getReporter() == null ? "guest" : task.getReporter())%></strong> created this project at <%=task.getCreatedDate()%></span>
    </div>
    <div class="span12">
      <div class="span4">Assignee: <%=(task.getAssignee() == null ? "none" : task.getReporter())%></div>
      <div class="span8">
        <%if(task.getLabels() != null && task.getLabels().size() > 0){%>
          <span>Label: </span>
          <%for(String label : task.getLabels()) {%>
            <span class="badge"><%=label%></span>
          <%}%>
        <%}%>
      </div>
    </div>
  </div>
  <div class="row-fluid comments">
    <div class="span12">
      <ul>
        <%for (Comment comment : comments) {%>
        <li>
          <%
            PortletURL deleteCommentURL = renderResponse.createActionURL();
            deleteCommentURL.setParameter("objectType", "comment");
            deleteCommentURL.setParameter("action", "delete");
            deleteCommentURL.setParameter("taskId", task.getId());
            deleteCommentURL.setParameter("commentId", comment.getId());
          %>
          <div class="comment">
            <div class="header">
              <%=(comment.getAuthor() == null ? "anonymous" : comment.getAuthor())%> comment
              at <%=(df.format(comment.getCreatedDate() == null ? new Date() : comment.getCreatedDate()))%>
              <a class="action" action="edit" href="javascript:void(0);"><i class="icon-pencil"></i></a>
              <a href="<%=deleteCommentURL.toString()%>"><i class="icon-trash"></i></a>
            </div>
            <div class="body"><%=comment.getText()%></div>
            <div class="edit" style="display: none">
              <form action="<portlet:actionURL />" method="POST" class="">
                <div>
                  <input type="hidden" name="objectType" value="comment"/>
                  <input type="hidden" name="action" value="update"/>
                  <input type="hidden" name="taskId" value="<%=task.getId()%>"/>
                  <input type="hidden" name="commentId" value="<%=comment.getId()%>"/>
                </div>
                <div class="control-group">
                  <div class="controls">
                    <textarea name="comment" rows="2"><%=comment.getText()%>
                    </textarea>
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
          <form action="<portlet:actionURL />" method="POST" class="">
            <div>
              <input type="hidden" name="objectType" value="comment"/>
              <input type="hidden" name="action" value="create"/>
              <input type="hidden" name="taskId" value="<%=task.getId()%>"/>
            </div>
            <div class="control-group">
              <div class="controls">
                <textarea class = "textarea" name="comment" rows="3" class="span12"></textarea>
              </div>
            </div>
            <div class="control-group">
              <div class="controls">
                <button type="submit" class="btn btn-primary">Comment</button>
              </div>
            </div>
          </form>
        </li>
      </ul>
    </div>
  </div>
</div>