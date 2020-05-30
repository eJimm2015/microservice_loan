package fr.dauphine.microservice.loan.repository;

import fr.dauphine.microservice.loan.model.Book;

public interface BookRepository {
    Book find(String isbn);
}
