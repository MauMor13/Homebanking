package com.mindhub.homebanking.controllers;
import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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
    @PostMapping("/new-loan")
    public ResponseEntity<Object> newLoan(Authentication authentication,
                                          @RequestParam (required = false) Integer maxAmount,
                                          @RequestParam (required = false) List<Integer> payment,
                                          @RequestParam (required = false) String name,
                                          @RequestParam (required = false) Float percentage){
        Client admin= clientService.findByEmail(authentication.getName());
        if (percentage.isNaN()||percentage==null){
            return new ResponseEntity<>("Missing fees", HttpStatus.BAD_REQUEST);
        }
        if (maxAmount<1){
            return new ResponseEntity<>("Missing amount", HttpStatus.BAD_REQUEST);
        }
        if (payment==null){
            return new ResponseEntity<>("Missing payment", HttpStatus.BAD_REQUEST);
        }
        if (name.isEmpty()){
            return new ResponseEntity<>("Missing name", HttpStatus.BAD_REQUEST);
        }
        if(loanService.existsByName(name)){
            return new ResponseEntity<>("that loan already exists", HttpStatus.BAD_REQUEST);
        }
        Loan loan=new Loan(name,maxAmount,payment,percentage);
        loanService.save(loan);
        return new ResponseEntity<>("Loan created correctly",HttpStatus.CREATED);
    }
}
