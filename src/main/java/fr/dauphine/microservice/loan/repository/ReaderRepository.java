package fr.dauphine.microservice.loan.repository;

import fr.dauphine.microservice.loan.model.Reader;

public interface ReaderRepository {
    Reader find(Integer id);
}
