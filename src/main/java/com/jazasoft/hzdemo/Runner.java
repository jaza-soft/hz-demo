package com.jazasoft.hzdemo;

import com.hazelcast.core.DistributedObject;
import com.hazelcast.core.DistributedObjectUtil;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Collection;

//@Component
public class Runner implements CommandLineRunner {

  @Override
  public void run(String... args) throws Exception {
    HazelcastInstance hazelcast = Hazelcast.getAllHazelcastInstances().stream().findAny().orElse(null);

    while (true) {
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
      Thread.sleep(30000L);
    }
  }
}
