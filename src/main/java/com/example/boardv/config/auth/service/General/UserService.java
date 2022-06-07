package com.example.boardv.config.auth.service.General;

import com.example.boardv.config.auth.domain.user.General.UserRepository;
import com.example.boardv.config.auth.dto.General.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

@Transactional
    public Long join(UserDto dto){
        dto.setPassword(encoder.encode(dto.getPassword())); //비밀번호 해쉬 암호화
        return userRepository.save(dto.toEntity()).getId();//암호화 후 db 저장
    }
}
