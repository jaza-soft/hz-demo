package com.jazasoft.hzdemo.restcontroller;

import com.jazasoft.hzdemo.entity.Book;
import com.jazasoft.hzdemo.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
public class BookRestController {
  private final BookService bookService;

  public BookRestController(BookService bookService) {
    this.bookService = bookService;
  }

  @GetMapping
  public ResponseEntity<?> findAll() {
    return ResponseEntity.ok(bookService.findAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> findOne(@PathVariable("id") Long id) {
    Book book = bookService.findOne(id);
    if (book == null) return ResponseEntity.notFound().build();
    return ResponseEntity.ok(book);
  }

  @PostMapping
  public ResponseEntity<?> save(@RequestBody Book book) {
    Book mBook = bookService.save(book);
    return ResponseEntity.ok(mBook);
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody Book book) {
    book.setId(id);
    Book mBook = bookService.update(book);
    return ResponseEntity.ok(mBook);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteOne(@PathVariable("id") Long id) {
    bookService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
