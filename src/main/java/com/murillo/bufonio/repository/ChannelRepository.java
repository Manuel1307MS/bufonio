package com.murillo.bufonio.repository;

import java.util.List;
import java.util.Optional;

import com.murillo.bufonio.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import com.murillo.bufonio.model.Channel;
import org.springframework.stereotype.Repository;

@Repository
public interface ChannelRepository extends JpaRepository<Channel, Long> {

    List<Channel> findAllByUser_IdUserOrderByCreatedAtDesc(Long idUser);

    Optional<Channel> findByTokenChannel(String tokenChannel);

    Optional<Channel> findByTokenChannelAndUser_IdUser(String tokenChannel, Long idUser);

    void deleteByTokenChannelAndUser_IdUser(String tokenChannel, Long idUser);

}