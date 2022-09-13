package ru.pashabezborod.vigenereCoder.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.pashabezborod.vigenereCoder.util.UserDataException;
import ru.pashabezborod.vigenereCoder.login.CookiesInfo;
import ru.pashabezborod.vigenereCoder.models.User;
import ru.pashabezborod.vigenereCoder.services.UserService;
import ru.pashabezborod.vigenereCoder.util.UserValidator;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;

@RestController
@RequestMapping("/login")
public class UserController {

    private final UserService userService;
    private final CookiesInfo cookiesInfo;
    private final UserValidator userValidator;

    @Autowired
    public UserController(UserService userService, CookiesInfo cookiesInfo, UserValidator userValidator) {
        this.userService = userService;
        this.cookiesInfo = cookiesInfo;
        this.userValidator = userValidator;
    }

    @PostMapping()
    public ResponseEntity<HttpStatus> addUser(@RequestBody @Valid User user, BindingResult bindingResult) {
        validateUser(user, bindingResult);

        userService.addUser(user);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping()
    public String register(@RequestParam("name") String name,
                         @RequestParam("password") String password,
                         HttpServletRequest request) {
        User user = new User();
        user.setPassword(password);
        if (!userService.checkUserByNameAndPassword(name, user.getPassword()))
            throw new UserDataException("User name or password is invalid");

        return String.valueOf(cookiesInfo.addCookie(name, request.getHeader("user-agent")));
    }

    @DeleteMapping("/user")
    public ResponseEntity<HttpStatus> deleteUser(@RequestParam("cookie") long cookie, HttpServletRequest request) {
        if (!cookiesInfo.checkCookie(cookie, request.getHeader("user-agent")))
            throw new UserDataException("Invalid cookie. Log in first");
        Optional<User> user = userService
                .getUserByName(cookiesInfo.getUserNameByCookie(cookie));
        if (user.isEmpty())
            throw new UserDataException("Invalid user data. Please log in again");

        cookiesInfo.deleteCookie(user.get().getName());
        userService.deleteUser(user.get());
        return ResponseEntity.ok(HttpStatus.OK);
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
