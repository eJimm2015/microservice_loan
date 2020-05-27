package fr.dauphine.microservice.loan.service.impl;
import fr.dauphine.microservice.loan.model.Loan;
import fr.dauphine.microservice.loan.model.Reader;
import fr.dauphine.microservice.loan.repository.LoanRepository;
import fr.dauphine.microservice.loan.service.LoanServiceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class LoanServiceProviderImpl implements LoanServiceProvider {

    @Autowired
    private LoanRepository loanRepository;

    @Override
    public Loan create(Loan loan) {
        return loanRepository.save(loan);
    }

    @Override
    public Loan returnBook(Loan loan) {
        loan.setReturnDate(new Date());
        return loanRepository.save(loan);
    }

    @Override
    public List<Loan> findByBorrowingDate(Date date) {
        return loanRepository.findByBorrowDate(date);
    }

    @Override
    public List<Loan> getAllBorrowedBooks() {
        return loanRepository.findByReturnDateNull();
    }

    @Override
    public List<Loan> getHistoryByReader(Reader reader) {
        return loanRepository.findByReader(reader);
    }

    @Override
    public Optional<Loan> getById(Integer id) {
        return loanRepository.findById(id);
    }
}
