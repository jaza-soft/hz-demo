package com.jazasoft.hzdemo.restcontroller;

import com.jazasoft.hz.entity.HAuthor;
import com.jazasoft.hzdemo.service.CachedAuthorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/authors")
public class AuthorRestController {
  private final CachedAuthorService cachedAuthorService;

  public AuthorRestController(CachedAuthorService cachedAuthorService) {
    this.cachedAuthorService = cachedAuthorService;
  }

  @GetMapping
  public ResponseEntity<?> findAll() {
    List<HAuthor> authors = cachedAuthorService.findAll();
    authors.forEach(this::sanitize);
    return ResponseEntity.ok(authors);
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> findOne(@PathVariable("id") Long id) {
    HAuthor author = cachedAuthorService.findOne(id);
    if (author == null) return ResponseEntity.notFound().build();
    sanitize(author);
    return ResponseEntity.ok(author);
  }

  @PostMapping
  public ResponseEntity<?> save(@RequestBody HAuthor author) {
    HAuthor mAuthor = cachedAuthorService.save(author);
    sanitize(mAuthor);
    return ResponseEntity.ok(mAuthor);
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody HAuthor author) {
    author.setId(id);
    HAuthor mAuthor = cachedAuthorService.update(author);
    sanitize(mAuthor);
    return ResponseEntity.ok(mAuthor);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteOne(@PathVariable("id") Long id) {
    cachedAuthorService.delete(id);
    return ResponseEntity.noContent().build();
  }

  private void sanitize(HAuthor author) {
    if (author == null) return;
  }

  @DeleteMapping("/evict")
  public ResponseEntity<?> evict() {
    cachedAuthorService.evict();
    return ResponseEntity.noContent().build();
  }
}
