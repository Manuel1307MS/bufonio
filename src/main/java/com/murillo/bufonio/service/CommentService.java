package com.murillo.bufonio.service;

import com.murillo.bufonio.model.Channel;
import com.murillo.bufonio.model.Comment;
import com.murillo.bufonio.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentService {

    @Autowired
    private ChannelService channelService;

    @Autowired
    private CommentRepository commentRepository;

    public void createComment(String tokenChannel, Comment comment) {
        Channel channel = channelService.getChannelByTokenChannelPublic(tokenChannel);
        comment.setChannel(channel);
        comment.setCreatedAt(LocalDateTime.now());
        commentRepository.save(comment);
    }

    public List<Comment> getCommentsByTokenChannel(String tokenChannel, LocalDateTime days) {
        Channel channel = channelService.getChannelByTokenChannel(tokenChannel);
        return commentRepository.findAllByChannel_IdChannelAndChannel_UserAndCreatedAtAfterAndProcessedFalse(
                channel.getIdChannel(),
                channel.getUser(),
                days);
    }
}