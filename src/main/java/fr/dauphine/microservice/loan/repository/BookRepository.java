package fr.dauphine.microservice.loan.repository;

import fr.dauphine.microservice.loan.model.Book;

import java.util.Optional;

public interface BookRepository {
    Optional<Book> find(String isbn);
}
