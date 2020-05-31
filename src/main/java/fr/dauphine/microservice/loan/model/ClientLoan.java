package fr.dauphine.microservice.loan.model;

import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDate;
import java.util.Objects;

public class ClientLoan {

    private String bookIsbn;
    private Integer readerId;
    @ApiModelProperty(value = "YYYY-MM-DD", example = "YYYY-MM-DD")
    private LocalDate borrowDate;
    @ApiModelProperty(value = "YYYY-MM-DD")
    private LocalDate returnDate;

    public String getBookIsbn() {
        return bookIsbn;
    }

    public void setBookIsbn(String bookIsbn) {
        this.bookIsbn = bookIsbn;
    }

    public Integer getReaderId() {
        return readerId;
    }

    public void setReaderId(Integer readerId) {
        this.readerId = readerId;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(LocalDate borrowDate) {
        this.borrowDate = borrowDate;
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
        ClientLoan that = (ClientLoan) o;
        return bookIsbn.equals(that.bookIsbn) &&
                readerId.equals(that.readerId) &&
                borrowDate.equals(that.borrowDate) &&
                returnDate.equals(that.returnDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookIsbn, readerId, borrowDate, returnDate);
    }

    @Override
    public String toString() {
        return "ClientLoan{" +
                "bookIsbn='" + bookIsbn + '\'' +
                ", readerId=" + readerId +
                ", borrowDate=" + borrowDate +
                ", returnDate=" + returnDate +
                '}';
    }

    public Loan toLoan() {
        return new Loan()
                .setBookIsbn(bookIsbn)
                .setReaderId(readerId)
                .setBorrowDate(borrowDate)
                .setReturnDate(returnDate);
    }
}

