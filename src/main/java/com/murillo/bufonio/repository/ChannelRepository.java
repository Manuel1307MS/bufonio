package com.murillo.bufonio.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.murillo.bufonio.model.Channel;
import com.murillo.bufonio.model.User;
import org.springframework.stereotype.Repository;

@Repository
public interface ChannelRepository extends JpaRepository<Channel, Long> {

    List<Channel> findAllByUserOrderByCreatedAtDesc(User user);

    Optional<Channel> findByTokenChannel(String tokenChannel);

    Optional<Channel> findByTokenChannelAndUser(String tokenChannel, User user);

    void deleteByTokenChannelAndUser(String tokenChannel, User user);

}