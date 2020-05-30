package fr.dauphine.microservice.loan.repository.impl;

import fr.dauphine.microservice.loan.model.Reader;
import fr.dauphine.microservice.loan.repository.ReaderRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

import static org.springframework.http.HttpMethod.GET;

@Service
@PropertySource("classpath:api.properties")
public class ReaderRepositoryImpl implements ReaderRepository {

    @Value("${reader.api}")
    private String readerApi;

    @Override
    public Reader find(Integer id) {
        RestTemplate restTemplate = new RestTemplate();
        String uri = String.format("%s%s", readerApi, id);
        try {
            ResponseEntity<Reader> responseEntity = restTemplate.exchange(uri, GET, null, Reader.class);
            return responseEntity.getBody();
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().equals(HttpStatus.NOT_FOUND)) throw new NoSuchElementException(String.format("L'utilisateur n°%s n'existe pas ", id));
            else throw e;
        }

    }

}
