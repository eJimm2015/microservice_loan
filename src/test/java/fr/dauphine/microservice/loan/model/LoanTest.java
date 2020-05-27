package fr.dauphine.microservice.loan.model;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class LoanTest {

    Loan loan;

    @Before
    public void setUp() {
     loan = new Loan();
    }

    @Test
    public void testIdGetterAndSetter() {
        loan.setId(12345);
        assertEquals(Integer.valueOf(12345), loan.getId());
    }

    @Test
    public void testBorrowDateGetterAndSetter() {
        Date d = new Date();
        loan.setBorrowDate(d);
        assertEquals(d, loan.getBorrowDate());
    }

    @Test
    public void testReturnDateGetterAndSetter() {
        Date d = new Date();
        loan.setReturnDate(d);
        assertEquals(d, loan.getReturnDate());
    }

    @Test
    public void testReaderGetterAndSetter() {
        loan.setReaderId(12345);
        assertEquals(Integer.valueOf(12345), loan.getReaderId());
    }

    @Test
    public void testBookGetterAndSetter() {
        loan.setBookIsbn("I1245DZ54");
        assertEquals("I1245DZ54", loan.getBookIsbn());
    }
    @Test
    public void testEquals() {
        Loan loan1 = new Loan();
        loan1.setId(12345);
        loan.setId(12345);
        assertEquals(loan, loan1);
    }

    @Test
    public void testNotEquals() {
        Loan loan1 = new Loan();
        loan1.setId(12345);
        loan.setId(12354);
        assertNotEquals(loan, loan1);
    }

    @Test
    public void testSameHashcode() {
        Loan loan1 = new Loan();
        loan1.setId(12345);
        loan.setId(12345);
        assertEquals(loan.hashCode(), loan1.hashCode());
    }

    @Test
    public void testNotSameHashcode() {
        Loan loan1 = new Loan();
        loan1.setId(12345);
        loan.setId(12354);
        assertNotEquals(loan.hashCode(), loan1.hashCode());
    }

    @Test
    public void testNotEmptyToString() {
        assertNotEquals("", loan.toString());
    }
}
