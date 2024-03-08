package com.jazasoft.hzdemo.service;

import com.jazasoft.hzdemo.entity.Book;
import com.jazasoft.hzdemo.repository.AuthorRepository;
import com.jazasoft.hzdemo.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class BookService {
  private final BookRepository bookRepository;
  public final AuthorRepository authorRepository;

  public BookService(BookRepository bookRepository, AuthorRepository authorRepository) {
    this.bookRepository = bookRepository;
    this.authorRepository = authorRepository;
  }

  public Book findOne(Long id) {
    return bookRepository.findById(id).orElse(null);
  }

  public Iterable<Book> findAll() {
    return bookRepository.findAll();
  }

  public Iterable<Book> findAllByCategory(String category) {
    return bookRepository.findAllByCategory(category);
  }

  @Transactional
  public Book save(Book book) {
    if (book.getAuthorId() != null) {
      book.setAuthor(authorRepository.findById(book.getAuthorId()).orElse(null));
    }
    return bookRepository.save(book);
  }

  @Transactional
  public Book update(Book book) {
    Book mBook = bookRepository.findById(book.getId()).orElseThrow();
    if (book.getAuthorId() != null) {
      mBook.setAuthor(authorRepository.findById(book.getAuthorId()).orElse(null));
    }
    mBook.setName(book.getName());
    mBook.setCategory(book.getCategory());
    return mBook;
  }

  @Transactional
  public void delete(Long id) {
    bookRepository.deleteById(id);
  }
}
