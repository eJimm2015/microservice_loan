package fr.dauphine.microservice.loan.service.impl;
import fr.dauphine.microservice.loan.dto.LoanDto;
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
    public LoanDto create(Loan loan) throws IllegalArgumentException {
        Optional<Reader> optionalReader = readerRepository.find(loan.getReaderId());
        Optional<Book> optionalBook = bookRepository.find(loan.getBookIsbn());
        boolean readerPresence = optionalReader.isPresent();
        boolean bookPresence = optionalBook.isPresent();
        if (readerPresence && bookPresence) {
            Loan saved = loanRepository.save(loan);
            return new LoanDto()
                    .fill(saved)
                    .setBook(optionalBook.get())
                    .setReader(optionalReader.get());
        }
        throw new IllegalArgumentException(prepareExceptionMessage(loan, readerPresence, bookPresence));
    }

    @Override
    public LoanDto returnBook(Loan loan) {
        Optional<Loan> byId = loanRepository.findById(loan.getId());
        if(byId.isPresent()) {
            Loan updated = byId.get();
            updated.setReturnDate(new Date());
            loanRepository.save(updated);
            LoanDto loanDto = new LoanDto().fill(loan)
                    .setReader(readerRepository.find(updated.getReaderId()).get())
                    .setBook(bookRepository.find(updated.getBookIsbn()).get());
            return loanDto;
        }
        throw new IllegalArgumentException(String.format("L'emprunt n°%s n'existe pas", loan.getId()));
    }

    @Override
    public List<Loan> findByBorrowingDate(Date date) {
        List<Loan> books = loanRepository.findByBorrowDate(date);
        return books;
    }

    @Override
    public List<Loan> getAllBorrowedBooks() {
        return loanRepository.findByReturnDateNull();
    }

    @Override
    public List<Loan> getHistoryByReader(Reader reader) {
        Optional<Reader> optionalReader = readerRepository.find(reader.getId());
        if(optionalReader.isPresent()) return loanRepository.findByReaderId(reader.getId());
        return Collections.emptyList();
    }

    @Override
    public Optional<Loan> getById(Integer id) {
        return loanRepository.findById(id);
    }

    private String prepareExceptionMessage(Loan loan, boolean reader, boolean book) {
        StringBuilder stringBuilder = new StringBuilder("");
        if(!reader) stringBuilder.append(String.format("L'utilisateur n°%s est inexistant", loan.getReaderId()));
        if(!book) stringBuilder.append(String.format("Le livre n°%s est inexistant", loan.getReaderId()));
        return stringBuilder.toString();
    }
}
