package fr.dauphine.microservice.loan.model;

import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDate;
import java.util.Objects;



public class Reader extends RepresentationModel<Reader> {

    private Integer id;
    private Gender gender;

    private String familyName;
    private String firstName;
    private LocalDate birthDate;
    private String address;

    public Integer getId() {
        return id;
    }

    public Reader setId(Integer id) {
        this.id = id;
        return this;
    }

    public Gender getGender() {
        return gender;
    }

    public Reader setGender(Gender gender) {
        this.gender = gender;
        return this;
    }

    public String getFamilyName() {
        return familyName;
    }

    public Reader setFamilyName(String familyName) {
        this.familyName = familyName;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public Reader setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public Reader setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public Reader setAddress(String address) {
        this.address = address;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reader reader = (Reader) o;
        return id.equals(reader.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Reader{" +
                "id=" + id +
                ", gender=" + gender +
                ", familyName='" + familyName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", birthDate=" + birthDate +
                ", address='" + address + '\'' +
                '}';
    }
}
