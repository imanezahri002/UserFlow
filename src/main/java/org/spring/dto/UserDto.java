package org.spring.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class UserDto {
    private Long id;
    @NotBlank(message="le nom ne doit pas etre vide")

    private String username;
    @Email(message="l'email n'est pas valide")
    private String email;
    private String password;

    public String getPassword() {
        return password;
    }

    @Size(max=15,min=8,message="le password doit contenir entre 8 et 15 caractère")
    public void setPassword(String password) {
        this.password = password;
    }

    public UserDto() {}

    public UserDto(Long id, String username, String email, String password) {
        this.id = id;
        this.password=password;
        this.username = username;
        this.email = email;
    }

    public UserDto(Long id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
