package ru.unlegit.springdatademo.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    @Column(length = 50, nullable = false)
    String name;
    @Column(length = 50, nullable = false, unique = true)
    String email;
    @Column(length = 50, nullable = false)
    String country;
    @ColumnDefault("null")
    int age;
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    Timestamp createdAt;
    @UpdateTimestamp
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "updated_at", nullable = false)
    Timestamp updatedAt;

    public User(String name, String email, String country, int age) {
        this.name = name;
        this.email = email;
        this.country = country;
        this.age = age;
    }
}