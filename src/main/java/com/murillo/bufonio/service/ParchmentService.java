package com.murillo.bufonio.service;

import com.murillo.bufonio.dto.ParchmentAnalysis;
import com.murillo.bufonio.dto.summary.ParchmentSummary;
import com.murillo.bufonio.exception.custom.RecourseNotFoundException;
import com.murillo.bufonio.model.Channel;
import com.murillo.bufonio.model.Comment;
import com.murillo.bufonio.model.Parchment;
import com.murillo.bufonio.model.User;
import com.murillo.bufonio.repository.ParchmentRepository;
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
        Channel channel = channelService.getChannelByTokenChannel(tokenChannel);
        User user = channel.getUser();
        return parchmentRepository.findAllByChannel_IdChannelAndChannel_UserOrderByCreatedAtDesc(channel.getIdChannel(), user);
    }

    public Parchment getParchmentByTokenChannelAndIdParchment(String tokenChannel, Long idParchment) {
        Channel channel = channelService.getChannelByTokenChannel(tokenChannel);
        return parchmentRepository.findByIdParchmentAndChannel(idParchment, channel)
                .orElseThrow(() -> new RecourseNotFoundException(
                        "No se encontró el pergamino con id " + idParchment + " en este canal"));
    }
}