package com.example.boardv.web;

import com.example.boardv.dto.PostsResponseDto;
import com.example.boardv.dto.PostsSaveRequestDto;
import com.example.boardv.dto.PostsUpdateRequestDto;
import com.example.boardv.service.PostsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

//@RestController
@RequiredArgsConstructor
@Controller
public class PostsApiController {

    private final PostsService postsService;

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

    @PutMapping("/api/v1/posts/{id}")
    public Long update(@PathVariable Long id, @RequestBody PostsUpdateRequestDto requestDto){
        return postsService.update(id,requestDto);

    }

    @GetMapping("/api/v1/posts/{id}")
    public String findById(@PathVariable Long id, Model model){
         model.addAttribute("posts", postsService.findById(id));
         return "/post/index";
    }

    @DeleteMapping("/api/v1/posts/{id}")
    public void deleteById(@PathVariable Long id){
        postsService.deleteById(id);
    }

    @DeleteMapping("/api/v1/posts")
    public void deleteAll(){
        postsService.deleteAll();
    }

}
