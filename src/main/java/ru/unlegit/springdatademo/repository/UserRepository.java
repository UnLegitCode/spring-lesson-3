package ru.unlegit.springdatademo.repository;

import org.springframework.data.repository.ListCrudRepository;
import ru.unlegit.springdatademo.model.User;

import java.util.List;

public interface UserRepository extends ListCrudRepository<User, Integer> {

    boolean existsByEmail(String email);

    List<User> findAllByCountry(String country);

    long countByCountry(String country);

    List<User> findAllByCountryAndAgeGreaterThanEqual(String country, int ageIsGreaterThan);
}