package dev.andela.assessment1.repository;

//CrudRepository
//PagingAndSortingRepository
//JpaRepository

import dev.andela.assessment1.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Integer> {

}
