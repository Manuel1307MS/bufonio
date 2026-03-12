package com.murillo.bufonio.util.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.murillo.bufonio.dto.UserDTO;
import com.murillo.bufonio.security.dto.request.auth.RegisterRequest;
import com.murillo.bufonio.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO toDTO(User user);

    @Mapping(target = "idUser", ignore = true)
    @Mapping(target = "channels", ignore = true)
    User fromRegisterRequest(RegisterRequest registerRequest);
}