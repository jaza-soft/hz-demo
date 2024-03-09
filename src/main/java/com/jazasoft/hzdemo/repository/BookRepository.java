package com.jazasoft.hzdemo.repository;

import com.jazasoft.hzdemo.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

  @Query(value = "select nextval('book_seq')", nativeQuery = true)
  Long nextId();

  List<Book> findAllByCategory(String category);
}
