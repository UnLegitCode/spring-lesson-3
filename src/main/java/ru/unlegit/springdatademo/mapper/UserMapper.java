package ru.unlegit.springdatademo.mapper;

import org.springframework.stereotype.Component;
import ru.unlegit.springdatademo.dto.UserDto;
import ru.unlegit.springdatademo.model.User;

import java.util.List;

@Component
public class UserMapper {

    public UserDto modelToDto(User model) {
        return new UserDto(
                model.getId(), model.getName(), model.getEmail(), model.getCountry(),
                model.getAge(), model.getCreatedAt(), model.getUpdatedAt()
        );
    }

    public List<UserDto> modelToDto(List<User> modelList) {
        return modelList.stream()
                .map(this::modelToDto)
                .toList();
    }
}