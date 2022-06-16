package com.example.boardv.config.auth.dto.General;

import com.example.boardv.config.auth.domain.user.Role;
import com.example.boardv.config.auth.domain.user.General.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class UserDto {

    @NotBlank(message = "아이디를 입력해주세요.") //입력폼이 비어있는 상태로 요청을 보내면 해당 에러 메시지가 나타남
    @Size(min = 2, max = 30, message = "닉네임은 2자 이상 30자 이하로 입력해주세요.") //지정된 사이즈에 벗어나는 값 입력시 해당 에러 메시지 나타남
    private String username;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하로 입력해주세요.")
    private String password;

    @NotBlank(message = "이메일 주소를 입력해주세요.")
    @Email(message = "올바른 이메일 주소를 입력해주세요.") //이메일 형태만 입력이 가능함
    private String email;

    private Role role;
    @Builder
    public UserDto(String username, String password, String email, Role role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }
    //DTO -> Entity
    public User toEntity(){
        User user = User.builder()
                .username(username)
                .password(password)
                .email(email)
                .role(role.USER)
                .build();
        return user;
    }
}
