(function($){
  $('.comment').on('click', 'a.action', function(e) {
    var $action = $(e.target || e.srcElement).closest('a');
    var $comment = $action.closest(".comment");
    var action = $action.attr('action');
    if(action == "edit") {
      $comment.find(".body").hide();
      $comment.find('.edit').show();
    }
    return false;
  });

  $('.comment').on('click', 'button[type="reset"]', function(e){
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

})($);
