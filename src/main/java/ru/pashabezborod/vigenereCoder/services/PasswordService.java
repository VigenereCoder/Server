package ru.pashabezborod.vigenereCoder.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pashabezborod.vigenereCoder.models.Password;
import ru.pashabezborod.vigenereCoder.models.User;
import ru.pashabezborod.vigenereCoder.repositories.PasswordRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class PasswordService {

    private final PasswordRepository passwordRepository;

    @Autowired
    public PasswordService(PasswordRepository userRepository) {
        this.passwordRepository = userRepository;
    }

    public List<String> getAllPasswords(User user) {
        return passwordRepository.getPasswordsByOwner(user).stream()
                .map(Password::getName)
                .collect(Collectors.toList());
    }
}
