package dev.andela.assessment1.service;

import dev.andela.assessment1.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IArticleService {

    // will retrieve all articles stored
    //List<Article> getAll();
    Page<Article> getAll(Pageable pageable);

    // will use the id to find an article with the same id
    // if none is found, it will return null
    Article findById(int id);

    // stores a new article and assigns it a unique id
    Article add(Article article);

    // removes an article by its id
    void remove(int id);

    // takes an updated article and stores it
    void update(Article updated);

}
