package com.wootecobook.turkey.comment.service;

import com.wootecobook.turkey.comment.domain.Comment;
import com.wootecobook.turkey.comment.domain.CommentRepository;
import com.wootecobook.turkey.comment.service.dto.CommentCreate;
import com.wootecobook.turkey.comment.service.dto.CommentResponse;
import com.wootecobook.turkey.comment.service.dto.CommentUpdate;
import com.wootecobook.turkey.comment.service.exception.CommentDeleteException;
import com.wootecobook.turkey.comment.service.exception.CommentNotFoundException;
import com.wootecobook.turkey.comment.service.exception.CommentSaveException;
import com.wootecobook.turkey.post.domain.Post;
import com.wootecobook.turkey.post.service.PostService;
import com.wootecobook.turkey.user.domain.User;
import com.wootecobook.turkey.user.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CommentService {
    private final PostService postService;
    private final UserService userService;
    private final CommentRepository commentRepository;

    public CommentService(final PostService postService, final UserService userService, final CommentRepository commentRepository) {
        this.postService = postService;
        this.userService = userService;
        this.commentRepository = commentRepository;
    }

    @Transactional(readOnly = true)
    public Page<CommentResponse> findCommentResponsesByPostId(final Long postId, final Pageable pageable) {

        final Post post = postService.findById(postId);
        return commentRepository.findAllByPost(post, pageable)
                .map(CommentResponse::from);
    }

    public CommentResponse save(final CommentCreate commentCreate) {
        final User user = userService.findById(commentCreate.getUserId());
        final Post post = postService.findById(commentCreate.getPostId());
        final Comment parent = findById(commentCreate.getParentId());
        final Comment comment = commentCreate.toEntity(user, post, parent);
        try {
            return CommentResponse.from(commentRepository.save(comment));
        } catch (RuntimeException e) {
            throw new CommentSaveException(e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public Comment findById(final Long id) {
        return commentRepository.findById(id).orElseThrow(() -> new CommentNotFoundException(id));
    }

    public void delete(final Long id, final Long userId) {
        Comment comment = findById(id);
        comment.isWrittenBy(userId);
        try {
            commentRepository.delete(comment);
        } catch (IllegalArgumentException e) {
            throw new CommentDeleteException(e.getMessage());
        }
    }

    public CommentResponse update(final CommentUpdate commentUpdate) {
        Comment comment = findById(commentUpdate.getId());
        comment.isWrittenBy(commentUpdate.getUserId());
        comment.update(commentUpdate.toEntity());
        return CommentResponse.from(comment);
    }
}
