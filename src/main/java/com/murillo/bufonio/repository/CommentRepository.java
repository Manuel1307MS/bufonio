package com.murillo.bufonio.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.murillo.bufonio.model.Comment;
import com.murillo.bufonio.model.User;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByChannel_IdChannelAndChannel_UserAndCreatedAtAfterAndProcessedFalse(
            Long channelId,
            User user,
            LocalDateTime afterDate
    );
}