package com.example.boardv.domain.web;

import com.example.boardv.config.auth.domain.user.General.User;
import com.example.boardv.config.auth.dto.General.UserSessionDto;
import com.example.boardv.dto.PostsSaveRequestDto;
import com.example.boardv.dto.PostsUpdateRequestDto;
import com.example.boardv.service.PostsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Map;

@RequiredArgsConstructor
@Controller
public class PostsApiController {
    private final PostsService postsService;
    private final HttpSession httpSession;

    @GetMapping("/posts/search") // keyword에 따라 쿼리문 작성시, uri주소가 이렇게 시작하기 때문에, 기본 화면("/")이랑 나눠서 진행
    public String searchIndex(@RequestParam(value = "searchType") SearchType searchType,
                              @RequestParam(value = "keyword")String keyword, Model model,
                              @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable){
        UserSessionDto user = (UserSessionDto) httpSession.getAttribute("user");//세션 조회
        if(user != null) { //header에서 로그인 된 화면 보여줘야하니까
            model.addAttribute("user", user.getUsername());
        }
        model.addAttribute("searchList",postsService.search(searchType,keyword, pageable)); //검색어에 따른 조회
        model.addAttribute("previous", pageable.previousOrFirst().getPageNumber());
        model.addAttribute("next", pageable.next().getPageNumber());
        model.addAttribute("checkNextPage", postsService.getNextPageCheck(searchType, keyword, pageable)); //페이지가 더 있는지 확인하는 boolean
        model.addAttribute("checkPreviousPage", postsService.getPreviousPageCheck(searchType,keyword,pageable));
        model.addAttribute("keyword", keyword);
        model.addAttribute("searchType", searchType);
        return "searchIndex";
    }

    @GetMapping("/posts/{id}") //게시글 하나 보여줄 때
    public String findById(@PathVariable Long id, Model model) {
        model.addAttribute("posts", postsService.findById(id));

        UserSessionDto user = (UserSessionDto) httpSession.getAttribute("user");
        if(user != null) { //header에서 로그인 된 화면 보여줘야하니까
            model.addAttribute("user", user.getUsername());

            if (postsService.findById(id).getAuthor().equals(user.getUsername())) { //세션정보와 해당 게시글의 작성자가 같으면
                model.addAttribute("oauthor", user.getUsername()); // 해당 게시글의 수정,삭제 버튼 추가
            }
        }
        return "/post/index";

    }

    @GetMapping("/posts/save") //게시글 저장 시, 사용자 정보를 세션 정보 이용하여 저장
    public String postsSave(Model model){
        UserSessionDto user = (UserSessionDto) httpSession.getAttribute("user");
        if(user != null)
        {
            model.addAttribute("user", user.getUsername());
            return "/post/save";
        }
       return "redirect:/auth/login";
    }

    //중복검사같은것도 필요없으니까, 공백일 경우만 체크해서 표시되게


    @PostMapping(value = "/posts/save", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String saveForFormRequest(@Valid PostsSaveRequestDto requestDto
        , Errors errors, Model model){
        UserSessionDto user = (UserSessionDto) httpSession.getAttribute("user");
        if(user != null)
        {
            model.addAttribute("user", user.getUsername());
        }
        if(errors.hasErrors()){
            model.addAttribute("requestDto", requestDto);
            Map<String, String> validatorResult = postsService.validateHandling(errors);
            for(String key: validatorResult.keySet()){
                System.out.println(key);
                model.addAttribute(key,validatorResult.get(key));
            }
            return "/post/save";
        }
        postsService.save(requestDto);
        return "redirect:/";
    }

    @GetMapping("/posts/update/{id}")
    public String update(@PathVariable Long id, Model model){ //글 번호 보여줘야하니까 model에 담아서 post 정보 넘겨줌
        UserSessionDto user= (UserSessionDto) httpSession.getAttribute("user");
        if(user !=null){ // header에서 보여줘야하니까
            model.addAttribute("user",user.getUsername());
            model.addAttribute("posts", postsService.findById(id));
            if(postsService.findById(id).getAuthor().equals(user.getUsername())){
                return "/post/update";
            }
        }

        return "redirect:/auth/login";
    }

    //id로 작성자 찾고, 그거랑 user비교하기기

   @PutMapping(value = "/posts/update/{id}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String updateForFormRequest(@PathVariable Long id,
                                       @Valid PostsUpdateRequestDto requestDto, Errors errors, Model model){
        UserSessionDto user= (UserSessionDto) httpSession.getAttribute("user");

        if(user !=null){ // header에서 보여줘야하니까
            model.addAttribute("user",user.getUsername());
        }
        model.addAttribute("posts", postsService.findById(id));

        if(errors.hasErrors()){
            model.addAttribute("requestDto", requestDto);
            Map<String, String> validatorResult = postsService.validateHandling(errors);
            for(String key: validatorResult.keySet()){
                System.out.println(key);
                model.addAttribute(key,validatorResult.get(key));
            }
            return "/post/update";
        }
        postsService.update(id,requestDto);
        return "redirect:/posts/{id}";
    }

    @DeleteMapping("/posts/delete/{id}")
    public String deleteById(@PathVariable Long id){
        postsService.deleteById(id);
        return "redirect:/";
    }


    /* @PostMapping(value = "/api/v1/posts", consumes = MediaType.APPLICATION_JSON_VALUE)//JSon 타입으로 데이터 교환할 때
   public Long saveForJsonRequest(@RequestBody PostsSaveRequestDto requestDto){
      return postsService.save(requestDto);
   }
    @PutMapping(value = "/api/v1/posts/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
   public String updateForJsonRequest(@PathVariable Long id, @RequestBody PostsUpdateRequestDto requestDto){
        postsService.update(id,requestDto);
       return "redirect:/";
    }
*/
}
