package ru.pashabezborod.vigenereCoder.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "password")
@NoArgsConstructor
@Getter @Setter
public class Password {

    @Id @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @JoinColumn(name = "owner", referencedColumnName = "id")
    @ManyToOne(targetEntity = User.class)
    private User owner;

    @Column(name = "name")
    @Length(min = 2, max = 20, message = "Pass name should be between 2 and 20 chars")
    @NotEmpty(message = "Pass name should not be empty")
    @NotNull(message = "Need a pass name")
    private String name;

    @Column(name = "password")
    @Length(min = 2, max = 30, message = "Pass should be between 2 and 30 chars")
    @NotEmpty(message = "Pass should not be empty")
    @NotNull(message = "Need a pass")
    private String password;

    @Column(name = "hash")
    private long hash;

}
