package dev.andela.assessment1.controller;

import dev.andela.assessment1.entity.Article;
import dev.andela.assessment1.service.ArticleService;
import dev.andela.assessment1.service.IArticleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

/**
 * DONE - IN A NUTSHELL
 * 0- Design-first approach (Size of the resource & Role in the orchestration, Endpoint path, interface in/out object, )
 * 0.1- Spring IO initializer, (java, spring version, Library need Data-Source, Spring Web, JPA, Bean @Validator, Swagger-ui / OpenAPI-doc, ..., Spring Test, Security, )
 * 1.0- Align with REST best practices (e.g., using Location headers and proper status codes),
 * 1.1- Handle edge cases gracefully (e.g., non-existing resources or invalid input),
 * 1.2- Improve maintainability (e.g., better update logic and validation), @ControllerAdvice, @ExceptionHandler
 * 1.3- Scale effectively (e.g., add pagination).
 * 1.4- Add Swagger or OpenAPI annotations to generate API documentation. This will make it easier to use and share your endpoints.
 *
 * TODO
 * - why will be good to add DTO with record
 * 2- Ensure you have integration tests for each endpoint to validate behavior, including edge cases like invalid input or non-existing IDs
 * Q1- What is the differance btw @RestController and @Controller?
 */

@RestController
@RequestMapping("/articles")
public class ArticleController {

    private final IArticleService service;

    public ArticleController(ArticleService service) {
        this.service = service;
    }

    // For large datasets, consider adding pagination support to the getAll endpoint
    //@GetMapping
    //public ResponseEntity<List<Article>> getAllArticles() {
    //    List<Article> articles = service.getAll();
    //    return new ResponseEntity<>(articles, HttpStatus.OK);
    //}

    @Operation(summary = "Retrieve all articles with optional pagination", description = "Fetch a paginated list of articles. If no pagination parameters are provided, all articles will be returned.")
    @ApiResponse(responseCode = "200", description = "List of articles", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Article.class)))
    @GetMapping
    public ResponseEntity<List<Article>> getAllArticles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    )
    {
        Pageable pageable = PageRequest.of(page, size);
        Page<Article> articlesPage = service.getAll(pageable);
        return new ResponseEntity<>(articlesPage.getContent(), HttpStatus.OK);
    }


    @Operation(summary = "Get article by ID", description = "Retrieve a specific article by its unique ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Article found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Article.class))),
            @ApiResponse(responseCode = "404", description = "Article not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Article> getArticleById(@PathVariable int id) {
        Article article = service.findById(id);

        if (article != null) {
            return new ResponseEntity<>(article, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Create a new article", description = "Add a new article to the system.")
    @ApiResponse(responseCode = "201", description = "Article created successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Article.class))
    )
    @PostMapping
    public ResponseEntity<Article> createArticle(@Valid @RequestBody Article article) {
        Article createdArticle = service.add(article);

        //Best practice 1 missing - new ResponseEntity<>(createdArticle, HttpStatus.CREATED);
        // set Location in the header url to '/{id}'
        return ResponseEntity.created(URI.create( "/articles/" + createdArticle.getId() ))
                             .body(createdArticle);
    }


    @Operation(summary = "Update an article", description = "Update the details of an existing article.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Article updated successfully"),
            @ApiResponse(responseCode = "404", description = "Article not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateArticle(@PathVariable int id, @Valid @RequestBody Article article) {
        Article existingArticle = service.findById(id);

        if (existingArticle == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        article.setId(id);
        service.update(article);

        // but you could enhance consistency by including the updated article in the response
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Delete an article", description = "Remove an article from the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Article deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Article not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable int id) {
        Article existingArticle = service.findById(id);

        if (existingArticle == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        service.remove(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
