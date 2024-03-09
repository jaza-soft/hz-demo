package com.jazasoft.hzdemo.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Cache(region = "book", usage = CacheConcurrencyStrategy.READ_WRITE)
@JsonIgnoreProperties(ignoreUnknown = true, value = {"hibernateLazyInitializer", "handler"})
public class Book implements Serializable {

  @Id
  @GeneratedValue(
      strategy = GenerationType.SEQUENCE,
      generator = "book_generator"
  )
  @GenericGenerator(
      name = "book_generator",
      strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
      parameters = {
          @org.hibernate.annotations.Parameter(name = "sequence_name", value = "book_seq"),
          @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
          @org.hibernate.annotations.Parameter(name = "increment_size", value = "10"),
          @org.hibernate.annotations.Parameter(name = "optimizer", value = "pooled-lo")
      }
  )
  private Long id;

  private String name;

  private String category;

//  @ManyToOne(fetch = FetchType.LAZY, optional = false)
//  @ManyToOne(fetch = FetchType.LAZY)
  @ManyToOne(optional = false)
//  @ManyToOne
  private Author author;

  @Transient
  private Long authorId;

  public Book() {
  }

  public Book(Long id, String name) {
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

  public Author getAuthor() {
    return author;
  }

  public void setAuthor(Author author) {
    this.author = author;
  }

  public Long getAuthorId() {
    return authorId;
  }

  public void setAuthorId(Long authorId) {
    this.authorId = authorId;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  @Override
  public String toString() {
    return "Book{" +
        "id=" + id +
        ", name='" + name + '\'' +
        '}';
  }
}
