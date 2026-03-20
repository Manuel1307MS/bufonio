package com.murillo.bufonio.service;

import com.murillo.bufonio.dto.ParchmentAnalysis;
import com.murillo.bufonio.model.Comment;
import com.murillo.bufonio.repository.CommentRepository;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BufonioServiceImp implements BufonioService {

    @Autowired
    private ChatClient chatClient;

    @Autowired
    private CommentRepository commentRepository;

    @Value("classpath:/prompts/parchment-analysis.st")
    private Resource parchmentAnalysisResource;

    @Override
    @Transactional
    public ParchmentAnalysis generateParchmentAnalysis(List<Comment> comments) {

        String commentsText = comments.stream()
                .map(c -> "- " + c.getComment())
                .collect(Collectors.joining("\n"));

        ParchmentAnalysis analysis = chatClient.prompt()
                .user(u -> u.text(parchmentAnalysisResource)
                        .param("commentsText", commentsText))
                .call()
                .entity(ParchmentAnalysis.class);

        comments.forEach(c -> c.setProcessed(true));
        commentRepository.saveAll(comments);

        return analysis;
    }
}