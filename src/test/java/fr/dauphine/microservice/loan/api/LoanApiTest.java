package fr.dauphine.microservice.loan.api;

import fr.dauphine.microservice.loan.dto.LoanDto;
import fr.dauphine.microservice.loan.model.ClientLoan;
import fr.dauphine.microservice.loan.model.Loan;
import fr.dauphine.microservice.loan.model.Reader;
import fr.dauphine.microservice.loan.service.impl.LoanServiceProviderImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LoanApiTest {

    @Mock
    private LoanServiceProviderImpl loanServiceProvider;

    @InjectMocks
    private LoanApi loanApi;

    @Test
    public void testCreation() {
        Loan loan = new Loan().setId(1);
        LoanDto loanDto = new LoanDto().fill(loan);
        when(loanServiceProvider.create(any())).thenReturn(loanDto);
        ResponseEntity<EntityModel<LoanDto>> entityModelResponseEntity = loanApi.create(new ClientLoan());
        EntityModel<LoanDto> body = entityModelResponseEntity.getBody();
        assertNotNull(body);
        assertNotNull(body.getLinks());
        assertEquals(loanDto, body.getContent());
    }

    @Test(expected = ResponseStatusException.class)
    public void testCreationWithUnknownIds() {
        when(loanServiceProvider.create(any())).thenThrow(new NoSuchElementException());
        loanApi.create(new ClientLoan());
    }

    @Test
    public void testReturnBook() {
        Loan loan = new Loan().setId(1);
        LoanDto loanDto = new LoanDto().fill(loan);
        when(loanServiceProvider.returnBook(1)).thenReturn(loanDto);
        ResponseEntity<EntityModel<LoanDto>> entityModelResponseEntity = loanApi.returnBook(1);
        EntityModel<LoanDto> body = entityModelResponseEntity.getBody();
        assertNotNull(body);
        assertNotNull(body.getLinks());
        assertEquals(loanDto, body.getContent());
    }

    @Test(expected = ResponseStatusException.class)
    public void testReturnBookWithUnknownId() {
        when(loanServiceProvider.returnBook(1)).thenThrow(new NoSuchElementException());
        loanApi.returnBook(1);
    }

    @Test
    public void testFindById() {
        LoanDto loanDto = new LoanDto().setId(1);
        when(loanServiceProvider.getById(1)).thenReturn(loanDto);
        ResponseEntity<EntityModel<LoanDto>> byId = loanApi.getById(1);
        EntityModel<LoanDto> body = byId.getBody();
        assertNotNull(body);
        assertNotNull(body.getLinks());
        assertEquals(loanDto, body.getContent());
    }

    @Test(expected = ResponseStatusException.class)
    public void testFindUnknownId() {
        when(loanServiceProvider.getById(1)).thenThrow(new NoSuchElementException());
        loanApi.getById(1);
    }

    @Test
    public void testFindByDate() {
        List<LoanDto> loanDtos = new ArrayList<>();
        loanDtos.add(new LoanDto().setId(1));
        loanDtos.add(new LoanDto().setId(2));
        String date = "2020-05-29";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        when(loanServiceProvider
                .findByBorrowingDate(LocalDate.parse(date, formatter)))
        .thenReturn(loanDtos);
        ResponseEntity<CollectionModel<LoanDto>> byDate = loanApi.findBy(date, null);
        CollectionModel<LoanDto> body = byDate.getBody();
        assertNotNull(body);
        assertEquals(loanDtos, new ArrayList<>(body.getContent()));

    }

    @Test
    public void testFindByReaderId() {
        Reader reader = new Reader().setId(1);
        List<LoanDto> loanDtos = new ArrayList<>();
        loanDtos.add(new LoanDto().setId(1));
        loanDtos.add(new LoanDto().setId(2));
        when(loanServiceProvider.getHistoryByReader(reader)).thenReturn(loanDtos);
        ResponseEntity<CollectionModel<LoanDto>> byDate = loanApi.findBy(null, 1);
        CollectionModel<LoanDto> body = byDate.getBody();
        assertNotNull(body);
        assertEquals(loanDtos, new ArrayList<>(body.getContent()));
    }

    @Test(expected = ResponseStatusException.class)
    public void testFindByUnknownReaderId() {
        when(loanServiceProvider.getHistoryByReader(any())).thenThrow(new NoSuchElementException());
        loanApi.findBy(null, 1);
    }

    @Test
    public void testFindAll() {
        List<LoanDto> loanDtos = new ArrayList<>();
        loanDtos.add(new LoanDto().setId(1));
        loanDtos.add(new LoanDto().setId(2));
        when(loanServiceProvider.getAllBorrowedBooks()).thenReturn(loanDtos);
        ResponseEntity<CollectionModel<LoanDto>> getAll = loanApi.findBy(null, null);
        CollectionModel<LoanDto> body = getAll.getBody();
        assertNotNull(body);
        assertEquals(loanDtos, new ArrayList<>(body.getContent()));
    }
}