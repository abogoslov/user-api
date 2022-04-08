package ru.bogoslov.userapi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.bogoslov.userapi.controller.request.UserRegisterRequest;
import ru.bogoslov.userapi.entity.User;

@Mapper
public interface UserMapper {

    UserMapper mapper = Mappers.getMapper(UserMapper.class);

    User userRegisterRequestToUser(UserRegisterRequest request);
}
