package com.jazasoft.hzdemo.service;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.jazasoft.hz.mapstore.entity.Author;
import com.jazasoft.hzdemo.repository.AuthorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class CachedAuthorService {
  private final AuthorRepository authorRepository;
  public final IMap<Long, Author> authorCache;

  public CachedAuthorService(AuthorRepository authorRepository, HazelcastInstance hazelcastInstance) {
    this.authorRepository = authorRepository;
    this.authorCache = hazelcastInstance.getMap(Author.class.getSimpleName());
  }

  public Author findOne(Long id) {
    return authorCache.get(id);
  }

  public List<Author> findAll() {
    authorCache.loadAll(false);
    Collection<Author> authors = authorCache.values();
    return new ArrayList<>(authors);
  }

  @Transactional
  public Author save(Author author) {
    Long id = authorRepository.nextId();
    author.setId(id);
    authorCache.put(author.getId(), author);
    return authorCache.get(author.getId());
  }

  @Transactional
  public Author update(Author author) {
    Author mAuthor = authorCache.get(author.getId());
    mAuthor.setName(author.getName());
    authorCache.put(author.getId(), mAuthor);
    return authorCache.get(author.getId());
  }

  public void evict() {
    authorCache.evictAll();
  }

  public void delete(Long id) {
    authorCache.remove(id);
  }
}
