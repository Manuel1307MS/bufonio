package com.murillo.bufonio.util.mapper;

import com.murillo.bufonio.dto.update.ChannelUpdate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.murillo.bufonio.dto.ChannelDTO;
import com.murillo.bufonio.dto.request.ChannelRequest;
import com.murillo.bufonio.model.Channel;

@Mapper(componentModel = "spring")
public interface ChannelMapper {

    ChannelDTO toDTO(Channel channel);

    @Mapping(target = "idChannel", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "tokenChannel", ignore = true)
    Channel fromChannelRequest(ChannelRequest channelRequest);

    Channel fromChannelUpdate(ChannelUpdate channelUpdate);
}