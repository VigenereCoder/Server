package ru.pashabezborod.vigenereCoder.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.pashabezborod.vigenereCoder.login.CookiesInfo;
import ru.pashabezborod.vigenereCoder.models.User;
import ru.pashabezborod.vigenereCoder.services.PasswordService;
import ru.pashabezborod.vigenereCoder.services.UserService;
import ru.pashabezborod.vigenereCoder.util.InvalidCookieException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping()
public class PasswordController {

    private final PasswordService passwordService;
    private final UserService userService;
    private final CookiesInfo cookiesInfo;

    @Autowired
    public PasswordController(PasswordService passwordService, UserService userService, CookiesInfo cookiesInfo) {
        this.passwordService = passwordService;
        this.userService = userService;
        this.cookiesInfo = cookiesInfo;
    }

    @GetMapping()
    public List<String> getAllPasswords(@RequestParam("cookie") long cookie, HttpServletRequest request) {
        if (!cookiesInfo.checkCookie(cookie, request.getHeader("user-agent")))
            throw new InvalidCookieException();
        User user = userService.getUserByName(cookiesInfo.getUserNameByCookie(cookie)).orElseThrow(InvalidCookieException::new);
        return passwordService.getAllPasswords(user);

    }

    @ExceptionHandler
    private ResponseEntity<HttpRequest> invalidCookieHandler(InvalidCookieException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

}
