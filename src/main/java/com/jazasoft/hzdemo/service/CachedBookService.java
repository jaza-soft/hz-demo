package com.jazasoft.hzdemo.service;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.hazelcast.query.Predicate;
import com.hazelcast.query.PredicateBuilder;
import com.hazelcast.query.Predicates;
import com.jazasoft.hz.mapstore.entity.Book;
import com.jazasoft.hz.mapstore.key.BookKey;
import com.jazasoft.hzdemo.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class CachedBookService {
  private final BookRepository bookRepository;

  private final IMap<BookKey, Book> bookCache;

  public CachedBookService(BookRepository bookRepository, HazelcastInstance hazelcast) {
    this.bookRepository = bookRepository;
    this.bookCache = hazelcast.getMap(Book.class.getSimpleName());
  }

  public Book findOne(Long id, String category) {
    return  bookCache.get(new BookKey(id, category));
  }

  public List<Book> findAll() {
    bookCache.loadAll(false);
    Collection<Book> books = bookCache.values();
    return new ArrayList<>(books);
  }

  @SuppressWarnings("unchecked")
  public List<Book> findAllByCategory(String category) {
    PredicateBuilder.EntryObject pb = Predicates.newPredicateBuilder().getEntryObject();
    Predicate<BookKey, Book> predicate = (Predicate<BookKey, Book>) pb.get("category").equal(category);
    return new ArrayList<>(bookCache.values(predicate));
  }


  @Transactional
  public Book save(Book book) {
    Long id = bookRepository.nextId();
    book.setId(id);
    bookCache.put(book.getKey(), book);
    return bookCache.get(book.getKey());
  }

  public Book update(Book book) {
    Book mBook = bookCache.get(book.getKey());
    mBook.setName(book.getName());
    mBook.setCategory(book.getCategory());
    mBook.setAuthorId(book.getAuthorId());
    bookCache.put(book.getKey(), mBook);
    return bookCache.get(book.getKey());
  }

  public void evict() {
    bookCache.evictAll();
  }

  public void delete(Long id, String category) {
    bookCache.remove(new BookKey(id, category));
  }
}
