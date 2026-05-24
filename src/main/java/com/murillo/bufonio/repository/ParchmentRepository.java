package com.murillo.bufonio.repository;

import java.util.List;
import java.util.Optional;
import com.murillo.bufonio.dto.summary.ParchmentSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.murillo.bufonio.model.Parchment;

@Repository
public interface ParchmentRepository extends JpaRepository<Parchment, Long> {

    @Query("SELECT p FROM Parchment p JOIN Channel c ON p.channelIdChannel = c.idChannel " +
            "WHERE c.tokenChannel = :tokenChannel AND c.userIdUser = :idUser " +
            "ORDER BY p.createdAt DESC")
    List<ParchmentSummary> findAllByChannelData(
            @Param("tokenChannel") String tokenChannel,
            @Param("idUser") Long idUser
    );

    @Query("SELECT p FROM Parchment p JOIN Channel c ON p.channelIdChannel = c.idChannel " +
            "WHERE p.idParchment = :idParchment AND c.tokenChannel = :tokenChannel AND c.userIdUser = :idUser")
    Optional<Parchment> findByIdAndChannelData(
            @Param("idParchment") Long idParchment,
            @Param("tokenChannel") String tokenChannel,
            @Param("idUser") Long idUser
    );
}