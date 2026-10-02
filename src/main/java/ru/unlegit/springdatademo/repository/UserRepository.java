package ru.unlegit.springdatademo.repository;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.unlegit.springdatademo.model.User;

import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class UserRepository {

    private static final RowMapper<User> USER_ROW_MAPPER = (resultSet, rowNumber) -> new User(
            resultSet.getInt("id"),
            resultSet.getString("name"),
            resultSet.getString("email")
    );
    private static final String TABLE_NAME = "users";
    private static final String INSERT_QUERY = "INSERT INTO %s(name, email) VALUES(?,?)".formatted(
            TABLE_NAME
    );
    private static final String UPDATE_QUERY = "UPDATE %s SET name=?, email=? WHERE id=?".formatted(
            TABLE_NAME
    );
    private static final String SELECT_ALL_QUERY = "SELECT * FROM %s".formatted(TABLE_NAME);
    private static final String SELECT_BY_ID_QUERY = "SELECT * FROM %s WHERE id=?".formatted(TABLE_NAME);
    private static final String DELETE_BY_ID_QUERY = "DELETE FROM %s WHERE id=?".formatted(TABLE_NAME);

    private final JdbcTemplate jdbcTemplate;

    public User save(User user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        if (user.getId() == -1) {
            jdbcTemplate.update(connection -> {
                var statement = connection.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS);

                statement.setString(1, user.getName());
                statement.setString(2, user.getEmail());

                return statement;
            }, keyHolder);

            user.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        } else {
            jdbcTemplate.update(UPDATE_QUERY, user.getName(), user.getEmail(), user.getId());
        }

        return user;
    }

    public List<User> findAll() {
        return jdbcTemplate.query(SELECT_ALL_QUERY, USER_ROW_MAPPER);
    }

    public Optional<User> findById(int id) {
        return jdbcTemplate.query(SELECT_BY_ID_QUERY, USER_ROW_MAPPER, id)
                .stream()
                .findAny();
    }

    public boolean deleteById(int id) {
        return jdbcTemplate.update(DELETE_BY_ID_QUERY, id) != 0;
    }
}