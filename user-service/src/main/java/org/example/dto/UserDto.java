package org.example.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.example.entity.User;

import java.time.LocalDate;

public class UserDto {

    public record Create(
            @NotNull
            @NotBlank
            @Email
            String email,

            @NotNull
            @NotBlank
            String username,

            @NotNull
            @NotBlank
            String password,

            @NotNull
            @NotBlank
            LocalDate birthDate,

            @NotNull
            @NotBlank
            String role
    ){
        public User toUser(){
            return User
                    .builder()
                    .birthDate(birthDate)
                    .name(username)
                    .email(email)
                    .password(password)
                    .role(role)
                    .build();
        }
    }
}
