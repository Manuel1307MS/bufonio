package com.murillo.bufonio.service;

import com.murillo.bufonio.model.Channel;
import com.murillo.bufonio.model.Comment;
import com.murillo.bufonio.model.User;
import com.murillo.bufonio.repository.CommentRepository;
import com.murillo.bufonio.security.service.SecurityContextService;
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

    @Autowired
    private SecurityContextService securityContextService;

    public void createComment(String tokenChannel, Comment comment) {
        Channel channel = channelService.getChannelByTokenChannelPublic(tokenChannel);
        comment.setChannel(channel);
        comment.setCreatedAt(LocalDateTime.now());
        commentRepository.save(comment);
    }

    public List<Comment> getCommentsByTokenChannel(String tokenChannel, LocalDateTime days) {
        User user = securityContextService.getCurrentUser().getUser();
        return commentRepository.findAllByChannel_TokenChannelAndChannel_User_IdUserAndCreatedAtAfterAndProcessedFalse(
                tokenChannel,
                user.getIdUser(),
                days);
    }
}