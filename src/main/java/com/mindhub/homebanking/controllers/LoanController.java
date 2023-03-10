package com.mindhub.homebanking.controllers;
import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
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
    ClientRepository clientRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    LoanRepository loanRepository;
    @Autowired
    ClientLoanRepository clientLoanRepository;
    @Transactional
    @GetMapping("/loans")
    public List<LoanDTO> getLoans(){
        return loanRepository.findAll().stream().map( loan -> new LoanDTO(loan)).collect(toList());
    }
    @PostMapping("/loans")
    public ResponseEntity<Object> newLoan(
            Authentication authentication,
            @RequestBody(required = false) LoanApplicationDTO loanApplicationDTO){
        Client clientAuthen = clientRepository.findByEmail(authentication.getName());
        Loan loan= loanRepository.findById(loanApplicationDTO.getId()).orElse(null);
        if (loanApplicationDTO.getId() == null || loanApplicationDTO.getId() == 0)
            return new ResponseEntity<>("wrong id value",HttpStatus.FORBIDDEN);
        if (loanApplicationDTO.getAmount() == null || loanApplicationDTO.getAmount() < 10000)
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
        if (!accountRepository.existsAccountByNumber(loanApplicationDTO.getNumberAccountDestini()))
            return new ResponseEntity<>("the account does not exist",HttpStatus.FORBIDDEN);
        if (clientAuthen.getAccounts().stream().noneMatch(account -> account.getNumber().equals(loanApplicationDTO.getNumberAccountDestini())))
            return new ResponseEntity<>("the destination account is not the authenticated client",HttpStatus.FORBIDDEN);

        ClientLoan clientLoan = new ClientLoan(loanApplicationDTO.getAmount() + loanApplicationDTO.getAmount() * 1.20 , loanApplicationDTO.getPayments());
        Transaction transaction = new Transaction(CREDIT,loanApplicationDTO.getAmount(), loan.getName().concat(" loan approved"), LocalDateTime.now());
        Account accountDestini = accountRepository.findByNumber(loanApplicationDTO.getNumberAccountDestini());
        accountDestini.setBalance(accountDestini.getBalance() + loanApplicationDTO.getAmount());
        clientAuthen.addClientLoan(clientLoan);
        loan.addClientLoan(clientLoan);
        accountDestini.addTransaction(transaction);
        clientLoanRepository.save(clientLoan);
        clientRepository.save(clientAuthen);
        loanRepository.save(loan);
        transactionRepository.save(transaction);
        accountRepository.save(accountDestini);
        return new ResponseEntity<>("Create Loan", HttpStatus.CREATED);
    }
}
