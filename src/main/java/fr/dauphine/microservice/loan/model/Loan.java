package fr.dauphine.microservice.loan.model;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
public class Loan  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    public Integer getId() {
        return id;
    }

    public Loan setId(Integer id) {
        this.id = id;
        return this;
    }

    @Column(nullable = false)
    private String bookIsbn;

    @Column(nullable = false)
    private Integer readerId;

    private Date borrowDate;

    private Date returnDate;

    public String getBookIsbn() {
        return bookIsbn;
    }

    public Loan setBookIsbn(String bookIsbn) {
        this.bookIsbn = bookIsbn;
        return this;
    }

    public Integer getReaderId() {
        return readerId;
    }

    public Loan setReaderId(Integer readerId) {
        this.readerId = readerId;
        return this;
    }

    public Date getBorrowDate() {
        return borrowDate;
    }

    public Loan setBorrowDate(Date borrowDate) {
        this.borrowDate = borrowDate;
        return this;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Loan loan = (Loan) o;
        return id.equals(loan.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Loan{" +
                "id=" + id +
                ", bookIsbn='" + bookIsbn + '\'' +
                ", readerId=" + readerId +
                ", borrowDate=" + borrowDate +
                ", returnDate=" + returnDate +
                '}';
    }
}
