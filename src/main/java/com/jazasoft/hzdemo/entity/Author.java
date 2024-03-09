package com.jazasoft.hzdemo.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Cache(region = "author", usage = CacheConcurrencyStrategy.READ_WRITE)
@JsonIgnoreProperties(ignoreUnknown = true, value = {"hibernateLazyInitializer", "handler"})
public class Author implements Serializable {
  @Id
  @GeneratedValue(
      strategy = GenerationType.SEQUENCE,
      generator = "author_generator"
  )
  @GenericGenerator(
      name = "author_generator",
      strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
      parameters = {
          @org.hibernate.annotations.Parameter(name = "sequence_name", value = "author_seq"),
          @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
          @org.hibernate.annotations.Parameter(name = "increment_size", value = "10"),
          @org.hibernate.annotations.Parameter(name = "optimizer", value = "pooled-lo")
      }
  )
  private Long id;
  private String name;

  @Cache(region = "author.books", usage = CacheConcurrencyStrategy.READ_WRITE)
  @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Book> bookList = new ArrayList<>();

  public Author() {
  }

  public Author(Long id, String name) {
    this.id = id;
    this.name = name;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<Book> getBookList() {
    return bookList;
  }

  public void setBookList(List<Book> bookList) {
    this.bookList = bookList;
  }

  @Override
  public String toString() {
    return "Author{" +
        "id=" + id +
        ", name='" + name + '\'' +
        '}';
  }
}
