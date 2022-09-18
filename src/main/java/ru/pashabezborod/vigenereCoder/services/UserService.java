package ru.pashabezborod.vigenereCoder.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import ru.pashabezborod.vigenereCoder.login.CookiesInfo;
import ru.pashabezborod.vigenereCoder.models.User;
import ru.pashabezborod.vigenereCoder.repositories.UserRepository;
import ru.pashabezborod.vigenereCoder.util.InvalidCookieException;
import ru.pashabezborod.vigenereCoder.util.UserDataException;
import ru.pashabezborod.vigenereCoder.util.UserValidator;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;

@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final CookiesInfo cookiesInfo;
    private final UserValidator userValidator;

    @Autowired
    public UserService(UserRepository userRepository, CookiesInfo cookiesInfo, UserValidator userValidator) {
        this.userRepository = userRepository;
        this.cookiesInfo = cookiesInfo;
        this.userValidator = userValidator;
    }

    @Transactional
    public ResponseEntity<HttpStatus> addUser(User user, BindingResult bindingResult) throws UserDataException {
        validateUser(user, bindingResult);
        userRepository.save(user);
        return ResponseEntity.ok(HttpStatus.OK);

    }

    public boolean checkUserByNameAndPassword(String name, long password) {
        return userRepository.getUserByNameAndPassword(name, password).isPresent();
    }

    public Optional<User> getUserByName(String name) {
        return userRepository.getUserByName(name);
    }

    @Transactional
    public ResponseEntity<HttpStatus> deleteUser(long cookie, HttpServletRequest request) throws UserDataException {
        if (!cookiesInfo.checkCookie(cookie, request.getHeader("user-agent")))
            throw new InvalidCookieException("Invalid cookie. Log in first");
        Optional<User> user = getUserByName(cookiesInfo.getUserNameByCookie(cookie));
        if (user.isEmpty())
            throw new UserDataException("Invalid user data. Please log in again");
        cookiesInfo.deleteCookie(user.get().getName());
        userRepository.delete(user.get());
        return ResponseEntity.ok(HttpStatus.OK);
    }

    public String registerUser(String name, String password, HttpServletRequest request) throws UserDataException {
        User user = new User();
        user.setPassword(password);
        if (!checkUserByNameAndPassword(name, user.getPassword()))
            throw new UserDataException("User name or password is invalid");
        return String.valueOf(cookiesInfo.addCookie(name, request.getHeader("user-agent")));
    }

    private void validateUser(User user, BindingResult bindingResult) throws UserDataException{
        userValidator.validate(user, bindingResult);
        if(bindingResult.hasErrors()) {
            StringJoiner errorMessage = new StringJoiner(" and ");
            List<FieldError> errors = bindingResult.getFieldErrors();
            errors.forEach(er ->
                    errorMessage.add(er.getDefaultMessage()));
            throw new UserDataException(errorMessage.toString());
        }
    }


}
