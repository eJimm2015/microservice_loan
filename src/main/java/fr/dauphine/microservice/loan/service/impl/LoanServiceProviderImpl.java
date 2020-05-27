package fr.dauphine.microservice.loan.service.impl;
import fr.dauphine.microservice.loan.model.Book;
import fr.dauphine.microservice.loan.model.Loan;
import fr.dauphine.microservice.loan.model.Reader;
import fr.dauphine.microservice.loan.repository.BookRepository;
import fr.dauphine.microservice.loan.repository.LoanRepository;
import fr.dauphine.microservice.loan.repository.ReaderRepository;
import fr.dauphine.microservice.loan.service.LoanServiceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class LoanServiceProviderImpl implements LoanServiceProvider {

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private ReaderRepository readerRepository;

    @Autowired
    private BookRepository bookRepository;

    @Override
    public Loan create(Loan loan) {
        Optional<Reader> optionalReader = readerRepository.find(loan.getReader().getId());
        Optional<Book> optionalBook = bookRepository.find(loan.getBook().getIsbn());
        if (optionalReader.isPresent() && optionalBook.isPresent()){
            loan.setReader(optionalReader.get()).setBook(optionalBook.get());
            return loanRepository.save(loan);
        }
        return loan;
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
        Optional<Reader> optionalReader = readerRepository.find(reader.getId());
        if(optionalReader.isPresent()) return loanRepository.findByReader(reader);
        return Collections.emptyList();
    }

    @Override
    public Optional<Loan> getById(Integer id) {
        return loanRepository.findById(id);
    }
}
