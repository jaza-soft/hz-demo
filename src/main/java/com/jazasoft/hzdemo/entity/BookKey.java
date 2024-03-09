package com.jazasoft.hzdemo.entity;

import com.hazelcast.partition.PartitionAware;

import java.io.Serializable;

public final class BookKey implements PartitionAware<String>, Serializable {
  private final Long id;
  private final String category;

  public BookKey(Long id, String category) {
    this.id = id;
    this.category = category;
  }

  @Override
  public String getPartitionKey() {
    return category;
  }

  public Long getId() {
    return id;
  }

  @Override
  public String toString() {
    return id +"@"+category;
  }
}
