package com.mindhub.homebanking.controllers;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDateTime;
import static com.mindhub.homebanking.models.TransactionType.CREDIT;
import static com.mindhub.homebanking.models.TransactionType.DEBIT;

@RequestMapping("/api")
@RestController
public class TransactionController {
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    ClientRepository clientRepository;
    @Transactional
    @RequestMapping(path = "/transactions",method = RequestMethod.POST)
    public ResponseEntity<Object> newTransactions(
            Authentication authentication,
            @RequestParam Double amount,
            @RequestParam String description,
            @RequestParam String numAccountOrigin,
            @RequestParam String numAccountDestini){
        Client clientAuthen = clientRepository.findByEmail(authentication.getName());
        if(amount.isNaN() || amount == 0)
            return new ResponseEntity<>("Missing amount",HttpStatus.BAD_REQUEST);
        if(description.isEmpty())
            return new ResponseEntity<>("Missing description",HttpStatus.BAD_REQUEST);
        if(numAccountOrigin.isEmpty())
            return new ResponseEntity<>("Missing number account Origin",HttpStatus.BAD_REQUEST);
        if(numAccountDestini.isEmpty())
            return new ResponseEntity<>("Missing number account Destini",HttpStatus.BAD_REQUEST);
        if (numAccountDestini.equals(numAccountOrigin))
            return new ResponseEntity<>("The accounts are the same",HttpStatus.BAD_REQUEST);
        if (!accountRepository.existsAccountByNumber(numAccountDestini))
            return new ResponseEntity<>("Destination account does not exist",HttpStatus.BAD_REQUEST);
        if (clientAuthen.getAccounts().stream().noneMatch(account -> account.getNumber().equals(numAccountOrigin)))
            return new ResponseEntity<>("Source account does not exist",HttpStatus.BAD_REQUEST);
        if (accountRepository.findByNumber(numAccountOrigin).getBalance()<amount)
            return new ResponseEntity<>("The amount in the account is not enough",HttpStatus.BAD_REQUEST);

        Account accountOrigin = accountRepository.findByNumber(numAccountOrigin);
        Account accountDestini = accountRepository.findByNumber(numAccountDestini);
        Transaction transactionOrigin = new Transaction(DEBIT,amount,description, LocalDateTime.now());
        Transaction transactionDestini = new Transaction(CREDIT,amount,description,LocalDateTime.now());
        accountOrigin.addTransaction(transactionOrigin);
        accountDestini.addTransaction(transactionDestini);
        accountOrigin.setBalance(accountOrigin.getBalance()-amount);
        accountDestini.setBalance(accountDestini.getBalance()+amount);
        accountRepository.save(accountOrigin);
        accountRepository.save(accountDestini);
        transactionRepository.save(transactionOrigin);
        transactionRepository.save(transactionDestini);
        return new ResponseEntity<>( "successful transaction", HttpStatus.ACCEPTED);
    }
}
