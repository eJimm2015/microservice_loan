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

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LoanServiceProviderImpl implements LoanServiceProvider {

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private ReaderRepository readerRepository;

    @Autowired
    private BookRepository bookRepository;

    @Override
    public LoanDto create(Loan loan) throws NoSuchElementException {
        try {
            Reader reader = readerRepository.find(loan.getReaderId());
            Book book = bookRepository.find(loan.getBookIsbn());
            Loan saved = loanRepository.save(loan);
            return new LoanDto()
                    .fill(saved)
                    .setBook(book)
                    .setReader(reader);
        } catch (NoSuchElementException e){
            throw e;
        }
    }

    @Override
    public LoanDto returnBook(Integer id) {
        Optional<Loan> byId = loanRepository.findById(id);
        if(byId.isPresent()) {
            Loan updated = byId.get();
            updated.setReturnDate(new Date());
            loanRepository.save(updated);
           return getDto(updated);

        }
        throw new IllegalArgumentException(String.format("L'emprunt n°%s n'existe pas", id));
    }

    @Override
    public List<LoanDto> findByBorrowingDate(Date date) {

        List<Loan> books = loanRepository.findByBorrowDate(date);
        return books.stream().map(this::getDto).collect(Collectors.toList());

    }

    @Override
    public List<LoanDto> getAllBorrowedBooks() {

        return loanRepository.findByReturnDateNull()
                .stream()
                .map(this::getDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<LoanDto> getHistoryByReader(Reader reader) throws NoSuchElementException {
        Reader reader1 = readerRepository.find(reader.getId());
        return loanRepository.findByReaderId(reader1.getId()).stream().map(e-> new LoanDto().fill(e).setReader(reader1)
                .setBook(bookRepository.find(e.getBookIsbn()))).collect(Collectors.toList());
    }

    @Override
    public LoanDto getById(Integer id) {
        Optional<Loan> byId = loanRepository.findById(id);
        if(byId.isPresent()) return getDto(byId.get());
        throw new NoSuchElementException(String.format("L'emprunt n°%s n'existe pas", id));
    }

    private LoanDto getDto(Loan fill) {
        return new LoanDto().fill(fill).setReader(readerRepository.find(fill.getReaderId()))
                .setBook(bookRepository.find(fill.getBookIsbn()));
    }
}
