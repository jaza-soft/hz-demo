package com.jazasoft.hzdemo.repository;

import com.jazasoft.hzdemo.entity.Author;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import javax.persistence.QueryHint;

@Repository
public interface AuthorRepository extends CrudRepository<Author, Long> {
  @Override
  @NonNull
  @QueryHints({
      @QueryHint(name = org.hibernate.jpa.QueryHints.HINT_CACHEABLE, value = "true"),
      @QueryHint(name = org.hibernate.jpa.QueryHints.HINT_CACHE_REGION, value = "author.findAll")
  })
  Iterable<Author> findAll();
}
