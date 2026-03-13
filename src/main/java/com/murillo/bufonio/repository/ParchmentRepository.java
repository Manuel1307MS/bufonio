package com.murillo.bufonio.repository;

import java.util.List;
import java.util.Optional;

import com.murillo.bufonio.dto.summary.ParchmentSummary;
import com.murillo.bufonio.model.Channel;
import org.springframework.data.jpa.repository.JpaRepository;

import com.murillo.bufonio.model.Parchment;
import org.springframework.stereotype.Repository;

@Repository
public interface ParchmentRepository extends JpaRepository<Parchment, Long> {

    List<ParchmentSummary> findAllByChannel_TokenChannelAndChannel_User_IdUserOrderByCreatedAtDesc(
            String tokenChannel,
            Long idUser
    );

    Optional<Parchment> findByIdParchmentAndChannel_TokenChannelAndChannel_User_IdUser(
            Long idParchment,
            String tokenChannel,
            Long idUser
    );
}