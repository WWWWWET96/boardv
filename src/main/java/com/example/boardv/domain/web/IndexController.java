package com.example.boardv.domain.web;

import com.example.boardv.config.auth.dto.General.UserDto;
import com.example.boardv.config.auth.dto.General.UserSessionDto;
import com.example.boardv.config.auth.service.General.CustomUserDetailsService;
import com.example.boardv.config.auth.service.General.UserService;
import com.example.boardv.config.auth.validator.CheckEmailValidator;
import com.example.boardv.config.auth.validator.CheckUsernameValidator;
import com.example.boardv.dto.PostListResponseDto;
import com.example.boardv.service.PostsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Map;

@RequiredArgsConstructor
@Controller
public class IndexController {
    private final UserService userService;
    private final HttpSession httpSession;
    private final PostsService postsService;
    private final CheckUsernameValidator checkUsernameValidator;
    private final CheckEmailValidator checkEmailValidator;
    private final CustomUserDetailsService customUserDetailsService;
    @InitBinder //커스텀 유효성 검증을 위해 추가
    public void validatorBinder(WebDataBinder binder) {
        binder.addValidators(checkUsernameValidator);
        binder.addValidators(checkEmailValidator);
    }

    @GetMapping("/")
    public String index(Model model,
                        @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {

        UserSessionDto user = (UserSessionDto) httpSession.getAttribute("user");//세션 조회
        if (user != null) {
            model.addAttribute("user", user.getUsername());
        }
        Page<PostListResponseDto> postlist = postsService.search(SearchType.ALL, null, pageable);
      /* Stream<Object> responseDtoStream= postlist.stream().map(postListResponseDto -> postListResponseDto.getModifiedDate()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:SSS")));
        List<String> responseStringList = responseDtoStream.map(Objects::toString).collect(Collectors.toList());

        String[] dateList = new String[]{"modifiedDate", String.valueOf(responseStringList)};*/

        model.addAttribute("posts", postlist); //전체 조회
        model.addAttribute("previous", pageable.previousOrFirst().getPageNumber());
        model.addAttribute("next", pageable.next().getPageNumber());
        model.addAttribute("checkNextPage", postsService.getNextPageCheck(SearchType.ALL, null, pageable)); //페이지가 더 있는지 확인하는 boolean
        model.addAttribute("checkPreviousPage", postsService.getPreviousPageCheck(SearchType.ALL, null, pageable));

        return "index";
    }


    @GetMapping("/auth/join")
    public String join() {
        UserSessionDto user = (UserSessionDto) httpSession.getAttribute("user");
        if (user == null) {
            return "/login/join";
        }
        return "redirect:/";
    }

    @PostMapping("/auth/joinProc")
    public String joinProc(@Valid UserDto userDto, Errors errors, Model model) {
        if (errors.hasErrors()) {
            model.addAttribute("userDto", userDto);//회원가입 실패시 입력 데이터 값을 유지

            //유효성 통과 못한 필드와 메시지를 핸들링
            Map<String, String> validatorResult = userService.validateHandling(errors);
            for (String key : validatorResult.keySet()) {
                model.addAttribute(key, validatorResult.get(key));
            }
            return "/login/join";
        }
        userService.join(userDto);
        return "redirect:/auth/login";
    }

    @GetMapping("/auth/login")
    public String login(@RequestParam(value = "error", required = false)String error,
                        @RequestParam(value = "exception", required = false) String exception,
                        Model model) {
        model.addAttribute("error", error);
        model.addAttribute("exception", exception);
        UserSessionDto user = (UserSessionDto) httpSession.getAttribute("user");//세션 조회
        if(user != null){
            return "redirect:/";
        }
        return "/login/login";

    }/*
문제: 아이디 맞고 비번 다르게 치면 로그인됨*/

    @PostMapping("/auth/loginProc")
    public String loginProc(UserDto userDto, Model model){
      customUserDetailsService.loadUserByUsername(userDto.getUsername()) ;//사용자 db에 있는지 확인
        UserSessionDto user = (UserSessionDto) httpSession.getAttribute("user"); //세션에서 user꺼내오기
        if(user != null){
            model.addAttribute("user", user.getUsername());
        }
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response){
        new SecurityContextLogoutHandler().logout(request,response, SecurityContextHolder.getContext().getAuthentication());
        return "redirect:/";
    }


}
