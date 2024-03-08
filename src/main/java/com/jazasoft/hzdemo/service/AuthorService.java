package com.jazasoft.hzdemo.service;

import com.jazasoft.hzdemo.entity.Author;
import com.jazasoft.hzdemo.repository.AuthorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AuthorService {
  private final AuthorRepository authorRepository;

  public AuthorService(AuthorRepository authorRepository) {
    this.authorRepository = authorRepository;
  }

  public Author findOne(Long id) {
    return authorRepository.findById(id).orElse(null);
  }

  public Iterable<Author> findAll() {
    return authorRepository.findAll();
  }

  @Transactional
  public Author save(Author author) {
    return authorRepository.save(author);
  }

  @Transactional
  public Author update(Author author) {
    Author mAuthor = authorRepository.findById(author.getId()).orElseThrow();
    mAuthor.setName(author.getName());
    return mAuthor;
  }

  @Transactional
  public void delete(Long id) {
    authorRepository.deleteById(id);
  }
}
