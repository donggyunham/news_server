package com.example.hello.news.entity;

import com.example.hello.news.dto.ArticleDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="article")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source", foreignKey = @ForeignKey(name = "article_ibfk_1"))
    private Source source;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category", foreignKey = @ForeignKey(name = "article_ibfk_2"))
    private Category category;

    @Column(length = 150)
    private String author;

    @Column(length = 500)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 500)
    private String url;

    @Column(name = "url_to_image", length = 500)
    private String urlToImage;

    @Column(name = "published_at", length = 100)
    private String publishedAt;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(name = "created_at",updatable = false, insertable = false)
    private String createdAt;

    @Column(name = "updated_at", insertable = false)
    private String updatedAt;

    public static Article fromDTO(ArticleDTO dto, Source src, Category cat){
        Article article = new Article();

        article.setSource(src);
        article.setCategory(cat);

        //article.setSource(dto.getSource());
        //article.setCategory(dto.get);
        article.setAuthor(dto.getAuthor());
        article.setTitle(dto.getTitle());
        article.setDescription(dto.getDescription());
        article.setUrl(dto.getUrl());
        article.setUrlToImage(dto.getUrlToImage());
        article.setPublishedAt(dto.getPublishedAt());
        article.setContent(dto.getContent());

        return article;
    }
}
