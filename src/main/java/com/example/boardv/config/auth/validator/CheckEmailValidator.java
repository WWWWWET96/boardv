package com.example.boardv.config.auth.validator;

import com.example.boardv.config.auth.domain.user.General.UserRepository;
import com.example.boardv.config.auth.dto.General.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@RequiredArgsConstructor
@Component
public class CheckEmailValidator extends AbstractValidator <UserDto> {
    private final UserRepository userRepository;
    @Override
    protected void doValidate(UserDto dto, Errors errors) {
        if(userRepository.existsByEmail(dto.toEntity().getEmail())){
            errors.rejectValue("email", "이메일 중복 오류", "이미 사용중인 이메일 입니다.");
        }
    }
}
