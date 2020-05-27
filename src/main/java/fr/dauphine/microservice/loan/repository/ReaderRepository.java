package fr.dauphine.microservice.loan.repository;

import fr.dauphine.microservice.loan.model.Reader;

import java.util.Optional;

public interface ReaderRepository {
    Optional<Reader> find(Integer id);
}
