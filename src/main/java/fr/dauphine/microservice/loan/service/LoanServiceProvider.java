package fr.dauphine.microservice.loan.service;

import fr.dauphine.microservice.loan.dto.LoanDto;
import fr.dauphine.microservice.loan.model.Loan;
import fr.dauphine.microservice.loan.model.Reader;

import java.util.Date;
import java.util.List;


public interface LoanServiceProvider {
    LoanDto create(final Loan loan) throws IllegalArgumentException;
    LoanDto returnBook(final Integer id);
    List<LoanDto> findByBorrowingDate(final Date date);
    List<LoanDto> getAllBorrowedBooks();
    List<LoanDto> getHistoryByReader(final Reader reader);
    LoanDto getById(final Integer id);
}
