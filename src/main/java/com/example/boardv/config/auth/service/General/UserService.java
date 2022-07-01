package com.example.boardv.config.auth.service.General;

import com.example.boardv.config.auth.domain.user.General.UserRepository;
import com.example.boardv.config.auth.dto.General.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

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

    public Map<String, String> validateHandling(Errors errors) {
    Map<String, String> validatorResult = new HashMap<>();

    for(FieldError error: errors.getFieldErrors()){
        String validKeyName= String.format("valid_%s", error.getField());
        validatorResult.put(validKeyName, error.getDefaultMessage());
    }
    return validatorResult;
    }
}
