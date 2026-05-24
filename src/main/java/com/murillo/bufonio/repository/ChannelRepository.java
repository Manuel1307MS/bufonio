package com.murillo.bufonio.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.murillo.bufonio.model.Channel;

@Repository
public interface ChannelRepository extends JpaRepository<Channel, Long> {

    List<Channel> findAllByUserIdUserOrderByCreatedAtDesc(Long userIdUser);

    Optional<Channel> findByTokenChannel(String tokenChannel);

    Optional<Channel> findByTokenChannelAndUserIdUser(String tokenChannel, Long userIdUser);

    void deleteByTokenChannelAndUserIdUser(String tokenChannel, Long userIdUser);
}