package com.murillo.bufonio.controller;

import com.murillo.bufonio.dto.ChannelDTO;
import com.murillo.bufonio.dto.request.ChannelRequest;
import com.murillo.bufonio.dto.update.ChannelUpdate;
import com.murillo.bufonio.model.Channel;
import com.murillo.bufonio.service.ChannelService;
import com.murillo.bufonio.util.LocationUtil;
import com.murillo.bufonio.util.mapper.ChannelMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/channels")
@Validated
public class ChannelController {

    private final ChannelService channelService;
    private final ChannelMapper channelMapper;

    public ChannelController(ChannelService channelService, ChannelMapper channelMapper) {
        this.channelService = channelService;
        this.channelMapper = channelMapper;
    }

    @PostMapping
    public ResponseEntity<ChannelDTO> createChannel(@Valid @RequestBody ChannelRequest channelRequest) {
        Channel channel = channelMapper.fromChannelRequest(channelRequest);
        Channel newChannel = channelService.createChannel(channel);
        ChannelDTO channelDTO = channelMapper.toDTO(newChannel);
        URI location = LocationUtil.getLocation("/api/channels", newChannel.getIdChannel());
        return ResponseEntity.created(location).body(channelDTO);
    }

    @GetMapping
    public ResponseEntity<List<ChannelDTO>> getChannels() {
        List<ChannelDTO> channelsDTO = channelService.getChannelsByUser().stream()
                .map(channelMapper::toDTO)
                .toList();
        return ResponseEntity.ok(channelsDTO);
    }

    @GetMapping("/{tokenChannel}")
    public ResponseEntity<ChannelDTO> getChannelByToken(
            @NotBlank @PathVariable String tokenChannel) {
        Channel channel = channelService.getChannelByTokenChannel(tokenChannel);
        return ResponseEntity.ok(channelMapper.toDTO(channel));
    }

    @PutMapping("/{tokenChannel}")
    public ResponseEntity<ChannelDTO> updateChannel(
            @PathVariable String tokenChannel,
            @Valid @RequestBody ChannelUpdate channelUpdate) {
        Channel channel = channelMapper.fromChannelUpdate(channelUpdate);
        Channel updated = channelService.updateChannelByTokenChannel(tokenChannel, channel);
        return ResponseEntity.ok(channelMapper.toDTO(updated));
    }

    @DeleteMapping("/{tokenChannel}")
    public ResponseEntity<Void> deleteChannel(@PathVariable String tokenChannel) {
        channelService.deleteByTokenChannel(tokenChannel);
        return ResponseEntity.noContent().build();
    }
}