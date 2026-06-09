package com.enviro.assessment.junior.nhletelo.dto;

import java.time.LocalDate;
import java.util.List;

public class InvestorDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate dateOfBirth;
    private int age;
    private List<InvestmentProductDTO> products;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public List<InvestmentProductDTO> getProducts() { return products; }
    public void setProducts(List<InvestmentProductDTO> products) { this.products = products; }
}