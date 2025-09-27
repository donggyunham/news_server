package com.example.hello.news.service;


import com.example.hello.news.dto.CategoryDTO;
import com.example.hello.news.dto.NewsResponse;
import com.example.hello.news.dto.SourceDTO;
import com.example.hello.news.dto.SourceResponse;
import com.example.hello.news.entity.Category;
import com.example.hello.news.entity.Source;
import com.example.hello.news.repository.CategoryRepository;
import com.example.hello.news.repository.SourceRepository;
import com.google.gson.Gson;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

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

    // 현재 작업은 데이터베이스 일괄 처리 작업을 하는 것.
    // 트랜잭션은 데이터베이스에서 데이터 처리 작업 단위라고 보면 된다. 입력, 처리.
    @Transactional
    public void inputSources() throws URISyntaxException, IOException, InterruptedException {
        String url = sourceURL + apiKey;
        System.out.println(url);

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

        SourceResponse sourceResponse = gson.fromJson(resBody, SourceResponse.class);

        System.out.println(sourceResponse.getStatus());
        System.out.println(sourceResponse.getSources().length);

        // sourceResponse에 있는 모든 sourcDTO 인스턴스의 데이터를 이용하여 Source Entity 인스턴스를 생성하고 데이터베이스에 저장한다.
        //  SourceDTO ==> Source

        try {

            for (SourceDTO dto : sourceResponse.getSources()) {
                // 빈 Source Entity 인스턴스를 생성
                Source source = new Source();

                source.setSid(dto.getId());
                source.setName(dto.getName());
                source.setDescription(dto.getDescription());
                source.setUrl(dto.getUrl());
                source.setName(dto.getName());
                source.setCategory(dto.getCategory());
                source.setLanguage(dto.getLanguage());
                source.setCountry(dto.getCountry());

                sourceRepository.save(source);
            }
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<SourceDTO> getSources(){
        //데이터베이스로부터 Source Entity 리스트를 가져와서 모든 Source Entity 인스턴스를 SourceDTO 인스턴스로 변환하여 반환한다.
        List<Source> sources = sourceRepository.findAll();

        /*for(Source source : sources){
        }*/
        // ramda
        //return sources.stream().map(source -> Source.toDTO(source)).toList();
        // stream 메소드는 리스트 안의 모든 아이템들을 가져오는 메소드다. 반복가능한 형태로.
        // stream.foreach(Functional Interface 익명 클래스 -> 람다식)
        // stream.map()
        // for(Source source : sources){}
        // stream 대표 사용 두개. 괄호 안에는 둘다 인터페이스가 들어가야된다.(익명클래스를 괄호 안에 정의했다고 본다.)
        /*return sources.stream().map(source -> {
            return Source.toDTO(source);
        }).toList();*/
        /*foreach map 차이 : 전자는 반환하는 값이 없음. 변경해서 넣고 끝. 맵은 반환까지 해준다.*/
        /*그러면 인풋에서 포이치 쓴건 왜쓴건가 맵을쓰면 안되나?*/
        return sources.stream().map(Source::toDTO).toList();
    }

    public void inputArticles(String category) throws URISyntaxException, IOException, InterruptedException {
        String url = String.format("%scategory=%s&%s", articleUrl, category, apiKey);
        System.out.println(url);

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

        NewsResponse newsResponse = gson.fromJson(resBody, NewsResponse.class);

        System.out.println(newsResponse.getStatus());
        System.out.println(newsResponse.getTotalResults());

    }
}
