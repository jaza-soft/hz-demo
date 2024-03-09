package com.jazasoft.hzdemo.service;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.flakeidgen.FlakeIdGenerator;
import com.hazelcast.map.IMap;
import com.jazasoft.hz.entity.HAuthor;
import com.jazasoft.hzdemo.entity.Author;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class CachedAuthorService {
  public final IMap<Long, HAuthor> authorCache;
  private final FlakeIdGenerator idGenerator;

  public CachedAuthorService(HazelcastInstance hazelcastInstance) {
    this.authorCache = hazelcastInstance.getMap(Author.class.getSimpleName());
    this.idGenerator = hazelcastInstance.getFlakeIdGenerator("default");
  }

  public HAuthor findOne(Long id) {
    return authorCache.get(id);
  }

  public List<HAuthor> findAll() {
    authorCache.loadAll(false);
    Collection<HAuthor> authors = authorCache.values();
    return new ArrayList<>(authors);
  }

  public HAuthor save(HAuthor author) {
    author.setId(idGenerator.newId());
    authorCache.put(author.getId(), author);
    return authorCache.get(author.getId());
  }

  public HAuthor update(HAuthor author) {
    HAuthor mAuthor = authorCache.get(author.getId());
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
