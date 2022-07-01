package com.example.boardv.config.auth.validator;

import com.example.boardv.config.auth.domain.user.General.UserRepository;
import com.example.boardv.config.auth.dto.General.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@RequiredArgsConstructor
@Component
public class CheckUsernameValidator extends AbstractValidator<UserDto>{
    private final UserRepository userRepository;


    @Override
    protected void doValidate(UserDto dto, Errors errors) {
        if(userRepository.existsByUsername(dto.toEntity().getUsername())){
            errors.rejectValue("username", "아이디 중복 오류", "이미 사용중인 아이디입니다.");
        }
    }
}
