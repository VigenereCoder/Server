package ru.pashabezborod.vigenereCoder.models;

import lombok.Data;

@Data
public class NewPassRequest {

    private String userName;

    private long cookie;
    private String passwordName;
    private String password;
    private long hash;
}
