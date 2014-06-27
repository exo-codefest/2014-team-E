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


})($);

   
