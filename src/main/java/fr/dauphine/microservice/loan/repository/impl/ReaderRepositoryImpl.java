package fr.dauphine.microservice.loan.repository.impl;

import fr.dauphine.microservice.loan.model.Reader;
import fr.dauphine.microservice.loan.repository.ReaderRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;
import java.util.Optional;

import static org.springframework.http.HttpMethod.GET;

@Service
@PropertySource("api.properties")
public class ReaderRepositoryImpl implements ReaderRepository {

    @Value("${reader.api}")
    private String readerApi;

    @Override
    public Optional<Reader> find(Integer id) {
      Reader reader = getReader(id);
      return Objects.isNull(reader.getId()) ? Optional.empty() : Optional.of(reader);
    }
    private Reader getReader(Integer id){
        RestTemplate restTemplate = new RestTemplate();
        String uri = String.format("%s%s",readerApi,id);
        ResponseEntity<Reader> responseEntity = restTemplate.exchange(uri, GET, null, Reader.class);
        return responseEntity.getBody();
    }
}
