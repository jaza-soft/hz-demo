package com.jazasoft.hzdemo.repository;

import com.jazasoft.hzdemo.entity.Book;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import javax.persistence.QueryHint;

@Repository
public interface BookRepository extends CrudRepository<Book, Long> {
  @Override
  @NonNull
  @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
  Iterable<Book> findAll();
}
