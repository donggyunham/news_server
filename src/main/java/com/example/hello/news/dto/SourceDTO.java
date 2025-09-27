package com.example.hello.news.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // @ToString, @EqualsAndHashCode와 @RequiredArgsConstructor 를 합쳐놓은 종합 선물 세트? 인터넷을 찾아보니 setter때문에 사용을 지양하는게 좋다는 말도 있다.
@AllArgsConstructor // 모든 필드 값을 파라미터로 받는 생성자를 생성, 클래스의 모든 필드를 한 번에 초기화 가능.
@NoArgsConstructor // 파라미터가 없는 디폴트 생성자를 생성, 클래스에 명시적으로 선언된 생성자가 없어도 인스턴스 생성 가능.
public class SourceDTO {
    private String id;
    private String name;
    private String description;
    private String url;
    private String category;
    private String language;
    private String country;
}
