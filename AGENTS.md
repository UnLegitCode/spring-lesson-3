# AGENTS.md

Single-module Spring Boot REST demo (users CRUD). Java 21, Spring Boot 4.1.1, Gradle 9.7.1 wrapper.

## Commands

Always use `./gradlew` (there is no system `gradle`).

- Build / compile: `./gradlew build`
- Compile only (fastest type check — there is no lint or typecheck task): `./gradlew compileJava`
- Run: `./gradlew bootRun` — serves on `http://localhost:8080`
- Run on another port: `./gradlew bootRun --args='--server.port=18080'`
- H2 console (off by default; must be enabled per run): `./gradlew bootRun --args='--spring.h2.console.enabled=true'` then `/h2-console`

## No test infrastructure

`src/test` does not exist and `spring-boot-starter-test` is **not** on the classpath, so
`./gradlew test` reports `NO-SOURCE` and always passes. A green `test` task means nothing.
To add tests you must first add `testImplementation("org.springframework.boot:spring-boot-starter-test")`
to `build.gradle.kts`.

There is no lint, formatter, or editorconfig config in the repo. `./gradlew build` runs
compile + test only, so `compileJava` plus the `http/UserController.http` smoke-test file
are the entire verification surface.

## JdbcTemplate is deliberate — do not add Spring Data

Despite the project name, the data layer is intentionally hand-written `JdbcTemplate`.
`spring-boot-starter-data-jdbc`, `-data-jpa`, and the PostgreSQL driver are commented out
in `build.gradle.kts`; they are inert, not an oversight or a TODO. Do not uncomment them,
do not introduce `CrudRepository`/`JpaRepository`, and do not replace `UserRepository` with a
Spring Data interface.

## Architecture

Entry point: `ru.unlegit.springdatademo.SpringDataDemoApplication`. Conventional layered
stack, each layer a package under that root:

- `controller/` — `UserController`, `@RestController` at `/users`. Returns DTOs; owns HTTP status codes.
- `service/` — `UserService`, owns `Optional.orElseThrow(UserNotFoundException::new)` and the delete-exists check.
- `repository/` — `UserRepository`, `@Repository` over `JdbcTemplate`. Hand-written SQL constants at the top of the class.
- `mapper/` — `UserMapper`, a hand-written `@Component` (no MapStruct). Overloaded on single `User`/`UserDto` and `List`, delegating to `this::`.
- `model/` — `User`, Lombok class. `dto/` — `UserDto`/`UserCreateDto`, Java records. `exception/` — `UserNotFoundException`.

## Schema is created in code, not by migrations

No Flyway, Liquibase, or `schema_users.sql`. `UserRepository.postInit()` (UserRepository.java:38)
runs a raw `CREATE TABLE users(...)` from `@PostConstruct`, so DDL lives in the repository
class and the schema is dropped and rebuilt on every boot. Adding a table means adding a
`@PostConstruct` block, not a migration.

The DB is H2 in-memory with an auto-generated unique name (e.g. `jdbc:h2:mem:<uuid>`), not
the default `testdb`. Data never survives a restart, and the connection URL changes each run.

## Conventions worth matching

- **Persistence sentinel:** `User.id == -1` means "not yet persisted". `UserRepository.save()`
  branches on this to decide INSERT vs UPDATE. Do not switch to `null`/0 or a separate
  `isNew()` flag without updating `save()`, `UserMapper.dtoToModel()`, and the DTOs together.
- **Lombok is `compileOnly` + `annotationProcessor`.** Any *new* annotation processor must be
  registered in the `annotationProcessor` block of `build.gradle.kts`, not `implementation` —
  forgetting this fails silently at runtime with Lombok-generated members missing.
- Dependency injection is via `@AllArgsConstructor`; `@FieldDefaults(level = PRIVATE, makeFinal = true)`
  on services replaces explicit `private final` keywords.
- `dtoToModel` populates `id` from the DTO, which is how `PUT` reaches the UPDATE branch.

## Environment note

The default `java` on this machine is JDK 23, but the build pins a Java 21 toolchain
(`build.gradle.kts`). Gradle auto-selects an installed JDK 21 (Corretto 21.x) for
`compileJava`/`bootRun`, so this normally needs no intervention. This directory is **not**
a git repository.