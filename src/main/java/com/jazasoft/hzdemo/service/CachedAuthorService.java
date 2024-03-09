package com.jazasoft.hzdemo.service;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.flakeidgen.FlakeIdGenerator;
import com.hazelcast.map.IMap;
import com.jazasoft.hz.mapstore.entity.Author;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class CachedAuthorService {
  public final IMap<Long, Author> authorCache;
  private final FlakeIdGenerator idGenerator;

  public CachedAuthorService(HazelcastInstance hazelcastInstance) {
    this.authorCache = hazelcastInstance.getMap(Author.class.getSimpleName());
    this.idGenerator = hazelcastInstance.getFlakeIdGenerator("default");
  }

  public Author findOne(Long id) {
    return authorCache.get(id);
  }

  public List<Author> findAll() {
    authorCache.loadAll(false);
    Collection<Author> authors = authorCache.values();
    return new ArrayList<>(authors);
  }

  public Author save(Author author) {
    author.setId(idGenerator.newId());
    authorCache.put(author.getId(), author);
    return authorCache.get(author.getId());
  }

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
