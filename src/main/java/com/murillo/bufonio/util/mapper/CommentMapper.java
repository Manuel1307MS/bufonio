package com.murillo.bufonio.util.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.murillo.bufonio.dto.CommentDTO;
import com.murillo.bufonio.dto.request.CommentRequest;
import com.murillo.bufonio.model.Comment;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    CommentDTO toDTO(Comment comment);

    @Mapping(target = "idComment", ignore = true)
    @Mapping(target = "channel", ignore = true)
    Comment fromCommentRequest(CommentRequest commentRequest);
}