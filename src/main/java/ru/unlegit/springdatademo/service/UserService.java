package ru.unlegit.springdatademo.service;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.unlegit.springdatademo.dto.UserDto;
import ru.unlegit.springdatademo.dto.UserUpdateDto;
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

    public UserDto createUser(String name, String author, String country, int age) {
        var user = userRepository.save(new User(name, author, country, age));

        return userMapper.modelToDto(user);
    }

    public UserDto updateUser(UserUpdateDto userDto) {
        var user = userRepository.findById(userDto.id())
                .orElseThrow(UserNotFoundException::new);

        user.setName(userDto.name());
        user.setEmail(userDto.email());
        user.setCountry(userDto.country());
        user.setAge(userDto.age());

        user = userRepository.save(user);

        return userMapper.modelToDto(user);
    }

    public void deleteUser(int id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException();
        }

        userRepository.deleteById(id);
    }

    public List<UserDto> listUsersByCountryAndMinAge(String country, int minAge) {
        return userMapper.modelToDto(
                userRepository.findAllByCountryAndAgeGreaterThanEqual(country, minAge)
        );
    }
}