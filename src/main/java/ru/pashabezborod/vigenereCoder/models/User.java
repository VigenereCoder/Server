package ru.pashabezborod.vigenereCoder.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;


@Entity
@Table(name = "vigenere_user")
@NoArgsConstructor
@Getter
@Setter
public class User {

    @Id @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    @Size(min = 2, max = 20, message = "User name should be between 2 and 20 chars")
    @NotEmpty(message = "User name should not be empty")
    private String name;

    @Column(name = "password")
    private Long password;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.PERSIST)
    private List<Password> passwordList;

    public void setPassword(String password) {
        this.password = (long) password.hashCode() * password.length() * 31L;
    }

    public void setPassword(Long password) {
        this.password = password;
    }

}
