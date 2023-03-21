package com.mindhub.homebanking.services;
import com.mindhub.homebanking.models.Loan;
import java.util.List;
import java.util.Optional;

public interface LoanService {
    List<Loan> findAll();
    Optional<Loan> findById(Long id);
    void save(Loan loan);
    boolean existsByName(String name);
}
