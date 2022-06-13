package com.example.boardv.web;

import com.example.boardv.config.auth.dto.General.UserDto;
import com.example.boardv.config.auth.dto.General.UserSessionDto;
import com.example.boardv.config.auth.service.General.CustomUserDetailsService;
import com.example.boardv.config.auth.service.General.UserService;
import com.example.boardv.dto.PostListResponseDto;
import com.example.boardv.service.PostsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;


@RequiredArgsConstructor
@Controller
public class IndexController {
    private final UserService userService;
    private final CustomUserDetailsService customUserDetailsService;
    private final HttpSession httpSession;
    private final PostsService postsService;

/*    @GetMapping("/")
    public String index(Model model   ){

          UserSessionDto user = (UserSessionDto) httpSession.getAttribute("user");
        if(user != null)
        {
            model.addAttribute("user", user.getUsername());
        }
        model.addAttribute("posts", postsService.findAllDesc());

        return "index";
    }*/
    @GetMapping("/")
    public String index(Model model,Pageable pageable){

        UserSessionDto user = (UserSessionDto) httpSession.getAttribute("user");
        if(user != null)
        {
            model.addAttribute("user", user.getUsername());
        }
        model.addAttribute("posts", postsService.findAllDesc(pageable));

        return "index";
    }

    @GetMapping("/auth/join")
    public String join() {
       return "/login/join";
    }
    @PostMapping("/auth/joinProc")
    public String joinProc(UserDto userDto){
            userService.join(userDto);
        return "redirect:/auth/login";
    }

    @GetMapping("/auth/login")
    public String login(){
        return "/login/login";
    }

    @PostMapping("/auth/loginProc")
    public String loginProc(String username, Model model){
        customUserDetailsService.loadUserByUsername(username); //사용자 db에 있는지 확인
        UserSessionDto user = (UserSessionDto) httpSession.getAttribute("user"); //세션에서 user꺼내오기
        if(user != null){
            model.addAttribute("user", user.getUsername());
        }
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response){
        new SecurityContextLogoutHandler().logout(request,response, SecurityContextHolder.getContext().getAuthentication());
        return "redirecte:/";
    }


}
