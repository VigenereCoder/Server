package ru.pashabezborod.vigenereCoder.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.pashabezborod.vigenereCoder.models.User;
import ru.pashabezborod.vigenereCoder.repositories.UserRepository;

@Component
public class UserValidator implements Validator {

    private final UserRepository userRepository;

    @Autowired
    public UserValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(User.class);
    }

    @Override
    @Transactional(readOnly = true)
    public void validate(Object target, Errors errors) {
        User user = (User) target;
        if (userRepository.getUserByName(user.getName()).isPresent())
            errors.rejectValue("name", "", "User " + user.getName() + " already exists");
    }
}
