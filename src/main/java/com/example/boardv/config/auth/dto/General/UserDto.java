package com.example.boardv.config.auth.dto.General;

import com.example.boardv.config.auth.domain.user.Role;
import com.example.boardv.config.auth.domain.user.General.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
    private String username;
    private String password;
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
                .role(role.GUEST)
                .build();
        return user;
    }
}
