package com.mapping.mapping.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpDto {
    private String userEmail;
    private String userPassword;
    private String userPasswordcheck;
    private String userNickname;
    private String userPhoneNumber;
    private String userAddress;
    private String userAddressdetail;
}
