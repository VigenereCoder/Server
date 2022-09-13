package ru.pashabezborod.vigenereCoder.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pashabezborod.vigenereCoder.models.User;
import ru.pashabezborod.vigenereCoder.repositories.UserRepository;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public void addUser(User user) {
        userRepository.save(user);
    }

    public boolean checkUserByNameAndPassword(String name, long password) {
        return userRepository.getUserByNameAndPassword(name, password).isPresent();
    }

    public Optional<User> getUserByName(String name) {
        return userRepository.getUserByName(name);
    }

    @Transactional
    public void deleteUser(User user) {
        userRepository.delete(user);
    }


}
