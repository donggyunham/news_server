package com.example.hello.news.controller;

import com.example.hello.news.dto.ArticleDTO;
import com.example.hello.news.dto.NewsResponse;
import com.example.hello.news.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.net.URISyntaxException;

// 컨트롤러 임을 명시
// Spring Framework에서 이 클래스가 Spring MVC에서 웹 요청을 처리하는 컨트롤러 역할을 한다는 표식
@Controller

// 객체 생성시 반드시 넣어야되도록 하는 어노테이션 (x)
// 롬복(Lombok)**에서 제공하는 어노테이션으로,
// final이나 @NonNull이 붙은 필드를 파라미터로 받는 생성자를 자동으로 생성해줌
// private final로 시작되는 Spring bean 클래스가 멤버로 있지 않으면 선언 안해도 됨
@RequiredArgsConstructor
public class NewsController {

    //@Autowired
    // 자동주입 어노테이션으로 주입가능하나, 변동 가능성이 있기 때문에 final(재할당 불가능)로 지정을 하고,
    // @RequiredArgsConstructor를 통해서 변동이없도록 지정. 근데 무엇의 변동이 없도록 지정하는지?
    // 여기선 NewsService 에서 가져오는 변수? 들의 값들이 변동이 없도록 지정하는 듯 하다.

    // final 로 지정되지 않은 인스턴스는 코드내에서 재사용될 가능성이 있음
    // newsService = new NewsService(); 등으로 다른 인스턴스로 바뀔 가능성이 있음
    // 이런 상황을 차단하고자 newsService 변수를 final로 설정함
    private final NewsService newsService;

    /*@RequestMapping("/news")
    public String newsHome(Model model, Pageable pageable) {
        try{
            NewsResponse newsResponse = newsService.getGeneral();
            model.addAttribute("news", newsResponse);
        } catch (URISyntaxException | IOException | InterruptedException e) {
            model.addAttribute("error", e.getMessage());
        }
        return "/news";
    }*/
    @RequestMapping("/news")
    public String newsHome(Model model, Pageable pageable) {
        try{
            Page<ArticleDTO> articles = newsService.getArticles(pageable);
            model.addAttribute("articles", articles);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "/news";
    }

    @RequestMapping("/")
    public String index(Model model){
        return "redirect:/news";
    }
}
