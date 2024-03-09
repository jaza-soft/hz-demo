package com.jazasoft.hzdemo.service;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.flakeidgen.FlakeIdGenerator;
import com.hazelcast.map.IMap;
import com.hazelcast.query.Predicate;
import com.hazelcast.query.PredicateBuilder;
import com.hazelcast.query.Predicates;
import com.jazasoft.hz.mapstore.entity.Book;
import com.jazasoft.hz.mapstore.key.BookKey;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class CachedBookService {
  private final IMap<BookKey, Book> bookCache;
  private final FlakeIdGenerator idGenerator;

  public CachedBookService(HazelcastInstance hazelcast) {
    this.bookCache = hazelcast.getMap(Book.class.getSimpleName());
    this.idGenerator = hazelcast.getFlakeIdGenerator("default");
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
    book.setId(idGenerator.newId());
    bookCache.put(book.key(), book);
    return bookCache.get(book.key());
  }

  public Book update(Book book) {
    Book mBook = bookCache.get(book.key());
    mBook.setName(book.getName());
    mBook.setCategory(book.getCategory());
    mBook.setAuthorId(book.getAuthorId());
    bookCache.put(book.key(), mBook);
    return bookCache.get(book.key());
  }

  public void evict() {
    bookCache.evictAll();
  }

  public void delete(Long id, String category) {
    bookCache.remove(new BookKey(id, category));
  }
}
