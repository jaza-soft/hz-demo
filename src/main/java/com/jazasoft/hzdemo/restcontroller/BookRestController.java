package com.jazasoft.hzdemo.restcontroller;

import com.jazasoft.hz.entity.HBook;
import com.jazasoft.hzdemo.service.CachedBookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookRestController {
  private final CachedBookService cachedBookService;

  public BookRestController(CachedBookService cachedBookService) {
    this.cachedBookService = cachedBookService;
  }

  @GetMapping
  public ResponseEntity<?> findAll() {
    List<HBook> books = cachedBookService.findAll();
    books.forEach(this::sanitize);
    return ResponseEntity.ok(books);
  }

  @GetMapping("/search-by-category")
  public ResponseEntity<?> findAllByCategory(@RequestParam("category") String category) {
    List<HBook> books = cachedBookService.findAllByCategory(category);
    books.forEach(this::sanitize);
    return ResponseEntity.ok(books);
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> findOne(@PathVariable("id") Long id, @RequestParam("category") String category) {
    HBook book = cachedBookService.findOne(id, category);
    if (book == null) return ResponseEntity.notFound().build();
    sanitize(book);
    return ResponseEntity.ok(book);
  }

  @PostMapping
  public ResponseEntity<?> save(@RequestBody HBook book) {
    HBook mBook = cachedBookService.save(book);
    sanitize(mBook);
    return ResponseEntity.ok(mBook);
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody HBook book) {
    book.setId(id);
    HBook mBook = cachedBookService.update(book);
    sanitize(mBook);
    return ResponseEntity.ok(mBook);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteOne(@PathVariable("id") Long id, @RequestParam("category") String category) {
    cachedBookService.delete(id, category);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/evict")
  public ResponseEntity<?> evict() {
    cachedBookService.evict();
    return ResponseEntity.noContent().build();
  }

  private void sanitize(HBook book) {
    if (book == null) return;
//    if (book.getAuthor() != null) {
//      book.getAuthor().setBookList(null);
//    }
  }
}
