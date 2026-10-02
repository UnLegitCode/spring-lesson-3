package ru.unlegit.springdatademo.service;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.unlegit.springdatademo.dto.UserDto;
import ru.unlegit.springdatademo.exception.UserNotFoundException;
import ru.unlegit.springdatademo.mapper.UserMapper;
import ru.unlegit.springdatademo.model.User;
import ru.unlegit.springdatademo.repository.UserRepository;

import java.util.List;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {

    UserRepository userRepository;
    UserMapper userMapper;

    public List<UserDto> listUsers() {
        var userList = userRepository.findAll();

        return userMapper.modelToDto(userList);
    }

    public UserDto getUserById(int id) {
        var user = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);

        return userMapper.modelToDto(user);
    }

    public UserDto createUser(String name, String author) {
        var user = userRepository.save(new User(name, author));

        return userMapper.modelToDto(user);
    }

    public UserDto updateUser(UserDto userDto) {
        var user = userRepository.save(userMapper.dtoToModel(userDto));

        return userMapper.modelToDto(user);
    }

    public void deleteUser(int id) {
        if (!userRepository.deleteById(id)) {
            throw new UserNotFoundException();
        }
    }
}