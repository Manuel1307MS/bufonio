package com.murillo.bufonio.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.murillo.bufonio.model.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT c FROM Comment c JOIN Channel ch ON c.channelIdChannel = ch.idChannel " +
            "WHERE ch.tokenChannel = :tokenChannel " +
            "AND ch.userIdUser = :idUser " +
            "AND c.createdAt > :days " +
            "AND c.processed = false")
    List<Comment> findUnprocessedComments(
            @Param("tokenChannel") String tokenChannel,
            @Param("idUser") Long idUser,
            @Param("days") LocalDateTime days
    );
}