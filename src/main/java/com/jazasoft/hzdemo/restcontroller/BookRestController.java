package com.jazasoft.hzdemo.restcontroller;

import com.jazasoft.hzdemo.entity.Book;
import com.jazasoft.hzdemo.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookRestController {
  private final BookService bookService;

  public BookRestController(BookService bookService) {
    this.bookService = bookService;
  }

  @GetMapping
  public ResponseEntity<?> findAll() {
    List<Book> books = bookService.findAll();
    books.forEach(this::sanitize);
    return ResponseEntity.ok(books);
  }

  @GetMapping("/search-by-category")
  public ResponseEntity<?> findAllByCategory(@RequestParam("category") String category) {
    List<Book> books = bookService.findAllByCategory(category);
    books.forEach(this::sanitize);
    return ResponseEntity.ok(books);
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> findOne(@PathVariable("id") Long id) {
    Book book = bookService.findOne(id);
    if (book == null) return ResponseEntity.notFound().build();
    sanitize(book);
    return ResponseEntity.ok(book);
  }

  @PostMapping
  public ResponseEntity<?> save(@RequestBody Book book) {
    Book mBook = bookService.save(book);
    sanitize(mBook);
    return ResponseEntity.ok(mBook);
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody Book book) {
    book.setId(id);
    Book mBook = bookService.update(book);
    sanitize(mBook);
    return ResponseEntity.ok(mBook);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteOne(@PathVariable("id") Long id) {
    bookService.delete(id);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/evict")
  public ResponseEntity<?> evict() {
    bookService.evict();
    return ResponseEntity.noContent().build();
  }

  private void sanitize(Book book) {
    if (book == null) return;
    if (book.getAuthor() != null) {
      book.getAuthor().setBookList(null);
    }
  }
}
