package ru.unlegit.springdatademo.dto;

public record UserCreateDto(String name, String email, String country, int age) {}