<%@ page import="javax.portlet.PortletURL" %>
<%@ page import="java.util.List" %>
<%@ page import="org.exoplatform.task.model.Project" %>
<%@ page import="java.util.Collections" %>
<%@ page import="java.util.Collection" %>
<%@ page import="org.exoplatform.services.organization.Group" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="org.exoplatform.services.organization.MembershipType" %>
<%@include file="includes/header.jsp" %>
<%
  List<Project> projects = null;
  try {
      projects = (List<Project>)renderRequest.getAttribute("projects");
  } catch (Throwable ex) {}

  if(projects == null) {
      projects = Collections.EMPTY_LIST;
  }
  Collection groups = (Collection)renderRequest.getAttribute("userGroups");
  if(groups == null) {
    groups = Collections.emptyList();
  }
%>
<input type="text" class="filterProject"></input>
<div class="uiTabPane uiTabNormal">
  <ul class="nav nav-tabs" id="myTab">
    <li class="active"><a data-toggle="tab" href="#myprojects">My Projects</a></li>
    <li><a data-toggle="tab" href="#sharedprojects">Shared Projects</a></li>
  </ul>
  <div class="tab-content" id="myTabContent">	 
    <!-- Begin My Projects List -->
    <div id="myprojects" class="tab-pane fade active in">      
      <table class="table table-hover table-project">
        <thead>
          <tr>
            <th>Project Name</th>
            <th>Description</th>
            <th>Members</th>
            <th>Action</th>
          </tr>
        </thead>
        <tbody>
        <%
        	  boolean hasProject = false;
           for(Project project : projects) {
               if (project.getOwner().equals(renderRequest.getRemoteUser())) {
               hasProject = true;
               PortletURL projectURL = renderResponse.createRenderURL();
               projectURL.setParameter("view", "issues");
               projectURL.setParameter("projectId", project.getId());
   
               PortletURL deleteAction = renderResponse.createActionURL();
               deleteAction.setParameter("objectType", "project");
               deleteAction.setParameter("action", "delete");
               deleteAction.setParameter("objectId", String.valueOf(project.getId()));
   
               PortletURL editURL = renderResponse.createRenderURL();
               editURL.setParameter("objectType", "project");
               editURL.setParameter("action", "edit");
               editURL.setParameter("objectId", String.valueOf(project.getId()));
               
           %>
          <tr class='project'>
            <td><a href="<%=projectURL.toString()%>" class='projectName'><%=project.getName()%></a></td>
            <td class='projectDesc'><%= project.getDesc()%></td>
            <td class="memberships">
              <%for(String membership : project.getMemberships()) {
                  PortletURL unshareURL = renderResponse.createActionURL();
                  unshareURL.setParameter("objectType", "project");
                  unshareURL.setParameter("action", "unshare");
                  unshareURL.setParameter("projectId", project.getId());
                  unshareURL.setParameter("membership", membership);
                %>
                  <span class="italic"><%=membership%></span>
                  <%--<span class="label label-success "><%=membership%> <a class="close" href="<%=unshareURL.toString()%>">&times;</a></span>--%>
                  <br/>
              <%}%>
            </td>
            <td>
              <a href="<%=editURL.toString()%>"><i class="icon-pencil"></i></a>
              <a href="<%=deleteAction.toString()%>"><i class="icon-trash"></i></a>
            </td>
          </tr>
              <%}
          }
          if(!hasProject) {%>
	          <tr>
	            <td colspan="5">No project is available, please create one!!</td>
	          </tr>
	       <%}%>
        </tbody>
      </table>
    </div>
    <!-- End My Projects List -->

    <!-- Begin Shared Projects List -->
    <div id="sharedprojects" class="tab-pane fade">
      <table class="table table-hover table-project">
        <thead>
          <tr>
            <th>Project Name</th>
            <th>Description</th>
            <th>Owner</th>
            <th>Members</th>
          </tr>
        </thead>
        <tbody>
        <%
            boolean hasShared = false;
            for(Project project : projects) {
                if (!project.getOwner().equals(renderRequest.getRemoteUser())) {
                hasShared = true;
                PortletURL projectURL = renderResponse.createRenderURL();
                projectURL.setParameter("view", "issues");
                projectURL.setParameter("projectId", project.getId());
    
                PortletURL deleteAction = renderResponse.createActionURL();
                deleteAction.setParameter("objectType", "project");
                deleteAction.setParameter("action", "delete");
                deleteAction.setParameter("objectId", String.valueOf(project.getId()));
    
                PortletURL editURL = renderResponse.createRenderURL();
                editURL.setParameter("objectType", "project");
                editURL.setParameter("action", "edit");
                editURL.setParameter("objectId", String.valueOf(project.getId()));
                
            %>
          <tr class='project'>
            <td><a href="<%=projectURL.toString()%>" class='projectName'><%=project.getName()%></a></td>
            <td class='projectDesc'><%= project.getDesc()%></td>
            <td><%=project.getOwner()%></td>
            <td class="memberships">
              <%for(String membership : project.getMemberships()) {
                  PortletURL unshareURL = renderResponse.createActionURL();
                  unshareURL.setParameter("objectType", "project");
                  unshareURL.setParameter("action", "unshare");
                  unshareURL.setParameter("projectId", project.getId());
                  unshareURL.setParameter("membership", membership);
                %>
                  <span class="italic"><%=membership%></span>
                  <%--<span class="label label-success"><%=membership%> <a class="close" href="<%=unshareURL.toString()%>">&times;</a></span>--%>
                  <br/>
              <%}%>
            </td>
          </tr>
              <%}
          }
          if(!hasShared) {%>
	          <tr>
	            <td colspan="4">There are no project shared for you!</td>
	          </tr>
        	 <%}%>
        </tbody>
      </table>
    </div>
    <!-- End Shared Projects List -->
  </div>
