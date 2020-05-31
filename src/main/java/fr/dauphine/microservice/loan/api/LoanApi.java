package fr.dauphine.microservice.loan.api;

import fr.dauphine.microservice.loan.dto.LoanDto;
import fr.dauphine.microservice.loan.model.ClientLoan;
import fr.dauphine.microservice.loan.model.Reader;
import fr.dauphine.microservice.loan.service.LoanServiceProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/loans")
@Api(value = "Loan API")
public class LoanApi {

    @Autowired
    private LoanServiceProvider loanServiceProvider;

    @PostMapping
    @ApiOperation("Create Loan")
    public ResponseEntity<EntityModel<LoanDto>> create(@RequestBody ClientLoan clientLoan) {

        try {
            LoanDto created = loanServiceProvider.create(clientLoan.toLoan());
            Link link = getLink(created.getId());
            return new ResponseEntity<>(EntityModel.of(created, link), CREATED);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(NOT_FOUND, e.getMessage());
        }
    }

    @PutMapping("{id}")
    @ApiOperation("Return Book")
    public ResponseEntity<EntityModel<LoanDto>> returnBook(@PathVariable("id") Integer id) {
        try {
            LoanDto returned = loanServiceProvider.returnBook(id);
            return ResponseEntity.ok(EntityModel.of(returned, getLink(returned.getId())));
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("{id}")
    @ApiOperation("Get Loan by ID")
    public ResponseEntity<EntityModel<LoanDto>> getById(@PathVariable("id") Integer id) {
       try {
           LoanDto loan = loanServiceProvider.getById(id);
           Link link = getLink(id);
           return new ResponseEntity<>(EntityModel.of(loan, link), OK);
       } catch (NoSuchElementException e) {
           throw new ResponseStatusException(NOT_FOUND, e.getMessage());
       }
    }

    @GetMapping
    @ApiOperation("Get Loans by attributes")
    public ResponseEntity<CollectionModel<LoanDto>> findBy(@ApiParam(value = "YYYY-MM-DD") @RequestParam(value = "date", required = false) String date,
                                                        @RequestParam(value = "reader", required = false) Integer id) {
        List<LoanDto> loans;
        if(date != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            loans = loanServiceProvider.findByBorrowingDate(LocalDate.parse(date, formatter));
        }
        else if(id != null) {
            try {
                loans = loanServiceProvider.getHistoryByReader(new Reader().setId(id));
            } catch (NoSuchElementException e) {
                throw new ResponseStatusException(NOT_FOUND, e.getMessage());
            }
        }
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
