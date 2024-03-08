package com.jazasoft.hzdemo.repository;

import com.jazasoft.hzdemo.entity.Author;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends CrudRepository<Author, Long> {
}
