package com.example.hello.news.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // @ToString, @EqualsAndHashCode와 @RequiredArgsConstructor 를 합쳐놓은 종합 선물 세트? 인터넷을 찾아보니 setter때문에 사용을 지양하는게 좋다는 말도 있다.
// setter 때문이라기보다는 많은 기능들이 포함되기 때문에 생각보다 무거울 수 있으나, Gson 연동등 호환성을 높이기 위해(런타임 에러를 가급적 억제) 그리고 혹시나 디버깅이 필요할 수 있어 여기서는 사용함
// Gson에 연동하는 테스트를 해본뒤 setter나 getter만으로 충분하다면 그렇게 하는게 좋은 방법임

@AllArgsConstructor // 모든 필드 값을 파라미터로 받는 생성자를 생성, 클래스의 모든 필드를 한 번에 초기화 가능.
@NoArgsConstructor // 파라미터가 없는 디폴트 생성자를 생성, 클래스에 명시적으로 선언된 생성자가 없어도 인스턴스 생성 가능.
public class ArticleDTO {
    private SourceDTO source;
    private String author;
    private String title;
    private String description;
    private String url;
    private String urlToImage;
    private String publishedAt;
    private String content;
}
