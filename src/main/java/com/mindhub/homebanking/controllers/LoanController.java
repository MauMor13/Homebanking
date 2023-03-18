package com.mindhub.homebanking.controllers;
import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.dtos.NewLoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import com.mindhub.homebanking.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import static com.mindhub.homebanking.models.TransactionType.CREDIT;
import static java.util.stream.Collectors.toList;
@RestController
@RequestMapping("/api")
public class LoanController {
    @Autowired
    private ClientService clientService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private LoanService loanService;
    @Autowired
    private ClientLoanService clientLoanService;
    @Transactional
    @GetMapping("/loans")
    public List<LoanDTO> getLoans(){
        return loanService.findAll().stream().map(LoanDTO::new).collect(toList());
    }
    @PostMapping("/loans")
    public ResponseEntity<Object> newClientLoan(
            Authentication authentication,
            @RequestBody(required = false) LoanApplicationDTO loanApplicationDTO){
        Client clientAuthen = clientService.findByEmail(authentication.getName());
        Loan loan= loanService.findById(loanApplicationDTO.getId()).orElse(null);
        if (loanApplicationDTO.getId() == null || loanApplicationDTO.getId() == 0)
            return new ResponseEntity<>("wrong id value",HttpStatus.FORBIDDEN);
        if (loanApplicationDTO.getAmount() < 10000)
            return new ResponseEntity<>("less than minimum amount",HttpStatus.FORBIDDEN);
        if (loanApplicationDTO.getPayments() == null)
            return new ResponseEntity<>("missing number of payments",HttpStatus.FORBIDDEN);
        if (loanApplicationDTO.getNumberAccountDestini().isEmpty())
            return new ResponseEntity<>("missing number account",HttpStatus.FORBIDDEN);
        if (loan == null)
            return new ResponseEntity<>("nonexistent loan",HttpStatus.FORBIDDEN);
        if (loan.getMaxAmount()<loanApplicationDTO.getAmount())
            return new ResponseEntity<>("requested amount greater than the admitted",HttpStatus.FORBIDDEN);
        if (loan.getPayments().stream().noneMatch(payment -> payment.equals(loanApplicationDTO.getPayments())))
            return new ResponseEntity<>("number of installments do not exist in the requested loan",HttpStatus.FORBIDDEN);
        if (!accountService.existsAccountByNumber(loanApplicationDTO.getNumberAccountDestini()))
            return new ResponseEntity<>("the account does not exist",HttpStatus.FORBIDDEN);
        if (clientAuthen.getAccounts().stream().noneMatch(account -> account.getNumber().equals(loanApplicationDTO.getNumberAccountDestini())))
            return new ResponseEntity<>("the destination account is not the authenticated client",HttpStatus.FORBIDDEN);

        Account accountDestini = accountService.findByNumber(loanApplicationDTO.getNumberAccountDestini());
        ClientLoan clientLoan = new ClientLoan(loanApplicationDTO.getAmount() * loan.getPercentage()  , loanApplicationDTO.getPayments());
        Transaction transaction = new Transaction(CREDIT,loanApplicationDTO.getAmount(), loan.getName().concat(" loan approved"), LocalDateTime.now(),accountDestini.getBalance() + loanApplicationDTO.getAmount());
        accountDestini.setBalance(accountDestini.getBalance() + loanApplicationDTO.getAmount());
        clientAuthen.addClientLoan(clientLoan);
        loan.addClientLoan(clientLoan);
        accountDestini.addTransaction(transaction);
        clientLoanService.save(clientLoan);
        clientService.save(clientAuthen);
        loanService.save(loan);
        transactionService.save(transaction);
        accountService.save(accountDestini);
        return new ResponseEntity<>("Create Loan", HttpStatus.CREATED);
    }
    @PostMapping("/new-loan")//nuevo servlet-
    public ResponseEntity<Object> newLoan(
            @RequestBody(required = false) NewLoanDTO newLoan){
        if (newLoan.getName().isEmpty())
            return new ResponseEntity<>("the name of the loan is missing",HttpStatus.FORBIDDEN);
        if (Double.isNaN(newLoan.getMaxAmount()))
            return new ResponseEntity<>("the max amount of the loan is missing",HttpStatus.FORBIDDEN);
        if (newLoan.getPayments().isEmpty())
            return new ResponseEntity<>("the payments of the loan is missing",HttpStatus.FORBIDDEN);
        if (Float.isNaN(newLoan.getPercentage()))
            return new ResponseEntity<>("the percentege of the loan is missing",HttpStatus.FORBIDDEN);

        Loan newloan = new Loan(newLoan.getName(),newLoan.getMaxAmount(),newLoan.getPayments(),newLoan.getPercentage());
        loanService.save(newloan);
        return new ResponseEntity<>("Create Loan", HttpStatus.CREATED);
    }
}
