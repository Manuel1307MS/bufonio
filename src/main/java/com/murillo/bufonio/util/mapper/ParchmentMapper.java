package com.murillo.bufonio.util.mapper;

import com.murillo.bufonio.dto.ParchmentAnalysis;
import com.murillo.bufonio.dto.ParchmentDTO;
import com.murillo.bufonio.model.Parchment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ParchmentMapper {

    ParchmentDTO toDTO(Parchment parchment);

    @Mapping(target = "idParchment", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "commentsCount", ignore = true)
    @Mapping(target = "channelIdChannel", ignore = true)
    Parchment toParchment(ParchmentAnalysis parchmentAnalysis);
}