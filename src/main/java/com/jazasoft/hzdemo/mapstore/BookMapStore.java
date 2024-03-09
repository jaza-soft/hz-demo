package com.jazasoft.hzdemo.mapstore;

import com.hazelcast.map.MapStore;
import com.jazasoft.hzdemo.ApplicationContextUtil;
import com.jazasoft.hzdemo.entity.Book;
import com.jazasoft.hzdemo.repository.BookRepository;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BookMapStore implements MapStore<Long, Book> {
  private final BookRepository bookRepository;

  public BookMapStore() {
    bookRepository = ApplicationContextUtil.getApplicationContext().getBean(BookRepository.class);
  }

  @Override
  public void store(Long id, Book book) {
    bookRepository.save(book);
  }

  @Override
  public void storeAll(Map<Long, Book> map) {
    bookRepository.saveAll(map.values());
  }

  @Override
  public void delete(Long id) {
    bookRepository.deleteById(id);
  }

  @Override
  public void deleteAll(Collection<Long> ids) {
    bookRepository.deleteAllById(ids);
  }

  @Override
  public Book load(Long id) {
    return bookRepository.findById(id).orElse(null);
  }

  @Override
  public Map<Long, Book> loadAll(Collection<Long> ids) {
    List<Book> books = bookRepository.findAllById(ids);
    return books.stream().collect(Collectors.toMap(Book::getId, Function.identity()));
  }

  @Override
  public Iterable<Long> loadAllKeys() {
    return bookRepository.findAll().stream().map(Book::getId).collect(Collectors.toList());
  }
}