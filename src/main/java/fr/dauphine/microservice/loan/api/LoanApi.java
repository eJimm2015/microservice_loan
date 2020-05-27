package fr.dauphine.microservice.loan.api;

import fr.dauphine.microservice.loan.dto.LoanDto;
import fr.dauphine.microservice.loan.model.Loan;
import fr.dauphine.microservice.loan.model.Reader;
import fr.dauphine.microservice.loan.service.LoanServiceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/loans")
public class LoanApi {

    @Autowired
    private LoanServiceProvider loanServiceProvider;

    @PostMapping
    public ResponseEntity<EntityModel<LoanDto>> create(@RequestBody Loan loan) {
        LoanDto created = loanServiceProvider.create(loan);
        Link link = getLink(created.getId());
        return new ResponseEntity<>(EntityModel.of(created, link), CREATED);
    }

    @PutMapping
    public ResponseEntity<EntityModel<LoanDto>> returnBook(@RequestBody Loan loan) {
        LoanDto returned = loanServiceProvider.returnBook(loan);
        return ResponseEntity.ok(EntityModel.of(returned, getLink(returned.getId())));
    }

    @GetMapping("{id}")
    public ResponseEntity<EntityModel<LoanDto>> getById(@PathVariable("id") Integer id) {
        Optional<LoanDto> loanOptional = loanServiceProvider.getById(id);
        LoanDto loan = loanOptional.orElse(new LoanDto());
        Link link = getLink(id);
        return new ResponseEntity<>(EntityModel.of(loan, link), CREATED);
    }

    @GetMapping
    public ResponseEntity<CollectionModel<LoanDto>> findBy(@RequestParam(value = "date", required = false) String date,
                                                        @RequestParam(value = "reader", required = false) Integer id) {
        List<LoanDto> loans = Collections.emptyList();
        if(date != null) {
            try {
                loans = loanServiceProvider
                        .findByBorrowingDate(new SimpleDateFormat("dd/mm/yyyy").parse(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        else if(id != null) loans = loanServiceProvider.getHistoryByReader(new Reader().setId(id));
        else loans = loanServiceProvider.getAllBorrowedBooks();

        List<LoanDto> collect = loans.stream()
                .map(e -> e.add(getLink(e.getId())))
                .collect(Collectors.toList());
        Link link = linkTo(methodOn(LoanApi.class)
                .findBy(date, id)).withSelfRel();
        return ResponseEntity.ok(CollectionModel.of(collect, link));
    }

    private Link getLink(Integer id) {
        return linkTo(methodOn(LoanApi.class)
                .getById(id)).withSelfRel();
    }

}
