package com.jazasoft.hzdemo.repository;

import com.jazasoft.hzdemo.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import javax.persistence.QueryHint;
import java.util.List;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
  @Override
  @NonNull
  @QueryHints({
      @QueryHint(name = org.hibernate.jpa.QueryHints.HINT_CACHEABLE, value = "true"),
      @QueryHint(name = org.hibernate.jpa.QueryHints.HINT_CACHE_REGION, value = "author.findAll")
  })
  List<Author> findAll();
}