package com.jazasoft.hzdemo.repository;

import com.jazasoft.hzdemo.entity.Book;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

//  @Cacheable(value = "query.book.findAllByCategory", key = "#category")
  List<Book> findAllByCategory(String category);
}
