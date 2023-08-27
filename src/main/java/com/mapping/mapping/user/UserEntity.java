package com.mapping.mapping.user;

import com.mapping.mapping.user.dto.SignUpDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name="User") //해당 클래스를 entity 클래스로 사용
@Table(name="User") //DB에 있는 해당 테이블과 현재 클래스를 매핑
public class UserEntity {
    @Id
    @Email(message = "이메일 형식에 맞지 않습니다.")
    private String userEmail;
    @NotEmpty(message = "사용자 Password는 필수항목입니다.")
    private String userPassword;
    @Column(unique=true,nullable = false)
    @NotEmpty(message = "사용자 Nickname 필수항목입니다.")
    private String userNickname;


    public UserEntity(SignUpDto dto){
        this.userEmail = dto.getUserEmail();
        this.userPassword = dto.getUserPassword();
        this.userNickname = dto.getUserNickname();

    }
}

