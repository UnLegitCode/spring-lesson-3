package ru.unlegit.springdatademo.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.unlegit.springdatademo.dto.UserCreateDto;
import ru.unlegit.springdatademo.dto.UserDto;
import ru.unlegit.springdatademo.service.UserService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<UserDto> listUsers() {
        return userService.listUsers();
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable int id) {
        return userService.getUserById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@RequestBody UserCreateDto dto) {
        return userService.createUser(dto.name(), dto.email());
    }

    @PutMapping
    public UserDto updateUser(@RequestBody UserDto dto) {
        return userService.updateUser(dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable int id) {
        userService.deleteUser(id);
    }
}