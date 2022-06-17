package com.example.boardv.config.auth.service.General;

import com.example.boardv.config.auth.domain.user.General.CustomUserDetails;
import com.example.boardv.config.auth.domain.user.General.User;
import com.example.boardv.config.auth.domain.user.General.UserRepository;
import com.example.boardv.config.auth.dto.General.UserSessionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Component
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    private final HttpSession httpSession;

    //username이 DB에 있는지 확인
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(()->
                new UsernameNotFoundException("해당 사용자가 존재하지 않습니다. : " + username));
        httpSession.setAttribute("user", new UserSessionDto(user));

        //시큐리티 세션에 유저 정보 저장
        return new CustomUserDetails(user);
    }

}
