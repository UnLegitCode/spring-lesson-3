package ru.unlegit.springdatademo.dto;

import java.sql.Timestamp;

public record UserDto(
        int id, String name, String email, String country,
        int age, Timestamp createdAt, Timestamp updatedAt
) {}