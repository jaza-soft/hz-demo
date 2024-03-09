package com.jazasoft.hzdemo.service;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.flakeidgen.FlakeIdGenerator;
import com.hazelcast.map.IMap;
import com.hazelcast.query.Predicate;
import com.hazelcast.query.PredicateBuilder;
import com.hazelcast.query.Predicates;
import com.jazasoft.hz.entity.HBook;
import com.jazasoft.hz.key.BookKey;
import com.jazasoft.hzdemo.entity.Book;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class CachedBookService {
  private final IMap<BookKey, HBook> bookCache;
  private final FlakeIdGenerator idGenerator;

  public CachedBookService(HazelcastInstance hazelcast) {
    this.bookCache = hazelcast.getMap(Book.class.getSimpleName());
    this.idGenerator = hazelcast.getFlakeIdGenerator("default");
  }

  public HBook findOne(Long id, String category) {
    return  bookCache.get(new BookKey(id, category));
  }

  public List<HBook> findAll() {
    bookCache.loadAll(false);
    Collection<HBook> books = bookCache.values();
    return new ArrayList<>(books);
  }

  @SuppressWarnings("unchecked")
  public List<HBook> findAllByCategory(String category) {
    PredicateBuilder.EntryObject pb = Predicates.newPredicateBuilder().getEntryObject();
    Predicate<BookKey, HBook> predicate = (Predicate<BookKey, HBook>) pb.get("category").equal(category);
    return new ArrayList<>(bookCache.values(predicate));
  }

  public HBook save(HBook book) {
    book.setId(idGenerator.newId());
    bookCache.put(book.key(), book);
    return bookCache.get(book.key());
  }

  public HBook update(HBook book) {
    HBook mBook = bookCache.get(book.key());
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
