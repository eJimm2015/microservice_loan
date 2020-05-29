package fr.dauphine.microservice.loan.repository.impl;

import fr.dauphine.microservice.loan.model.Book;
import fr.dauphine.microservice.loan.model.Reader;
import fr.dauphine.microservice.loan.repository.BookRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;
import java.util.Optional;

import static org.springframework.http.HttpMethod.GET;

@Service
@PropertySource("classpath:api.properties")
public class BookRepositoryImpl implements BookRepository {

    @Value("${book.api}")
    private String bookApi;

    @Override
    public Optional<Book> find(String isbn) {
        Book book = getBook(isbn);
        return Objects.isNull(book.getIsbn()) ? Optional.empty() : Optional.of(book);
    }
    private Book getBook(String isbn){
        RestTemplate restTemplate = new RestTemplate();
        String uri = String.format("%s%s",bookApi,isbn);
        ResponseEntity<Book> responseEntity = restTemplate.exchange(uri, GET, null, Book.class);
        return responseEntity.getBody();
    }
}