</div>
<script type="text/javascript">
  require(["SHARED/jquery", "SHARED/bts_tab"], function($) {
    $('#myTab a').click(function (e) {
    e.preventDefault();
    $(this).tab('show');
    })
  });
</script>

<%
Project p = (Project)renderRequest.getAttribute("_project");
if (p != null) {
%>
<div>
    <form id="form-edit-project" action="<portlet:actionURL />" method="POST" class="form-horizontal">
        <fieldset>
            <legend>Edit project</legend>
            <div>
                <input type="hidden" name="objectType" value="project"/>
                <input type="hidden" name="action" value="edit"/>
                <input type="hidden" name="projectId" value="<%=p.getId()%>"/>
            </div>
            <div class="control-group">
                <label class="control-label" for="inputName">Project name</label>
                <div class="controls">
                    <input type="text" name="name" id="inputName" placeholder="Name of project" value="<%=p.getName()%>">
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">Description</label>
                <div class="controls">
                    <textarea name="description" rows="3"><%=p.getDesc()%></textarea>
                </div>
            </div>
            <div class="control-group">
              <div class="control-label">
                Shared Groups:
              </div>
              <div class="controls">
                <div class="list-memberships">
                  <%
                    StringBuilder memberships = new StringBuilder();
                  %>
                  <%for(String membership : p.getMemberships()) {
                      if(memberships.length() > 0) {
                        memberships.append(',');
                      }
                      memberships.append(membership);
                  %>
                  <span class="membership"><span class="badge"><%=membership%></span> <a class="close" href="javascript:void(0);">&times;</a></span>
                  <%}%>
                </div>
                <input type="hidden" name="memberships" value="<%=memberships.toString()%>"/>
                <select class="span3" name="group">
                  <option value="">Select group</option>
                  <%
                    Iterator it = groups.iterator();
                    while(it.hasNext()) {
                      Group g = (Group)it.next();%>
                  <option value="<%=g.getId()%>"><%=g.getId()%></option>
                  <%}
                  %>
                </select>
                <button class="btn" type="button" name="add-membership">Add</button>
              </div>
            </div>
            <div class="control-group">
                <div class="controls">
                    <button type="submit" class="btn btn-primary">Update</button>
                    <%
                      PortletURL cancelURL = renderResponse.createRenderURL();
                      allProjectURL.setParameter("view", "projects");
                    %>
                    <a class="btn" href="<%=cancelURL.toString()%>">Cancel</a>
                </div>
            </div>
        </fieldset>
    </form>
</div>
<%    
} else {
%>
<div class='uiAction'>
	<button type="button" class="btn btn-primary createProject">Create new project</button>
</div>
<div class="formCreateProject">
    <form id="form-create-project" action="<portlet:actionURL />" method="POST" class="form-horizontal">
        <fieldset>
            <legend>Create new project</legend>
            <div>
                <input type="hidden" name="objectType" value="project"/>
                <input type="hidden" name="action" value="create"/>
            </div>
            <div class="control-group">
                <label class="control-label" for="inputName">Project name</label>
                <div class="controls">
                    <input type="text" name="name" id="inputName" placeholder="Name of project">
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">Description</label>
                <div class="controls">
                    <textarea name="description" rows="3"></textarea>
                </div>
            </div>
            <div class="control-group">
              <div class="control-label">
                Shared Groups:
              </div>
              <div class="controls">
                <div class="list-memberships">

                </div>
                <input type="hidden" name="memberships" value=""/>
                <select class="span3" name="group">
                  <option value="">Select group</option>
                  <%
                    Iterator itg = groups.iterator();
                    while(itg.hasNext()) {
                      Group g = (Group)itg.next();%>
                  <option value="<%=g.getId()%>"><%=g.getId()%></option>
                  <%}
                  %>
                </select>
                <button class="btn" type="button" name="add-membership">Add</button>
              </div>
            </div>
            <div class="control-group">
                <div class="controls">
                    <button type="submit" class="btn btn-primary">Create</button>
                    <%
                      PortletURL cancelURL = renderResponse.createRenderURL();
                      allProjectURL.setParameter("view", "projects");
                    %>
                    <button type="button" class="btn" onclick="window.location=<%=cancelURL.toString()%>">Cancel</button>
                </div>
            </div>
        </fieldset>
    </form>
</div>
<%}%>