package com.jazasoft.hzdemo.mapstore;

import com.hazelcast.map.MapStore;
import com.hazelcast.map.PostProcessingMapStore;
import com.jazasoft.hzdemo.ApplicationContextUtil;
import com.jazasoft.hzdemo.entity.Book;
import com.jazasoft.hzdemo.entity.BookKey;
import com.jazasoft.hzdemo.repository.AuthorRepository;
import com.jazasoft.hzdemo.repository.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BookMapStore implements MapStore<BookKey, Book>, PostProcessingMapStore {
  private final Logger logger = LoggerFactory.getLogger(BookMapStore.class);
  private final BookRepository bookRepository;
  private final AuthorRepository authorRepository;

  public BookMapStore() {
    bookRepository = ApplicationContextUtil.getApplicationContext().getBean(BookRepository.class);
    authorRepository = ApplicationContextUtil.getApplicationContext().getBean(AuthorRepository.class);
  }

  @Override
  public void store(BookKey key, Book book) {
    logger.info("store: key = {}", key);
    if (book.getAuthorId() != null) {
      book.setAuthor(authorRepository.findById(book.getAuthorId()).orElse(null));
    }
    bookRepository.save(book);
  }

  @Override
  public void storeAll(Map<BookKey, Book> map) {
    logger.info("storeAll");
    bookRepository.saveAll(map.values());
  }

  @Override
  public void delete(BookKey key) {
    logger.info("delete: key = {}", key);
    bookRepository.deleteById(key.getId());
  }

  @Override
  public void deleteAll(Collection<BookKey> keys) {
    logger.info("deleteAll: keys = {}", keys);
    List<Long> ids = keys.stream().map(BookKey::getId).toList();
    bookRepository.deleteAllById(ids);
  }

  @Override
  public Book load(BookKey key) {
    logger.info("load: key = {}", key);
    return bookRepository.findById(key.getId()).orElse(null);
  }

  @Override
  public Map<BookKey, Book> loadAll(Collection<BookKey> keys) {
    logger.info("loadAll: keys = {}", keys);
    List<Long> ids = keys.stream().map(BookKey::getId).toList();
    List<Book> books = bookRepository.findAllById(ids);
    Map<BookKey, Book> bookMap = books.stream().collect(Collectors.toMap(Book::getKey, Function.identity()));
    logger.info("Final Keys: {}", bookMap.keySet());
    return bookMap;
  }

  @Override
  public Iterable<BookKey> loadAllKeys() {
    logger.info("loadAllKeys");
    return bookRepository.findAll().stream().map(Book::getKey).collect(Collectors.toList());
  }
}