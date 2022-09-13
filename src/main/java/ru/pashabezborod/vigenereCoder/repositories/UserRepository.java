package ru.pashabezborod.vigenereCoder.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.pashabezborod.vigenereCoder.models.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> getUserByName(String name);

    Optional<User> getUserByNameAndPassword(String name, long password);

}
