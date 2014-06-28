package org.exoplatform.task;

import java.util.List;

import org.exoplatform.task.model.Comment;

public class TestComment extends AbstractTest {

    public void testCreateComment() throws TaskServiceException {
        Comment c = new Comment("test author", "test create comment");
        c.setTaskId(testTask.getId());
        
        Comment newComment = service.addComment(c);
        assertNotNull(service.getCommentById(newComment.getId()));
    
        List<Comment> comments = service.getCommentByTask(testTask.getId(), 0, -1);
        assertEquals(2, comments.size());
    }
    
    public void testRemoveComment() throws TaskServiceException {
        assertNotNull(service.getCommentById(testComment.getId()));
        service.removeComment(testComment.getId());
        assertNull(service.getCommentById(testComment.getId()));
    }
    
    public void testUpdateComment() {
        Comment c = service.getCommentById(testComment.getId());
        assertEquals("comment for issue", c.getText());
        c.setText("updated");
        service.updateComment(c);
        
        Comment updated = service.getCommentById(testComment.getId());
        assertEquals("updated", updated.getText());
    }
}
