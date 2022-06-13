package com.example.boardv.web;

import com.example.boardv.config.auth.dto.General.UserSessionDto;
import com.example.boardv.dto.PostsResponseDto;
import com.example.boardv.dto.PostsSaveRequestDto;
import com.example.boardv.dto.PostsUpdateRequestDto;
import com.example.boardv.service.PostsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

//@RestController
@RequiredArgsConstructor
@Controller
public class PostsApiController {

    private final PostsService postsService;
    private final HttpSession httpSession;

    @GetMapping("/posts/save") //게시글 저장 시, 사용자 정보를 세션 정보 이용하여 저장
    public String postsSave(Model model){
        UserSessionDto user = (UserSessionDto) httpSession.getAttribute("user");
        if(user != null)
        {
            model.addAttribute("user", user.getUsername());
        }
        return "/post/save";
    }
    @PostMapping(value = "/api/v1/posts", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String saveForJsonRequest(@RequestBody PostsSaveRequestDto requestDto){
        System.out.println(requestDto.toString());
        postsService.save(requestDto);
        return "redirect:/";
    }
    @PostMapping(value = "/api/v1/posts", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String saveForFormRequest(PostsSaveRequestDto requestDto){
        System.out.println(requestDto.toString());
        postsService.save(requestDto);
        return "redirect:/";
    }
    @GetMapping("/api/v1/posts/{id}") //게시글 하나 보여줄 때
    public String findById(@PathVariable Long id, Model model){
        model.addAttribute("posts", postsService.findById(id));

        UserSessionDto user = (UserSessionDto) httpSession.getAttribute("user");
        if(user != null){
            model.addAttribute("user", user.getUsername());

            if(postsService.findById(id).getAuthor().equals(user.getUsername())){ //세션정보와 해당 게시글의 작성자가 같으면
                model.addAttribute("oauthor", user.getUsername());
                System.out.println(model.getAttribute("oauthor"));
            }
        }

        return "/post/index";
    }
    @GetMapping("/api/v1/posts/update/{id}")
    public String update(@PathVariable Long id, Model model){ //글 번호 보여줘야하니까 model에 담아서 post 정보 넘겨줌
        model.addAttribute("posts", postsService.findById(id));
        return "/post/update";
    }
    @PutMapping(value = "/api/v1/posts/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String updateForJsonRequest(@PathVariable Long id, @RequestBody PostsUpdateRequestDto requestDto){
         postsService.update(id,requestDto);
        return "redirect:/";
    }

    @PutMapping(value = "/api/v1/posts/update/{id}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String updateForFormRequest(@PathVariable Long id, PostsUpdateRequestDto requestDto){
        postsService.update(id,requestDto);
        return "redirect:/";
    }


    @DeleteMapping("/api/v1/posts/delete/{id}")
    public String deleteById(@PathVariable Long id){
        postsService.deleteById(id);
        return "redirect:/";
    }

    @DeleteMapping("/api/v1/posts/deleteAll")
    public void deleteAll(){
        postsService.deleteAll();
    }

}
