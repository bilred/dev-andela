package dev.andela.assessment1.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.Objects;

@Entity
@Schema(description = "Represents an article with a title, body, and ID.")
public class Article {
//    Title (String) - getTitle/setTitle
//    Body (String) - getBody/setBody
//    Id (int) - getId/setId

    @Schema(description = "Unique identifier of the article.", example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    @Schema(description = "Title of the article.", example = "Understanding REST APIs")
    @NotBlank
    private String title;

    @Schema(description = "Body content of the article.", example = "This article explains REST APIs in detail.")
    @NotBlank
    private String body;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Article article = (Article) o;
        return id == article.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
