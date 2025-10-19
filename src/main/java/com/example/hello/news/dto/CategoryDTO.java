package com.example.hello.news.dto;

import lombok.*;

// POJO : Plain Old Java object

@Getter
@Setter
//매개변수가 없는 생성자
@NoArgsConstructor
public class CategoryDTO {
    private String id;
    private String name;
    private String memo;
}
