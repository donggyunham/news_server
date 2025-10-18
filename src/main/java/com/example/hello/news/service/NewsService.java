package com.example.hello.news.service;


import com.example.hello.news.dto.*;
import com.example.hello.news.entity.Article;
import com.example.hello.news.entity.Category;
import com.example.hello.news.entity.Source;
import com.example.hello.news.repository.ArticleRepository;
import com.example.hello.news.repository.CategoryRepository;
import com.example.hello.news.repository.SourceRepository;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@Service // 서비스임을 명시
@RequiredArgsConstructor
public class NewsService {

    @Value("${newsapi.source_url}")
    private String sourceURL;

    @Value("${newsapi.apikey}")
    private String apiKey;

    @Value("${newsapi.article_url}")
    private String articleUrl;

    private final CategoryRepository categoryRepository;
    private final SourceRepository sourceRepository;
    private final ArticleRepository articleRepository;
    /*
    * @RequiredArgsConstructor
    public class NewsService {

    private final CategoryRepository categoryRepository;
    * }
    *
    * 위아래는 같은거라고 생각하면된다.
    *
    * @Autowired
    * private CategoryRepository categoryRepository;
    * */
    public NewsResponse getGeneral() throws URISyntaxException, IOException, InterruptedException {

        String url ="https://newsapi.org/v2/top-headlines?country=us&apiKey=a3b5b0243c714efea75778d097aed2ed";

        // HttpClient = http 클라이언트 api, 비동기 및 동기 방식의 http(s) 요청을 손쉽게 수행할 수 있게 해주는 클래스.
        HttpClient client = HttpClient.newBuilder().build();

        // 아래와 같은 형식으로 GET 방식으로 요청을 보낸다. 빌더에 대해서는 차주 설명을 한번 더 들어봐야될 듯 하다.
        HttpRequest request =HttpRequest.newBuilder()
                .uri(new URI(url))
                .GET()
                .build();

        // client로 request, 요청을 보내고 응답받은, response받은 내용들을 문자열로 가져온다.
        // 문자열로 받아올 것이기 때문에, <String>으로 명시를 해줘야 한다.
        HttpResponse<String> response =client.send(request, HttpResponse.BodyHandlers.ofString());

        // 문자열로 받아온 데이터를 resBody에 담는다.
        String resBody = response.body();

        // google에서 개발한 오픈소스 java라이브러리, 자바객체를 json 형식으로 쉽게 변환하거나,
        // json 데이터를 자바 객체로 변환할 수 있게 해줌.
        Gson gson = new Gson();

        // 위에서 resBody에 문자열로 받아온 내용을 gson을 통해 json데이터 형태로 변환해준다.
        // NewsResponse.class는 무엇을 의미하는가? ---> 클래스 자기 자신의 메타 정보라고 생각하면 좋을듯..
        // resBody로 받아온 문자열들을 NewsResponse에 만들어둔 형태로 변환을 해서 newsResponse에 담는 과정.
        NewsResponse newsResponse = gson.fromJson(resBody, NewsResponse.class);

        System.out.println(newsResponse.getStatus());

        // newsResponse를 리턴.
        return newsResponse;
    }

    public List<CategoryDTO> getCategories(){
        // categoryRepository.findAll() = select * from category ==> fetch 한다고 표현.
        //entity list
        List<Category> categories = categoryRepository.findAll();

        //dto list
        //비어있는 categorydto list인스턴스를 생성.
        List<CategoryDTO> categoryDTOList = new ArrayList<>();
        for (Category category : categories) {
            CategoryDTO dto = new CategoryDTO();
            dto.setName(category.getName());
            dto.setMemo(category.getMemo());
            categoryDTOList.add(dto);
        }
        return categoryDTOList;
        }

    public String inputCategory(Category category) {
        if(category != null){
            try{
                categoryRepository.save(category);
                //Category saved = categoryRepository.save(category);
                //saved.getName().equals(category.getName());
            }catch (Exception e){
                System.out.println( e.getMessage() );
                return String.format("ERROR: %s", e.getMessage());
            }

            return "SUCCESS";
        }
        return "ERROR: 카테고리 정보가 없습니다.";
    }

    public CategoryDTO updateCategory(String categoryId, String categoryName, String categoryMemo) {
        return null;
    }
}
