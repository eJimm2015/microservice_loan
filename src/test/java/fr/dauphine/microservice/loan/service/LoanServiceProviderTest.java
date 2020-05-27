package fr.dauphine.microservice.loan.service;

import fr.dauphine.microservice.loan.model.Loan;
import fr.dauphine.microservice.loan.model.Reader;
import fr.dauphine.microservice.loan.repository.LoanRepository;
import fr.dauphine.microservice.loan.service.impl.LoanServiceProviderImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LoanServiceProviderTest {

    @Mock
    LoanRepository loanRepository;

    @InjectMocks
    LoanServiceProviderImpl loanServiceProvider;

    @Test
    public void testBookCreation() {
        Loan loan = new Loan();
        when(loanRepository.save(loan)).thenReturn(loan.setId(12345));
        assertEquals(Integer.valueOf(12345), loanServiceProvider.create(loan).getId());
    }

    @Test
    public void testGetById() {
        int id = 12345;
        Loan loan = new Loan().setId(id);
        when(loanRepository.findById(id)).thenReturn(Optional.of(loan));
        assertEquals(Optional.of(loan), loanServiceProvider.getById(id));
    }

    @Test
    public void testReturnBook() {
        Loan loan = new Loan();
        when(loanRepository.save(loan)).thenReturn(loan);
        assertNull(loan.getReturnDate());
        assertNotNull(loanServiceProvider.returnBook(loan).getReturnDate());
    }

    @Test
    public void testFindByBorrowDate() {
        Date date = new Date();
        List<Loan> loans = List.of(new Loan().setId(1), new Loan().setId(2));
        when(loanRepository.findByBorrowDate(date)).thenReturn(loans);
        assertEquals(loans, loanServiceProvider.findByBorrowingDate(date));
    }

    @Test
    public void testGetAllBorrowedBooks() {
        List<Loan> loans = List.of(new Loan().setId(1), new Loan().setId(2));
        when(loanRepository.findByReturnDateNull()).thenReturn(loans);
        assertEquals(loans, loanServiceProvider.getAllBorrowedBooks());
    }

    @Test
    public void testGetHistoryByReader() {
        List<Loan> loans = List.of(new Loan().setId(1), new Loan().setId(2));
        Reader reader = new Reader().setId(12345);
        when(loanRepository.findByReader(reader)).thenReturn(loans);
        assertEquals(loans, loanServiceProvider.getHistoryByReader(reader));
    }
}
