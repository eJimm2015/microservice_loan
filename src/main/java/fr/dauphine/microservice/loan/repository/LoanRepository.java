package fr.dauphine.microservice.loan.repository;

import fr.dauphine.microservice.loan.model.Loan;
import fr.dauphine.microservice.loan.model.Reader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Integer> {
    List<Loan> findByBorrowDate(final Date borrowDate);
    List<Loan> findByReaderId(final Integer readerId);
    List<Loan> findByReturnDateNull();
}
