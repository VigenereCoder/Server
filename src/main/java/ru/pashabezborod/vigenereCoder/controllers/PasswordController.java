package ru.pashabezborod.vigenereCoder.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.pashabezborod.vigenereCoder.models.NewPassRequest;
import ru.pashabezborod.vigenereCoder.services.PasswordService;
import ru.pashabezborod.vigenereCoder.util.InvalidCookieException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/password")
public class PasswordController {

    private final PasswordService passwordService;

    @Autowired
    public PasswordController(PasswordService passwordService) {
        this.passwordService = passwordService;
    }

    @GetMapping("/all")
    public List<String> getAllPasswords(@RequestParam("cookie") long cookie, HttpServletRequest request) {
        return passwordService.getAllPasswords(cookie, request);
    }

    @GetMapping()
    public Map<String, Long> getPassAndHash(@RequestParam("cookie") long cookie,
                                            @RequestParam("passName") String passName,
                                            HttpServletRequest request) throws InvalidCookieException {
        return passwordService.getPassAndHash(cookie, passName, request);
    }

    @PostMapping()
    public ResponseEntity<HttpStatus> addNewPassword(@RequestBody NewPassRequest newPassRequest, HttpServletRequest request) {
        return passwordService.addNewPassword(newPassRequest, request);
    }

    @PutMapping
    public ResponseEntity<HttpStatus> updatePassword(@RequestBody NewPassRequest newPassRequest, HttpServletRequest request) {
        return passwordService.updatePassword(newPassRequest, request);
    }

    @DeleteMapping
    public ResponseEntity<HttpStatus> deletePassword(@RequestBody NewPassRequest newPassRequest, HttpServletRequest request) {
        return passwordService.deletePassword(newPassRequest, request);
    }

    @ExceptionHandler
    private ResponseEntity<String> invalidCookieHandler(InvalidCookieException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
