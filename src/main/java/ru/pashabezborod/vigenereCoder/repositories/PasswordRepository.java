package ru.pashabezborod.vigenereCoder.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.pashabezborod.vigenereCoder.models.Password;
import ru.pashabezborod.vigenereCoder.models.User;

import java.util.List;

@Repository
public interface PasswordRepository extends JpaRepository<Password, Integer> {

    List<Password> getPasswordsByOwner(User user);
}
