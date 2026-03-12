package com.murillo.bufonio.repository;

import java.util.List;
import java.util.Optional;

import com.murillo.bufonio.dto.summary.ParchmentSummary;
import com.murillo.bufonio.model.Channel;
import org.springframework.data.jpa.repository.JpaRepository;

import com.murillo.bufonio.model.Parchment;
import com.murillo.bufonio.model.User;
import org.springframework.stereotype.Repository;

@Repository
public interface ParchmentRepository extends JpaRepository<Parchment, Long> {

    List<ParchmentSummary> findAllByChannel_IdChannelAndChannel_UserOrderByCreatedAtDesc(Long idChannel, User user);

    Optional<Parchment> findByIdParchmentAndChannel(Long idParchment, Channel channel);
}