(function($){
  $('.createProject').click(function() {
	  var formCreate = $('.formCreateProject');
	  formCreate.fadeIn(1000);
	  $(this).hide();
  });
  
  $('.filterProject').keypress(function(e) {
	 var search = null;
	 var jFilterInput = $(this);
	 if (e.keyCode() >= 32 && e.keyCode() <= 127) {
		 search = window.setTimeout(700, function() {
			 jFilterInput.
		 });
	 } 
  });
  
  $('.comments').on('click', 'a.action', function(e) {
    var $action = $(e.target || e.srcElement).closest('a');
    var $comment = $action.closest(".comment");
    var action = $action.attr('action');
    if(action == "edit") {
      var $comments = $comment.closest('div.comments');
      $comments.find('.edit').hide();
      $comment.find(".body").hide();
      $comment.find('.edit').show();
    }
    return false;
  });

  $('.comments').on('click', 'button[type="reset"]', function(e){
    var $action = $(e.target || e.srcElement);
    var $comment = $action.closest(".comment");
    $comment.find(".body").show();
    $comment.find('.edit').hide();
  });

  //. Task title
  $('.task-title').on('click', 'a.action', function(e) {
    var $action = $(e.target || e.srcElement).closest('a');
    var $title = $action.closest('.task-title');
    var action = $action.attr('action');
    if(action == 'update-title') {
      $title.find('h2').hide();
      $title.find('form').show();
    }
  });
  $('.task-title').on('click', 'button[type="reset"]', function(e){
    var $title = $(e.target || e.srcElement).closest(".task-title");
    $title.find('h2').show();
    $title.find('form').hide();
  });

  //. Add membership
  $('td.memberships').on('click', 'a.action', function(e){
    var $action = $(e.target || e.srcElement).closest('a');
    var action = $action.attr('action');
    if(action == "add") {
      var $popup = $('#UIPopupAddMembership');
      var windowWidth = $(window).width();
      var windowHeight = $(window).height();
      var popupWidth = $popup.width();
      var popupHeight = $popup.height();

      var left = (windowWidth - popupWidth) / 2;
      var top = (windowHeight - popupHeight) / 2;

      $popup.css("left", left + "px");
      $popup.css("top", top + "px");

      $popup.find('input[name="projectId"]').val($action.attr('projectId'));
      $popup.show();
    }
  });
  $('#UIPopupAddMembership').on('click', 'button[type="reset"]', function(e){
    var $popup = $('#UIPopupAddMembership');
    $popup.hide();
  });

  $('#form-edit-project, #form-create-project').on('click', 'button[name="add-membership"]', function(e) {
    var $form = $(e.target).closest('form');
    var $memberships = $form.find('input[name="memberships"]');
    var memberships = $memberships.val();
    var arrayMemberships = memberships.split(',');

    var $group = $form.find('select[name="group"]');
    var group = $group.val();

    if(group == '') {
      return;
    }

    for(var i = 0; i < arrayMemberships.length; i++) {
      var m = arrayMemberships[i];
      if(m == group) return;
    }

    if(memberships.length > 0) {
      memberships += ',';
    }
    memberships += group;
    var $label = $('<span class="membership"><span class="badge">'+group+'</span><a class="close" href="javascript:void(0);">&times;</a></span>');
    $label.appendTo($form.find('div.list-memberships'));
    $memberships.val(memberships);
  });

  $('#form-edit-project, #form-create-project').on('click', 'a.close', function(e) {
    var $label = $(e.target).closest('span.membership');
    var membership = $label.find('span').html();

    var $form = $label.closest('form');
    var $memberships = $form.find('input[name="memberships"]');
    var memberships = $memberships.val();
    var arrayMemberships = membership == '' ? new Array() : memberships.split(',');

    var newArrayMemberships = new Array();
    for(var i = 0; i < arrayMemberships.length; i++) {
      var m = arrayMemberships[i];
      if(m != membership) {
        newArrayMemberships.push(m);
      }
    }

    memberships = newArrayMemberships.join(',');
    $memberships.val(memberships);
    $label.remove();
  });

  $('#form-edit-task').on('keydown', 'input[name="label"]', function(e) {
    console.log(e);
    if(e.which == 13 || e.keyCode == 13) {
      e.preventDefault();
      $(e.target || e.srcElement).closest('form').find('button[name="add-label"]').click();
      return false;
    }
  });
  $('#form-edit-task').on('click', 'button[name="add-label"]', function(e){
    var $control = $(e.target).closest('div.control-labels');
    var $input = $control.find('input[name="label"]');
    var $labels = $control.find('input[name="labels"]');
    var $listLabels = $control.find('div.list-labels');
    var labels = $.trim($labels.val());
    var arrayLabels = labels == '' ? new Array() : labels.split(',');

    var newLabels = $input.val();
    if(newLabels == '') {
      return;
    }
    var newArrayLabels = newLabels.split(',');

    for(var i = 0 ; i < newArrayLabels.length; i++) {
      var l = newArrayLabels[i];
      if(-1 == $.inArray(l, arrayLabels)) {
        arrayLabels.push(l);
        $('<span class="task-label"><span class="badge">'+l+'</span> <a class="close" href="javascript:void(0);">&times;</a></span>').appendTo($listLabels);
      }
    }

    $input.val('');
    $labels.val(arrayLabels.join(','));
  });
  $('#form-edit-task').on('click', 'span.task-label a.close', function(e){
    var $label = $(e.target).closest('span.task-label');
    var $control = $label.closest('div.control-labels');
    var $labels = $control.find('input[name="labels"]');
    var labels = $labels.val();
    var arrayLabels = labels.split(',');
    var newArrayLabels = new Array();
    var label = $.trim($label.find('span.badge').html());

    for(var i = 0 ; i < arrayLabels.length; i++) {
      if(label != arrayLabels[i]) {
        newArrayLabels.push(arrayLabels[i])
      }
    }

    $labels.val(newArrayLabels.join(','));
    $label.remove();
  });
  $('div.detail-view').on('click', 'a.edit', function(e) {
    var $view = $(e.target).closest('div.detail-view');
    var $form = $view.find('div.task-edit');
    var $detail = $view.find('div.task-detail');
    var $comments = $view.find('div.comments');

    $comments.find('div.task-edit').hide();
    $form.show();
    $detail.hide();
    $comments.hide();
  });

  $('div.detail-view').on('click', 'button[name="cancel-edit"]', function(e) {
    var $view = $(e.target).closest('div.detail-view');
    var $form = $view.find('div.task-edit');
    var $detail = $view.find('div.task-detail');
    var $comments = $view.find('div.comments');

    $form.hide();
    $detail.show();
    $comments.show();
  });

  $('div.comments').on('click', 'a.load-more-comment', function(e) {
    var $a = $(e.target || e.srcElement).closest('a');
    var $comments = $a.closest('div.comments');
    var $li = $a.closest('li');
    var $ul = $li.closest('ul');
    var $template = $li.find('#comment_template');

    var url = $a.attr('url');
    $.ajax({
      url: url,
      method: 'GET',
      data: {},
      dataType: 'json',
      success: function(response) {
        if(response.code != 200) {
          return;
        }
        var data = response.data;
        if(data.nextURL) {
          $a.attr('url', data.nextURL);
        } else {
          $a.remove();
        }
        var comments = data.comments;
        $.each(comments, function(index, comment) {
          var $html = $template.clone();
          var html = $html.html();
          html = html.replace('%AUTHOR%', comment.author);
          html = html.replace('%COMMENT_TIME%', comment.created);
          html = html.replace('%DELETE_URL%', comment.deleteURL);
          html = html.replace('%COMMENT_TEXT%', comment.text);
          html = html.replace('%TASKID%', comment.taskId);
          html = html.replace('%COMMENT_ID%', comment.id);
          html = html.replace('%COMMENT_TEXT%', comment.text);
          html = html.replace('%UPDATE_URL%', comment.updateURL);
          html = "<li>" + html + "</li>";

          $(html).insertAfter($li);
        })
      }
    });

    return false;
  });

  $('div.comments').on('click', 'a.delete-comment', function(e) {
    var $a = $(e.target || e.srcElement).closest('a');
    var $li = $a.closest('li');
    var url = $a.attr('url');
    $.ajax({
      url: url,
      method: 'GET',
      data: {},
      dataType: 'json',
      success: function(response) {
        if(response.code == 200) {
          $li.remove();
        }
      }
    });
  });

  $('#form-add-comment').on('submit', function(e){
    e.preventDefault();
    var $form = $(e.target || e.srcElement);
    var $textarea = $form.find('textarea');
    var $taskId = $form.find('input[name="taskId"]')


    var url = $form.attr('action');
    var method = $form.attr('method');
    var taskId = $taskId.val();
    var text = $.trim($textarea.val());
    if(text == '') {
      return false;
    }
    var data = {
      objectType: 'comment',
      action: 'create',
      taskId: taskId,
      comment: text
    };
    $.ajax({
      url: url,
      method: method,
      data: data,
      dataType: 'json',
      success: function(response) {
        if(response.code != 201) {
          return;
        }
        var comment = response.data;

        var $li = $form.closest('li');
        var $comments = $form.closest('div.comments');
        var $template = $comments.find('div#comment_template');
        var $html = $template.clone();

        var html = $html.html();
        html = html.replace('%AUTHOR%', comment.author);
        html = html.replace('%COMMENT_TIME%', comment.created);
        html = html.replace('%DELETE_URL%', comment.deleteURL);
        html = html.replace('%COMMENT_TEXT%', comment.text);
        html = html.replace('%TASKID%', comment.taskId);
        html = html.replace('%COMMENT_ID%', comment.id);
        html = html.replace('%COMMENT_TEXT%', comment.text);
        html = html.replace('%UPDATE_URL%', comment.updateURL);
        html = "<li>" + html + "</li>";

        $(html).insertBefore($li);
        $textarea.val('');
      }
    });


    return false;
  });

  $('div.detail-view').on('submit', 'form.form-update-comment', function(e){
    e.preventDefault();
    var $form = $(e.target || e.srcElement);
    var $textarea = $form.find('textarea');
    var $taskId = $form.find('input[name="taskId"]')
    var $commentId = $form.find('input[name="commentId"]');


    var url = $form.attr('action');
    var method = $form.attr('method');
    var taskId = $taskId.val();
    var commentId = $commentId.val();
    var text = $.trim($textarea.val());
    if(text == '') {
      return false;
    }
    var data = {
      objectType: 'comment',
      action: 'update',
      taskId: taskId,
      commentId: commentId,
      comment: text
    };
    $.ajax({
      url: url,
      method: method,
      data: data,
      dataType: 'json',
      success: function(response) {
        if(response.code != 200) {
          return;
        }
        var comment = response.data;

        var $li = $form.closest('li');
        var $body = $li.find('div.body');
        var $edit = $li.find('div.edit');
        $edit.hide();
        $body.show();
        $body.html(comment.text);
        $textarea.val(comment.text);

      }
    });

    return false;
  });

  $('div.GitMasterPortlet').on('click', 'a.change-status', function(e){
    var $a = $(e.target).closest('a');
    var $btn = $a.closest('.btn-status');

    var url = $btn.attr('url-changeStatus');
    var status = $a.attr('status');

    $.ajax({
      url: url,
      method: 'GET',
      data: {
        status: status
      },
      dataType: 'json',
      success: function(response) {
        if(response.code != 200) {
          return;
        }

        var task = response.data;
        $btn.find('button[data-toggle="dropdown"] > span.value').html(task.statusName);
      }
    });

    return true;
  });

  $('div.GitMasterPortlet').on('click', 'a.change-assignee', function(e){
    var $a = $(e.target).closest('a');
    var $btn = $a.closest('.btn-assign');

    var url = $btn.attr('url');
    var assignee = $a.attr('assignee');

    $.ajax({
      url: url,
      method: 'GET',
      data: {
        assignee: assignee
      },
      dataType: 'json',
      success: function(response) {
        if(response.code != 200) {
          return;
        }

        var task = response.data;
        $btn.find('button[data-toggle="dropdown"] > span.value').html(task.assigneeName);
      }
    });

    return true;
  });

  $('#select_all_tasks').change(function() {
    var checkboxes = $(this).closest('form').find(':checkbox');
    if($(this).is(':checked')) {
        checkboxes.attr('checked', 'checked');
    } else {
        checkboxes.removeAttr('checked');
    }
});
})($);
