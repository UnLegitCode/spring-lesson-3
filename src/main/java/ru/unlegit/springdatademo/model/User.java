package ru.unlegit.springdatademo.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {

    int id = -1;
    String name;
    String email;

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }
}