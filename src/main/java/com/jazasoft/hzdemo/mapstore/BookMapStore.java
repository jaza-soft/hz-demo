package com.jazasoft.hzdemo.mapstore;

import com.hazelcast.map.MapStore;
import com.hazelcast.map.PostProcessingMapStore;
import com.jazasoft.hzdemo.ApplicationContextUtil;
import com.jazasoft.hzdemo.entity.Book;
import com.jazasoft.hzdemo.repository.AuthorRepository;
import com.jazasoft.hzdemo.repository.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BookMapStore implements MapStore<Long, Book>, PostProcessingMapStore {
  private final Logger logger = LoggerFactory.getLogger(BookMapStore.class);
  private final BookRepository bookRepository;
  private final AuthorRepository authorRepository;

  public BookMapStore() {
    bookRepository = ApplicationContextUtil.getApplicationContext().getBean(BookRepository.class);
    authorRepository = ApplicationContextUtil.getApplicationContext().getBean(AuthorRepository.class);
  }

  @Override
  public void store(Long id, Book book) {
    logger.info("store: id = {}", id);
    if (book.getAuthorId() != null) {
      book.setAuthor(authorRepository.findById(book.getAuthorId()).orElse(null));
    }
    bookRepository.save(book);
  }

  @Override
  public void storeAll(Map<Long, Book> map) {
    logger.info("storeAll");
    bookRepository.saveAll(map.values());
  }

  @Override
  public void delete(Long id) {
    logger.info("delete: id = {}", id);
    bookRepository.deleteById(id);
  }

  @Override
  public void deleteAll(Collection<Long> ids) {
    logger.info("deleteAll: ids = {}", ids);
    bookRepository.deleteAllById(ids);
  }

  @Override
  public Book load(Long id) {
    logger.info("load: id = {}", id);
    return bookRepository.findById(id).orElse(null);
  }

  @Override
  public Map<Long, Book> loadAll(Collection<Long> ids) {
    logger.info("loadAll: ids = {}", ids);
    List<Book> books = bookRepository.findAllById(ids);
    return books.stream().collect(Collectors.toMap(Book::getId, Function.identity()));
  }

  @Override
  public Iterable<Long> loadAllKeys() {
    logger.info("loadAllKeys");
    return bookRepository.findAll().stream().map(Book::getId).collect(Collectors.toList());
  }
}