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
        return books.stream().map(e-> getDto(e)).collect(Collectors.toList());

    }

    @Override
    public List<LoanDto> getAllBorrowedBooks() {

        return loanRepository.findByReturnDateNull()
                .stream()
                .map(e-> getDto(e))
                .collect(Collectors.toList());
    }

    @Override
    public List<LoanDto> getHistoryByReader(Reader reader) {
        Optional<Reader> optionalReader = readerRepository.find(reader.getId());
        if(optionalReader.isPresent()) return loanRepository.findByReaderId(reader.getId()).stream().map(e-> new LoanDto().fill(e).setReader(readerRepository.find(e.getReaderId()).get())
                .setBook(bookRepository.find(e.getBookIsbn()).get())).collect(Collectors.toList());
      throw new IllegalArgumentException(String.format("L'utilisateur n°%s n'existe pas", reader.getId()));
    }

    @Override
    public LoanDto getById(Integer id) {
        Optional<Loan> byId = loanRepository.findById(id);
        if(byId.isPresent()) return getDto(byId.get());
        throw new IllegalArgumentException(String.format("L'emprunt n°%s n'existe pas", id));
    }

    private String prepareExceptionMessage(Loan loan, boolean reader, boolean book) {
        StringBuilder stringBuilder = new StringBuilder("");
        if(!reader) stringBuilder.append(String.format("L'utilisateur n°%s n'existe pas. ", loan.getReaderId()));
        if(!book) stringBuilder.append(String.format("Le livre n°%s n'existe pas", loan.getBookIsbn()));
        return stringBuilder.toString();
    }

    private LoanDto getDto(Loan fill) {
        return new LoanDto().fill(fill).setReader(readerRepository.find(fill.getReaderId()).get())
                .setBook(bookRepository.find(fill.getBookIsbn()).get());
    }
}
