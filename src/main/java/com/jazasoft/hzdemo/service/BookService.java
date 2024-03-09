package com.jazasoft.hzdemo.service;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.hazelcast.query.Predicate;
import com.hazelcast.query.PredicateBuilder;
import com.hazelcast.query.Predicates;
import com.jazasoft.hzdemo.entity.Book;
import com.jazasoft.hzdemo.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class BookService {
  private final BookRepository bookRepository;

  private final IMap<Long, Book> bookCache;

  public BookService(BookRepository bookRepository, HazelcastInstance hazelcast) {
    this.bookRepository = bookRepository;
    this.bookCache = hazelcast.getMap("book-cache");
  }

  public Book findOne(Long id) {
    return  bookCache.get(id);
  }

  public List<Book> findAll() {
    bookCache.loadAll(false);
    Collection<Book> books = bookCache.values();
    return new ArrayList<>(books);
  }

  @SuppressWarnings("unchecked")
  public List<Book> findAllByCategory(String category) {
    PredicateBuilder.EntryObject pb = Predicates.newPredicateBuilder().getEntryObject();
    Predicate<Long, Book> predicate = (Predicate<Long, Book>) pb.get("category").equal(category);
    return new ArrayList<>(bookCache.values(predicate));
  }


  @Transactional
  public Book save(Book book) {
    Long id = bookRepository.nextId();
    book.setId(id);
    bookCache.put(book.getId(), book);
    return bookCache.get(book.getId());
  }

  public Book update(Book book) {
    Book mBook = bookCache.get(book.getId());
    mBook.setName(book.getName());
    mBook.setCategory(book.getCategory());
    mBook.setAuthorId(book.getAuthorId());
    bookCache.put(book.getId(), mBook);
    return bookCache.get(book.getId());
  }

  public void evict() {
    bookCache.evictAll();
  }

  @Transactional
  public void delete(Long id) {
    bookCache.remove(id);
  }
}
