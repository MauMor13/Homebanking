package com.mindhub.homebanking.services.impl;
import com.mindhub.homebanking.models.Loan;
import com.mindhub.homebanking.repositories.LoanRepository;
import com.mindhub.homebanking.services.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class LoanServiceImpl implements LoanService {
    @Autowired
    private LoanRepository loanRepository;
    @Override
    public List<Loan> findAll() {
        return loanRepository.findAll();
    }
    @Override
    public Optional<Loan> findById(Long id) {
        return loanRepository.findById(id);
    }
    @Override
    public void save(Loan loan) { loanRepository.save(loan); }
    @Override
    public boolean existsByName(String name) { return loanRepository.existsLoanByName(name); }
}
