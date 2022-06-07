package com.example.boardv.config.auth.dto.General;

import com.example.boardv.config.auth.domain.user.General.User;
import com.example.boardv.config.auth.domain.user.Role;
import lombok.Getter;

import java.io.Serializable;

//인증된 사용자 정보를 세션에 저장하기 위한 클라스
@Getter
public class UserSessionDto implements Serializable {
    private String username;
    private String password;
    private String email;
    private Role role;

    //Entity -> DTO
    public UserSessionDto(User user){
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.email = user.getEmail();
        this.role = user.getRole();
    }
}
