package com.murillo.bufonio.service;

import com.murillo.bufonio.exception.custom.RecourseNotFoundException;
import com.murillo.bufonio.model.Channel;
import com.murillo.bufonio.model.User;
import com.murillo.bufonio.repository.ChannelRepository;
import com.murillo.bufonio.security.service.SecurityContextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ChannelService {

    @Autowired
    private ChannelRepository channelRepository;

    @Autowired
    private SecurityContextService securityContextService;

    public Channel createChannel(Channel channel){
        User user = securityContextService.getCurrentUser().getUser();
        channel.setUser(user);
        channel.setTokenChannel(UUID.randomUUID().toString());
        channel.setCreatedAt(LocalDateTime.now());
        return channelRepository.save(channel);
    }

    public List<Channel> getChannelsByUser(){
        User user = securityContextService.getCurrentUser().getUser();
        return channelRepository.findAllByUserOrderByCreatedAtDesc(user);
    }

    public Channel getChannelByTokenChannel(String tokenChannel) {
        User user = securityContextService.getCurrentUser().getUser();
        return channelRepository.findByTokenChannelAndUser(tokenChannel,user)
                .orElseThrow(() -> new RecourseNotFoundException(
                        "No se encontró el canal con token: " + tokenChannel));
    }

    public Channel getChannelByTokenChannelPublic(String tokenChannel) {
        return channelRepository.findByTokenChannel(tokenChannel)
                .orElseThrow(() -> new RecourseNotFoundException(
                        "No se encontró el canal con token: " + tokenChannel));
    }

    @Transactional
    public void deleteByTokenChannel(String tokenChannel) {
        User user = securityContextService.getCurrentUser().getUser();
        channelRepository.deleteByTokenChannelAndUser(tokenChannel,user);
    }

    @Transactional
    public Channel updateChannelByTokenChannel(String tokenChannel, Channel channel) {
        User user = securityContextService.getCurrentUser().getUser();
        Channel channelUpdate = channelRepository.findByTokenChannelAndUser(tokenChannel,user)
                .orElseThrow(() -> new RecourseNotFoundException("No se encontro el canal con token: " + tokenChannel));
        channelUpdate.setNameChannel(channel.getNameChannel());
        return channelRepository.save(channelUpdate);
    }
}