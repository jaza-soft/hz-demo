package com.jazasoft.hzdemo.service;

import com.jazasoft.hzdemo.entity.Author;
import com.jazasoft.hzdemo.entity.Book;
import com.jazasoft.hzdemo.repository.AuthorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

  public List<Author> findAll() {
    return authorRepository.findAll();
  }

  @Transactional
  public Author save(Author author) {
    for (Book book: author.getBookList()) {
      book.setAuthor(author);
    }
    return authorRepository.save(author);
  }

  @Transactional
  public Author update(Author author) {
    Author mAuthor = authorRepository.findById(author.getId()).orElseThrow();
    mAuthor.setName(author.getName());

    //** Update children entity **//
    List<Long> existingIds = author.getBookList().stream().map(Book::getId).filter(Objects::nonNull).toList();
    List<Book> newList = author.getBookList().stream().filter(book -> book.getId() == null).collect(Collectors.toList());
    List<Book> removeList = mAuthor.getBookList().stream().filter(book -> !existingIds.contains(book.getId())).collect(Collectors.toList());;

    //** New **//
    newList.forEach(book -> book.setAuthor(mAuthor));
    mAuthor.getBookList().addAll(newList);

    //** Remove **//
    mAuthor.getBookList().removeAll(removeList);

    //** Existing **//
    for (Long id: existingIds) {
      Book book = author.getBookList().stream().filter(b -> id.equals(b.getId())).findAny().orElse(null);
      Book mBook = mAuthor.getBookList().stream().filter(b -> id.equals(b.getId())).findAny().orElse(null);
      if (book == null || mBook == null) continue;
      mBook.setName(book.getName());
      mBook.setCategory(book.getCategory());
    }

    return mAuthor;
  }

  @Transactional
  public void delete(Long id) {
    authorRepository.deleteById(id);
  }
}
