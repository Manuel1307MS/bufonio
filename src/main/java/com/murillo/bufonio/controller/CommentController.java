package com.murillo.bufonio.controller;

import com.murillo.bufonio.dto.request.CommentRequest;
import com.murillo.bufonio.model.Comment;
import com.murillo.bufonio.service.CommentService;
import com.murillo.bufonio.util.mapper.CommentMapper;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/channels/{tokenChannel}/comments")
@Validated
public class CommentController {

    private final CommentService commentService;
    private final CommentMapper commentMapper;

    public CommentController(CommentService commentService, CommentMapper commentMapper) {
        this.commentService = commentService;
        this.commentMapper = commentMapper;
    }

    @PostMapping("/public")
    public ResponseEntity<Void> createPublicComment(
            @NotBlank @PathVariable String tokenChannel,
            @RequestBody CommentRequest commentRequest) {

        Comment comment = commentMapper.fromCommentRequest(commentRequest);
        commentService.createComment(tokenChannel, comment);
        return ResponseEntity.noContent().build();
    }
}