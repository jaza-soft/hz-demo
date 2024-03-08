package com.jazasoft.hzdemo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jazasoft.hzdemo.entity.Book;
import com.jazasoft.hzdemo.repository.AuthorRepository;
import com.jazasoft.hzdemo.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class BookService {
  private final BookRepository bookRepository;
  private final AuthorRepository authorRepository;

  private CacheManager cacheManager;
  private ObjectMapper objectMapper;

  public BookService(BookRepository bookRepository, AuthorRepository authorRepository) {
    this.bookRepository = bookRepository;
    this.authorRepository = authorRepository;
  }

  @Autowired
  public void setCacheManager(CacheManager cacheManager) {
    this.cacheManager = cacheManager;
  }

  @Autowired
  public void setObjectMapper(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  public Book findOne(Long id) {
    return bookRepository.findById(id).orElse(null);
  }

  public List<Book> findAll() {
    return bookRepository.findAll();
  }

  @Cacheable(value = "query.book.findAllByCategory", key = "#category")
  public List<Book> findAllByCategory(String category) {
    List<Book> books = bookRepository.findAllByCategory(category);
    try {
      books.forEach(book -> {
        if (book.getAuthor() != null) {
          book.getAuthor().setBookList(null);
        }
      });
      objectMapper.writer().writeValueAsString(books);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
    return books;
  }

  @Transactional
  public Book save(Book book) {
    if (book.getAuthorId() != null) {
      book.setAuthor(authorRepository.findById(book.getAuthorId()).orElse(null));
    }
    Book mBook = bookRepository.save(book);

    //** Evict Spring Cache for query matching this category **//
    Cache cache = cacheManager.getCache("query.book.findAllByCategory");
    if (cache != null) {
      cache.evictIfPresent(mBook.getCategory());
    }

    return mBook;
  }

  @Transactional
  public Book update(Book book) {
    Book mBook = bookRepository.findById(book.getId()).orElseThrow();
    if (book.getAuthorId() != null) {
      mBook.setAuthor(authorRepository.findById(book.getAuthorId()).orElse(null));
    }

    String prevCategory = mBook.getCategory();

    mBook.setName(book.getName());
    mBook.setCategory(book.getCategory());

    //** Evict Spring Cache for query matching this category **//
    Cache cache = cacheManager.getCache("query.book.findAllByCategory");
    if (cache != null) {
      cache.evictIfPresent(prevCategory);
      cache.evictIfPresent(mBook.getCategory());
    }

    return mBook;
  }

  @Transactional
  public void delete(Long id) {
    Book book = bookRepository.findById(id).orElse(null);
    if (book == null) return;
    bookRepository.delete(book);

    //** Evict Spring Cache for query matching this category **//
    Cache cache = cacheManager.getCache("query.book.findAllByCategory");
    if (cache != null) {
      cache.evictIfPresent(book.getCategory());
    }
  }
}
