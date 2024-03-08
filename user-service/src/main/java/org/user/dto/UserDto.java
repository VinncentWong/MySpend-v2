package org.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.user.entity.User;
import org.user.validator.annotation.StrictRole;

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
            LocalDate birthDate,

            @NotNull
            @NotBlank
            @StrictRole
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

    public record Login(
            String email,
            String password
    ){
        public User toUser(){
            return User
                    .builder()
                    .email(email)
                    .password(password)
                    .build();
        }
    }
}
