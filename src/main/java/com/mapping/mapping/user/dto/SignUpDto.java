package com.mapping.mapping.user.dto;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpDto {

    @Email(message = "이메일 형식에 맞지 않습니다.")
    private String userEmail;

    private String userPassword;

    private String userPasswordcheck;

    private String userNickname;

}
