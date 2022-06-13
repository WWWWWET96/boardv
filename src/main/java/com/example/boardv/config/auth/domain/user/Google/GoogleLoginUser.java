/*
package com.example.boardv.config.auth.domain.user.Google;

import com.example.boardv.config.auth.domain.BaseTimeEntity;
import com.example.boardv.config.auth.domain.user.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class GoogleLoginUser extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column
    private String picture;

    @Enumerated(EnumType.STRING)//JPA로 데이터베이스저장할 때 Enum값을 String으로 저장
    @Column(nullable = false)
    private Role role;

    @Builder
    public GoogleLoginUser(Long id, String name, String email, String picture, Role role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.role = role;
    }
    public GoogleLoginUser update(String name, String picture){
        this.name = name;
        this.picture = picture;

        return this;
    }

    public String getRoleKey(){
        return this.role.getKey();
    }
}
*/
/**/