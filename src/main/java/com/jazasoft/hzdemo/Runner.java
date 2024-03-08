package com.jazasoft.hzdemo;

import com.hazelcast.core.DistributedObject;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.jazasoft.hzdemo.entity.Book;
import com.jazasoft.hzdemo.repository.BookRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;

//@Component
public class Runner implements CommandLineRunner {

  private final BookRepository bookRepository;

  public Runner(BookRepository bookRepository) {
    this.bookRepository = bookRepository;
  }

  @Override
  public void run(String... args) throws Exception {
    HazelcastInstance hazelcast = Hazelcast.getAllHazelcastInstances().stream().findAny().orElse(null);

    System.out.println(Book.class.getName());
//    IMap<Object, Object> iMap = Hazelcast.getAllHazelcastInstances().stream().findAny().orElseThrow().getMap(Book.class.getName());
//    System.out.println(iMap.size());
//
//    bookRepository.save(new Book(1L, "harry Potter"));
//
    while (true) {
      Thread.sleep(1000L);
      if (hazelcast != null) {
        Collection<DistributedObject> distributedObjects = hazelcast.getDistributedObjects();
        for (DistributedObject object : distributedObjects) {
          if (object instanceof IMap) {
            IMap map = hazelcast.getMap(object.getName());
            System.out.println("MapName=" + map.getName());
            map.keySet().forEach(key -> {
              System.out.println("Key="+key+",Value="+map.get(key));
            });
          }
        }
      }
    }
  }
}
