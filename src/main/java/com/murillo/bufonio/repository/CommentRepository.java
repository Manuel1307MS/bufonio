package com.murillo.bufonio.repository;

import java.time.LocalDateTime;
import java.util.List;

import com.murillo.bufonio.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import com.murillo.bufonio.model.Comment;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByChannel_TokenChannelAndChannel_User_IdUserAndCreatedAtAfterAndProcessedFalse(
            String tokenChannel,
            Long idUser,
            LocalDateTime days
    );
}