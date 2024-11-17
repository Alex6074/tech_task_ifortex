package com.ifortex.bookservice.service.impl;

import com.ifortex.bookservice.dto.SearchCriteria;
import com.ifortex.bookservice.model.Book;
import com.ifortex.bookservice.repository.BookRepository;
import com.ifortex.bookservice.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Map<String, Long> getBooks() {
        return bookRepository.findAll().stream()
                .flatMap(book -> book.getGenres().stream())
                .collect(Collectors.groupingBy(genre -> genre, Collectors.counting()))
                .entrySet().stream()
                .sorted((e1, e2) -> Long.compare(e2.getValue(), e1.getValue()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue,
                        LinkedHashMap::new
                ));
    }

    @Override
    public List<Book> getAllByCriteria(SearchCriteria searchCriteria) {
        return bookRepository.findAll().stream()
                .filter(book -> (searchCriteria.getTitle().isBlank()
                        || book.getTitle().contains(searchCriteria.getTitle())))
                .filter(book -> (searchCriteria.getAuthor().isBlank()
                        || book.getAuthor().contains(searchCriteria.getAuthor())))
                .filter(book -> (searchCriteria.getGenre().isBlank()
                        || book.getGenres().contains(searchCriteria.getGenre())))
                .filter(book -> (searchCriteria.getDescription().isBlank()
                        || book.getDescription().contains(searchCriteria.getDescription())))
                .filter(book -> (searchCriteria.getYear() == null
                        || book.getPublicationDate().getYear() == searchCriteria.getYear()))
                .sorted(Comparator.comparing(Book::getPublicationDate))
                .toList();
    }
}
