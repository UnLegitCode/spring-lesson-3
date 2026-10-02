package ru.unlegit.springdatademo.mapper;

import org.springframework.stereotype.Component;
import ru.unlegit.springdatademo.dto.UserDto;
import ru.unlegit.springdatademo.model.User;

import java.util.List;

@Component
public class UserMapper {

    public UserDto modelToDto(User model) {
        return new UserDto(model.getId(), model.getName(), model.getEmail());
    }

    public List<UserDto> modelToDto(List<User> modelList) {
        return modelList.stream()
                .map(this::modelToDto)
                .toList();
    }

    public User dtoToModel(UserDto dto) {
        return new User(dto.id(), dto.name(), dto.email());
    }

    public List<User> dtoToModel(List<UserDto> dtoList) {
        return dtoList.stream()
                .map(this::dtoToModel)
                .toList();
    }
}