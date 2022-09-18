package ru.pashabezborod.vigenereCoder.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.pashabezborod.vigenereCoder.util.UserDataException;
import ru.pashabezborod.vigenereCoder.login.CookiesInfo;
import ru.pashabezborod.vigenereCoder.models.User;
import ru.pashabezborod.vigenereCoder.services.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/login")
public class UserController {

    private final UserService userService;
    private final CookiesInfo cookiesInfo;
    @Autowired
    public UserController(UserService userService, CookiesInfo cookiesInfo) {
        this.userService = userService;
        this.cookiesInfo = cookiesInfo;
    }

    @PostMapping()
    public ResponseEntity<HttpStatus> addUser(@RequestBody @Valid User user, BindingResult bindingResult) throws UserDataException {
        return userService.addUser(user, bindingResult);
    }

    @GetMapping()
    public String register(@RequestParam("name") String name,
                         @RequestParam("password") String password,
                         HttpServletRequest request) throws UserDataException {
        return userService.registerUser(name, password, request);
    }

    @DeleteMapping("/user")
    public ResponseEntity<HttpStatus> deleteUser(@RequestParam("cookie") long cookie, HttpServletRequest request) throws UserDataException {
        return userService.deleteUser(cookie, request);
    }

    @DeleteMapping("/cookie")
    public ResponseEntity<HttpStatus> deleteCookie(@RequestParam("cookie") long cookie, HttpServletRequest request) {
        cookiesInfo.deleteCookie(cookie, request.getHeader("user-agent"));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler
    private ResponseEntity<String> wrongUserDataHandler(UserDataException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
