package fr.dauphine.microservice.loan.service;

import fr.dauphine.microservice.loan.dto.LoanDto;
import fr.dauphine.microservice.loan.model.Book;
import fr.dauphine.microservice.loan.model.Loan;
import fr.dauphine.microservice.loan.model.Reader;
import fr.dauphine.microservice.loan.repository.LoanRepository;
import fr.dauphine.microservice.loan.repository.impl.BookRepositoryImpl;
import fr.dauphine.microservice.loan.repository.impl.ReaderRepositoryImpl;
import fr.dauphine.microservice.loan.service.impl.LoanServiceProviderImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LoanServiceProviderTest {

    @Mock
    LoanRepository loanRepository;

    @Mock
    ReaderRepositoryImpl readerRepository;

    @Mock
    BookRepositoryImpl bookRepository;

    @InjectMocks
    LoanServiceProviderImpl loanServiceProvider;

    @Test
    public void testBookCreation() {
        Loan loan = new Loan();
        when(loanRepository.save(loan)).thenReturn(loan.setId(12345));
        when(readerRepository.find(any())).thenReturn(Optional.of(new Reader().setId(12)));
        when(bookRepository.find(any())).thenReturn(Optional.of(new Book().setIsbn("AE45")));
        assertEquals(Integer.valueOf(12345), loanServiceProvider.create(loan).getId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnknownReader() {
        int id = 12345;
        Loan loan = new Loan().setId(123);
        when(readerRepository.find(any())).thenReturn(Optional.empty());
        when(bookRepository.find(any())).thenReturn(Optional.of(new Book().setIsbn("A123456")));
        loanServiceProvider.create(loan);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnknownBook() {
        int id = 12345;
        Loan loan = new Loan().setId(123);
        when(readerRepository.find(any())).thenReturn(Optional.of(new Reader().setId(1)));
        when(bookRepository.find(any())).thenReturn(Optional.empty());
        loanServiceProvider.create(loan);
    }

    @Test
    public void testGetById() {
        int id = 12345;
        Loan loan = new Loan().setId(id);
        Book book= new Book().setIsbn("12345");
        Reader reader= new Reader().setId(16);
        LoanDto loanDto= new LoanDto().fill(loan).setReader(reader).setBook(book);
        when(loanRepository.findById(id)).thenReturn(Optional.of(loan));
        when(readerRepository.find(any())).thenReturn(Optional.of(reader));
        when(bookRepository.find(any())).thenReturn(Optional.of(book));
        assertEquals(loanDto, loanServiceProvider.getById(id));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetUnknownId() {
        int id = 12345;
        when(loanRepository.findById(id)).thenReturn(Optional.empty());
        loanServiceProvider.getById(id);
    }

    @Test
    public void testReturnBook() {
        Loan loan = new Loan().setId(15);
        Book book= new Book().setIsbn("12345");
        Reader reader= new Reader().setId(16);
        when(loanRepository.save(loan)).thenReturn(loan);
        when(loanRepository.findById(any())).thenReturn(Optional.of(loan));
        when(readerRepository.find(any())).thenReturn(Optional.of(reader));
        when(bookRepository.find(any())).thenReturn(Optional.of(book));
        assertNull(loan.getReturnDate());
        assertNotNull(loanServiceProvider.returnBook(15).getReturnDate());
    }

    @Test
    public void testFindByBorrowDate() {
        Date date = new Date();
        Book book= new Book().setIsbn("12345");
        Reader reader= new Reader().setId(16);
        List<Loan> loans = List.of(new Loan().setId(1), new Loan().setId(2));
        List<LoanDto> loanDtos=loans.stream().map(e->new LoanDto().fill(e).setReader(reader).setBook(book)).collect(Collectors.toList());
        when(loanRepository.findByBorrowDate(date)).thenReturn(loans);
        when(readerRepository.find(any())).thenReturn(Optional.of(reader));
        when(bookRepository.find(any())).thenReturn(Optional.of(book));
        assertEquals(loanDtos, loanServiceProvider.findByBorrowingDate(date));
    }

    @Test
    public void testGetAllBorrowedBooks() {
        Book book= new Book().setIsbn("12345");
        Reader reader= new Reader().setId(16);
        List<Loan> loans = List.of(new Loan().setId(1), new Loan().setId(2));
        List<LoanDto> loanDtos=loans.stream().map(e->new LoanDto().fill(e).setReader(reader).setBook(book)).collect(Collectors.toList());
        when(readerRepository.find(any())).thenReturn(Optional.of(reader));
        when(bookRepository.find(any())).thenReturn(Optional.of(book));
        when(loanRepository.findByReturnDateNull()).thenReturn(loans);

        assertEquals(loanDtos, loanServiceProvider.getAllBorrowedBooks());
    }

    @Test
    public void testGetHistoryByReader() {
        List<Loan> loans = List.of(new Loan().setId(1).setReaderId(12345).setBookIsbn("12345"), new Loan().setId(2).setReaderId(12345).setBookIsbn("12345"));
        Reader reader = new Reader().setId(12345);
        Book book = new Book().setIsbn("12345");
        List<LoanDto> loanDtos= loans.stream().map(e-> new LoanDto().fill(e).setReader(reader).setBook(book)).collect(Collectors.toList());
        when(loanRepository.findByReaderId(reader.getId())).thenReturn(loans);
        when(readerRepository.find(reader.getId())).thenReturn(Optional.of(reader));
        when(bookRepository.find(any())).thenReturn(Optional.of(book));
        assertEquals(loanDtos, loanServiceProvider.getHistoryByReader(reader));
    }
}
