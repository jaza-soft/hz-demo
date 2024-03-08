package com.jazasoft.hzdemo.restcontroller;

import com.jazasoft.hzdemo.entity.Author;
import com.jazasoft.hzdemo.service.AuthorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/authors")
public class AuthorRestController {
  private final AuthorService authorService;

  public AuthorRestController(AuthorService authorService) {
    this.authorService = authorService;
  }

  @GetMapping
  public ResponseEntity<?> findAll() {
    Iterable<Author> authors = authorService.findAll();
    authors.forEach(this::sanitize);
    return ResponseEntity.ok(authors);
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> findOne(@PathVariable("id") Long id) {
    Author author = authorService.findOne(id);
    if (author == null) return ResponseEntity.notFound().build();
    sanitize(author);
    return ResponseEntity.ok(author);
  }

  @PostMapping
  public ResponseEntity<?> save(@RequestBody Author author) {
    Author mAuthor = authorService.save(author);
    sanitize(mAuthor);
    return ResponseEntity.ok(mAuthor);
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody Author author) {
    author.setId(id);
    Author mAuthor = authorService.update(author);
    sanitize(mAuthor);
    return ResponseEntity.ok(mAuthor);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteOne(@PathVariable("id") Long id) {
    authorService.delete(id);
    return ResponseEntity.noContent().build();
  }

  private void sanitize(Author author) {
    if (author == null) return;
    author.getBookList().forEach(book -> book.setAuthor(null));
  }
}
