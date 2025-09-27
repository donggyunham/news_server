package com.example.hello.news.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SourceResponse {
    private String status;
    private SourceDTO[] sources;
}
