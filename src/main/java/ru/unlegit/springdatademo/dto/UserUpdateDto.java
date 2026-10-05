package ru.unlegit.springdatademo.dto;

public record UserUpdateDto(int id, String name, String email, String country, int age) {}