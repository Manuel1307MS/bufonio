package com.murillo.bufonio.service;

import com.murillo.bufonio.dto.ParchmentAnalysis;
import com.murillo.bufonio.dto.summary.ParchmentSummary;
import com.murillo.bufonio.exception.custom.RecourseNotFoundException;
import com.murillo.bufonio.model.Channel;
import com.murillo.bufonio.model.Comment;
import com.murillo.bufonio.model.Parchment;
import com.murillo.bufonio.model.User;
import com.murillo.bufonio.repository.ParchmentRepository;
import com.murillo.bufonio.security.service.SecurityContextService;
import com.murillo.bufonio.util.mapper.ParchmentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ParchmentService {

    @Autowired
    private ParchmentRepository parchmentRepository;

    @Autowired
    private SecurityContextService securityContextService;

    @Autowired
    private ChannelService channelService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private BufonioService bufonioService;

    @Autowired
    private ParchmentMapper  parchmentMapper;

    public Parchment createParchment(String tokenChannel) {

        Channel channel = channelService.getChannelByTokenChannel(tokenChannel);

        LocalDateTime sevenDaysAgo = LocalDate.now().minusDays(7).atStartOfDay();

        List<Comment> comments = commentService.getCommentsByTokenChannel(tokenChannel, sevenDaysAgo);

        ParchmentAnalysis analysis = bufonioService.generateParchmentAnalysis(comments);

        Parchment parchment = parchmentMapper.toParchment(analysis);

        parchment.setChannel(channel);
        parchment.setCreatedAt(LocalDateTime.now());
        parchment.setCommentsCount(comments.size());

        return parchmentRepository.save(parchment);
    }

    public List<ParchmentSummary> getParchmentSummariesByTokenChannel(String tokenChannel) {
        User user = securityContextService.getCurrentUser().getUser();
        return parchmentRepository.findAllByChannel_TokenChannelAndChannel_User_IdUserOrderByCreatedAtDesc(
                tokenChannel,
                user.getIdUser()
        );
    }

    public Parchment getParchmentByTokenChannelAndIdParchment(String tokenChannel, Long idParchment) {
        User user = securityContextService.getCurrentUser().getUser();
        return parchmentRepository.findByIdParchmentAndChannel_TokenChannelAndChannel_User_IdUser(idParchment, tokenChannel, user.getIdUser())
                .orElseThrow(() -> new RecourseNotFoundException(
                        "Parchment has not been found with ID: " + idParchment + " for the specified channel"));
    }
}