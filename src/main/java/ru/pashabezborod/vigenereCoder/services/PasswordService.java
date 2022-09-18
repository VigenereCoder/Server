package ru.pashabezborod.vigenereCoder.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pashabezborod.vigenereCoder.login.CookiesInfo;
import ru.pashabezborod.vigenereCoder.models.NewPassRequest;
import ru.pashabezborod.vigenereCoder.models.Password;
import ru.pashabezborod.vigenereCoder.models.User;
import ru.pashabezborod.vigenereCoder.repositories.PasswordRepository;
import ru.pashabezborod.vigenereCoder.repositories.UserRepository;
import ru.pashabezborod.vigenereCoder.util.InvalidCookieException;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class PasswordService {

    private final PasswordRepository passwordRepository;
    private final CookiesInfo cookiesInfo;
    private final UserRepository userRepository;

    @Autowired
    public PasswordService(PasswordRepository userRepository, CookiesInfo cookiesInfo, UserRepository userRepository1) {
        this.passwordRepository = userRepository;
        this.cookiesInfo = cookiesInfo;
        this.userRepository = userRepository1;
    }

    public List<String> getAllPasswords(Long cookie, HttpServletRequest request) throws InvalidCookieException {
        return passwordRepository.getPasswordsByOwner(getUser(cookie, request))
                .stream()
                .map(Password::getName)
                .collect(Collectors.toList());
    }

    public Map<String, Long> getPassAndHash(Long cookie, String passName, HttpServletRequest request) throws InvalidCookieException {
        User user = getUser(cookie, request);
        Password password = passwordRepository.getPasswordByOwnerAndName(user, passName);
        return Collections.singletonMap(password.getPassword(), password.getHash());
    }

    @Transactional
    public ResponseEntity<HttpStatus> addNewPassword(NewPassRequest newPassRequest, HttpServletRequest request) throws InvalidCookieException {
        User user = getUser(newPassRequest.getCookie(), request);
        Password password = new Password();
        password.setName(newPassRequest.getPasswordName());
        password.setPassword(newPassRequest.getPassword());
        password.setHash(newPassRequest.getHash());
        password.setOwner(user);
        user.getPasswordList().add(password);
        passwordRepository.save(password);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<HttpStatus> updatePassword(NewPassRequest newPassRequest, HttpServletRequest request) throws InvalidCookieException {
        User user = getUser(newPassRequest.getCookie(), request);
        Password password = passwordRepository.getPasswordByOwnerAndName(user, newPassRequest.getPasswordName());
        password.setPassword(newPassRequest.getPassword());
        password.setHash(newPassRequest.getHash());
        passwordRepository.save(password);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<HttpStatus> deletePassword(NewPassRequest newPassRequest, HttpServletRequest request) throws InvalidCookieException {
        User user = getUser(newPassRequest.getCookie(), request);
        passwordRepository.deletePasswordByOwnerAndName(user, newPassRequest.getPasswordName());
        return ResponseEntity.ok(HttpStatus.OK);
    }

    private User getUser(Long cookie, HttpServletRequest request) throws InvalidCookieException {
        if (!cookiesInfo.checkCookie(cookie, request.getHeader("user-agent")))
            throw new InvalidCookieException("Log in first");
        return userRepository.getUserByName(cookiesInfo.getUserNameByCookie(cookie))
                .orElseThrow(() -> new InvalidCookieException("Invalid user"));
    }
}
