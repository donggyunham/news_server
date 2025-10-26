package com.example.hello.news.controller;

import com.example.hello.news.dto.CategoryDTO;
import com.example.hello.news.dto.CountArticleByCategory;
import com.example.hello.news.dto.SourceByArticleDTO;
import com.example.hello.news.dto.SourceDTO;
import com.example.hello.news.entity.Category;
import com.example.hello.news.service.ArticleService;
import com.example.hello.news.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;

@Controller
@RequiredArgsConstructor
// localhost:8090/admin -> 해당 라우터 경로 아래로는 이 클래스에서 처리를 담당한다.
@RequestMapping("/admin")
public class AdminController {
    private final NewsService newsService;
    private final ArticleService articleService;

    /**
     * /admin/category reques를 처리하기 위한 함수
     * newsService로부터 카테고리 데이터 전체를 가져와서 model에 전달한다.
     * @param model : 템플릿에 전달할 데이터세트
     * @return
     */
    @GetMapping("/category")
    public String categories(Model model){
        // 데이터베이스로부터 카테고리 정보를 가져와서 admin의 category 페이지에 전달한다.
        List<CategoryDTO> categories = newsService.getCategories();
        model.addAttribute("category",categories);
        return "category"; // templates directory 아래에 있는 category.html을 렌더링해라.
    }

    // category_name으로부터 전달된 데이터를 데이터베이스에 저장하라는 request
    @PostMapping("/inputCategory")
    public String inputCategory(@RequestParam("category_name") String categoryName, Model model){
        /* 문자열 앞뒤의 공백을 없애주는게 트림이다. !categoryName.trim().isEmpty() => 앞뒤 공백을 없앤 데이터가 비어있지 않을 때 */
        if(categoryName != null && !categoryName.trim().isEmpty()){
            // 카테고리 이름 데이터가 정상적으로 전달되었음
            // 카테고리 엔티티 인스턴스를 생성
            Category category = new Category();
            // 네임 필드를 카테고리네임 값으로 설정
            category.setName(categoryName);

            String msg = newsService.inputCategory(category);
            if(msg != null && msg.startsWith("ERROR")){
                //저장하다가 에러가 발생한 경우
                model.addAttribute("ERROR", msg);

                List<CategoryDTO> categories = newsService.getCategories();
                model.addAttribute("category",categories);

                // 템플릿 폴더 아래에 있는 카테고리화면을 렌더링 해라.
                // 서버사이드 렌더링 => 순수 html code가 아닌 다른 것들을 포함해서 html과 조합해서 만들어낸다
                return "category";
            }
        }
        // request를 다시 만들어서 해당 request를 요청.
        return "redirect:/admin/category";
    }

    @PostMapping("/updateCategory/{id}")
    public String updateCategory(@PathVariable("id")String categoryId,
                                 @RequestParam("name")String categoryName,
                                 @RequestParam("memo")String categoryMemo,
                                 Model model){

        newsService.updateCategory(categoryId, categoryName, categoryMemo);
        return "redirect:/admin/category";
    }

    @PostMapping("deleteCategory/{id}")
    public String deleteCategory(@PathVariable String id, Model model){
        try{
            newsService.deleteCategory(id);
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "category";
        }
        return "redirect:/admin/category";
    }
    /*@GetMapping("/source")
    public String getSources(Model model){
        try {
            newsService.inputSources();
        }catch (URISyntaxException | IOException | InterruptedException|RuntimeException e){
            e.getStackTrace();
            model.addAttribute("ERROR", e.getMessage());
            return "source";
        }
        return "redirect:/admin/source";
    }*/
    @GetMapping("/source")
    public String getSources(Model model, Pageable pageable){
        Page<SourceDTO> sources = newsService.getSources(pageable);
        model.addAttribute("sources", sources);
        return "source";
    }


    @GetMapping("/inputSources")
    public String inputSources(Model model){
        try {
            articleService.inputSources();
        }catch (URISyntaxException | IOException | InterruptedException | RuntimeException e){
            e.getStackTrace();
            model.addAttribute("error", e.getMessage());
            return "source";
        }

        return "redirect:/admin/source";
    }

    @GetMapping("/article")
    public String article(Model model){
        // 카테고리 목록
        List<CategoryDTO> categories = newsService.getCategories();
        // 전체 기사 개수
        Long articleCount = articleService.getTotalArticleCount();
        // 카테고리 목록 별 기사 개수
        List<CountArticleByCategory> countByCategories = articleService.countArticleByCategories();

        // 소스별 기사들의 개수
        // 상위 10개의 정보들만 별도로 취합하고, 나머지 개수들을 별도로 구함.
        List<SourceByArticleDTO> sourceByArticles = articleService.getArticleCountBySource();
        // "count : ??"
        Long top10Sum = sourceByArticles.stream().mapToLong(SourceByArticleDTO::getCount).sum(); // 상위 10개 기사 개수
        Long etcCount = articleCount - top10Sum; // 기타 개수

        // 위에서 구한 데이터들을 템플릿에 전달
        model.addAttribute("articleCount", articleCount);
        model.addAttribute("countsByCategory", countByCategories);
        model.addAttribute("categories", categories);
        model.addAttribute("sourceByArticles", sourceByArticles);
        model.addAttribute("etcCount", etcCount);

        return "/article";
    }

    @PostMapping("/inputArticles")
    public String inputArticles(@RequestParam("categoryName") String category, Model model){
        try {
            articleService.inputArticles(category);
        }catch (URISyntaxException | IOException | InterruptedException|RuntimeException e){
            e.getStackTrace();
            model.addAttribute("ERROR", e.getMessage());
            return "article";
        }

        return "redirect:/admin/article";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model){
        HashMap<String, Long> counts = newsService.getRecordCount();
        model.addAttribute("counts", counts);
        return "dashboard";
    }

    @GetMapping("/")
    public String index(Model model){
        return "redirect:/admin/dashboard";
    }
}
