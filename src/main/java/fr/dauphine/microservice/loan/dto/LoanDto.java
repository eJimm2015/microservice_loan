package fr.dauphine.microservice.loan.dto;

import fr.dauphine.microservice.loan.model.Book;
import fr.dauphine.microservice.loan.model.Loan;
import fr.dauphine.microservice.loan.model.Reader;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDate;
import java.util.Objects;

public class LoanDto extends RepresentationModel<LoanDto> {

    private Integer id;

    public Integer getId() {
        return id;
    }

    public LoanDto setId(Integer id) {
        this.id = id;
        return this;
    }

    private Book book;

    private Reader reader;

    private LocalDate borrowDate;

    private LocalDate returnDate;

    public Book getBook() {
        return book;
    }

    public LoanDto setBook(Book book) {
        this.book = book;
        return this;
    }

    public Reader getReader() {
        return reader;
    }

    public LoanDto setReader(Reader reader) {
        this.reader = reader;
        return this;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public LoanDto setBorrowDate(LocalDate borrowDate) {
        this.borrowDate = borrowDate;
        return this;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoanDto loan = (LoanDto) o;
        return id.equals(loan.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public LoanDto fill(Loan loan) {
        id = loan.getId();
        borrowDate = loan.getBorrowDate();
        returnDate = loan.getReturnDate();
        return this;
    }
}
