package com.pasta.dto.request;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class RegistrationRequest {

    @NotBlank(message = "Имя не заполнено")
    @Size(min = 3, message = "Имя не должно быть короче 3 символов")
    private String firstname;

    @NotBlank(message = "Фамилия не заполнена")
    @Size(min = 3, message = "Не должно быть короче 3 символов")
    private String lastname;

    @Email(message = "Почта не соответствует формату email")
    private String email;

    @NotBlank(message = "Пароль не заполнен")
    private String password;
}
