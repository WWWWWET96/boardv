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
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Controller
public class IndexController {
    private final UserService userService;
    private final CustomUserDetailsService customUserDetailsService;
    private final HttpSession httpSession;
    private final PostsService postsService;
    @GetMapping("/")
    public String index(Model model,
                        @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable){

        UserSessionDto user = (UserSessionDto) httpSession.getAttribute("user");//세션 조회
        if(user != null)
        {
            model.addAttribute("user", user.getUsername());
        }
        Page<PostListResponseDto> postlist = postsService.search(SearchType.ALL,null,pageable);
      /* Stream<Object> responseDtoStream= postlist.stream().map(postListResponseDto -> postListResponseDto.getModifiedDate()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:SSS")));
        List<String> responseStringList = responseDtoStream.map(Objects::toString).collect(Collectors.toList());

        String[] dateList = new String[]{"modifiedDate", String.valueOf(responseStringList)};*/

        model.addAttribute("posts", postlist); //전체 조회
        model.addAttribute("previous", pageable.previousOrFirst().getPageNumber());
        model.addAttribute("next", pageable.next().getPageNumber());
        model.addAttribute("checkNextPage", postsService.getNextPageCheck(SearchType.ALL, null, pageable)); //페이지가 더 있는지 확인하는 boolean
        model.addAttribute("checkPreviousPage", postsService.getPreviousPageCheck(SearchType.ALL,null,pageable));

        return "index";
    }


    @GetMapping("/auth/join")
    public String join(Model model) {
        UserSessionDto user = (UserSessionDto) httpSession.getAttribute("user");
        if(user == null){
            return "/login/join";
        }
        model.getAttribute("email");
        model.addAttribute("username");
       return "redirect:/";
    }

    @PostMapping("/auth/joinProc")
    public String joinProc(UserDto userDto) {
        if((userService.joinCheckByEmailDuplicate(userDto)==false) &&
                (userService.joinCheckByUsernameDuplicate(userDto) ==false)){ //아이디나 이메일이 중복되면 경우
            userService.join(userDto);
            return"redirect:/auth/login";
            }

        return "redirect:/auth/join";
    }

    @GetMapping("/auth/login")
    public String login(){
        UserSessionDto user = (UserSessionDto) httpSession.getAttribute("user");//세션 조회
        if(user == null){
            return "/login/login";
        }
        return "redirect:/";

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
        return "redirect:/";
    }


}
