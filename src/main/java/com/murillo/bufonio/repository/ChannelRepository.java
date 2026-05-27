package com.murillo.bufonio.repository;

import java.util.List;
import java.util.Optional;

import com.murillo.bufonio.dto.summary.ChannelSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.murillo.bufonio.model.Channel;

@Repository
public interface ChannelRepository extends JpaRepository<Channel, Long> {

    List<Channel> findAllByUserIdUserOrderByCreatedAtDesc(Long userIdUser);

    Optional<Channel> findByTokenChannel(String tokenChannel);

    Optional<Channel> findByTokenChannelAndUserIdUser(String tokenChannel, Long userIdUser);

    void deleteByTokenChannelAndUserIdUser(String tokenChannel, Long userIdUser);

    @Query("SELECT c.idChannel as idChannel, c.nameChannel as nameChannel, COALESCE(AVG(p.urgencyLevel), 0.0) as averageUrgency " +
            "FROM Channel c " +
            "LEFT JOIN Parchment p ON c.idChannel = p.channelIdChannel " +
            "WHERE c.userIdUser = :userIdUser " +
            "GROUP BY c.idChannel, c.nameChannel, c.createdAt " +
            "ORDER BY c.createdAt DESC")
    List<ChannelSummary> findChannelsSummaryByUserId(@Param("userIdUser") Long userIdUser);
}