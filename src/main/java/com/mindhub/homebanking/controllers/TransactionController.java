package com.mindhub.homebanking.controllers;
import com.mindhub.homebanking.dtos.TransactionDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;
import static com.mindhub.homebanking.models.TransactionType.CREDIT;
import static com.mindhub.homebanking.models.TransactionType.DEBIT;
import static java.util.stream.Collectors.toList;

@RequestMapping("/api")
@RestController
public class TransactionController {
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private ClientService clientService;
    @Transactional
    @PostMapping("/transactions")
    public ResponseEntity<Object> newTransactions(
            Authentication authentication,
            @RequestParam(required = false) Double amount,
            @RequestParam String description,
            @RequestParam String numAccountOrigin,
            @RequestParam String numAccountDestini){
        Client clientAuthen = clientService.findByEmail(authentication.getName());
        if(amount == null || amount <= 0)
            return new ResponseEntity<>("Missing amount",HttpStatus.BAD_REQUEST);
        if(description.isEmpty())
            return new ResponseEntity<>("Missing description",HttpStatus.BAD_REQUEST);
        if(numAccountOrigin.isEmpty())
            return new ResponseEntity<>("Missing number account Origin",HttpStatus.BAD_REQUEST);
        if(numAccountDestini.isEmpty())
            return new ResponseEntity<>("Missing number account Destini",HttpStatus.BAD_REQUEST);
        if (numAccountDestini.equals(numAccountOrigin))
            return new ResponseEntity<>("The accounts are the same",HttpStatus.BAD_REQUEST);
        if (!accountService.existsAccountByNumber(numAccountDestini))
            return new ResponseEntity<>("Destination account does not exist",HttpStatus.BAD_REQUEST);
        if (clientAuthen.getAccounts().stream().noneMatch(account -> account.getNumber().equals(numAccountOrigin)))
            return new ResponseEntity<>("Source account does not exist",HttpStatus.BAD_REQUEST);
        if (accountService.findByNumber(numAccountOrigin).getBalance()<amount)
            return new ResponseEntity<>("The amount in the account is not enough",HttpStatus.BAD_REQUEST);

        Account accountOrigin = accountService.findByNumber(numAccountOrigin);
        Account accountDestini = accountService.findByNumber(numAccountDestini);
        Transaction transactionOrigin = new Transaction(DEBIT,amount*-1,description.concat(accountDestini.getNumber()), LocalDateTime.now(),accountOrigin.getBalance()-amount);
        Transaction transactionDestini = new Transaction(CREDIT,amount,description.concat(accountOrigin.getNumber()),LocalDateTime.now(),accountDestini.getBalance()+amount);
        accountOrigin.addTransaction(transactionOrigin);
        accountDestini.addTransaction(transactionDestini);
        accountOrigin.setBalance(accountOrigin.getBalance()-amount);
        accountDestini.setBalance(accountDestini.getBalance()+amount);
        accountService.save(accountOrigin);
        accountService.save(accountDestini);
        transactionService.save(transactionOrigin);
        transactionService.save(transactionDestini);
        return new ResponseEntity<>( "successful transaction", HttpStatus.ACCEPTED);
    }
    @GetMapping("/filter-transactions")
    public ResponseEntity<?> filterTransactions(@RequestParam String fromAccount,
                                                @RequestParam (required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
                                                @RequestParam (required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
                                                @RequestParam (required = false) String description,
                                                @RequestParam (required = false) Double maxAmount,
                                                @RequestParam (required = false) Double minAmount,
                                                @RequestParam (required = false) TransactionType type,
                                                Authentication authentication){
        Account currentAccount = accountService.findByNumber(fromAccount);
        Client client = clientService.findByEmail(authentication.getName());
        Set<Transaction> transaccionesFiltradas = currentAccount.getTransactions();
        if (client.getAccounts().stream().noneMatch(account -> account.getId() == currentAccount.getId()))
            return new ResponseEntity<>("the account does not belong to the authenticated client",HttpStatus.BAD_REQUEST);
        if (!currentAccount.getActive())
            return new ResponseEntity<>("Account not active",HttpStatus.BAD_REQUEST);
        if (startDate == null && endDate != null){
            transaccionesFiltradas = transaccionesFiltradas.stream().filter(transaction -> transaction.getDate().isBefore(endDate) || transaction.getDate().equals(endDate) ).collect(Collectors.toSet()); }
        if (startDate != null && endDate == null){
            transaccionesFiltradas = transaccionesFiltradas.stream().filter(transaction -> transaction.getDate().isAfter(startDate) || transaction.getDate().equals(startDate) ).collect(Collectors.toSet()); }
        if (startDate != null && endDate != null && startDate.isBefore(endDate)){
            transaccionesFiltradas = transaccionesFiltradas.stream().filter(transaction -> transaction.getDate().isAfter(startDate) && transaction.getDate().isBefore(endDate) || transaction.getDate().equals(endDate)  || transaction.getDate().equals(startDate) ).collect(Collectors.toSet()); }
        if (description != null && !description.isEmpty()){
            transaccionesFiltradas = transaccionesFiltradas.stream().filter(transaction -> transaction.getDescription().contains(description)).collect(Collectors.toSet()); }
        if(maxAmount != null && !maxAmount.isNaN() && minAmount == null){
            transaccionesFiltradas = transaccionesFiltradas.stream().filter(transaction -> transaction.getAmount() <= maxAmount).collect(Collectors.toSet()); }
        if(minAmount != null && !minAmount.isNaN() && maxAmount == null){
            transaccionesFiltradas = transaccionesFiltradas.stream().filter(transaction -> transaction.getAmount() >= minAmount).collect(Collectors.toSet()); }
        if(maxAmount != null && !maxAmount.isNaN() && minAmount != null && !minAmount.isNaN()){
            transaccionesFiltradas = transaccionesFiltradas.stream().filter(transaction -> transaction.getAmount() >= minAmount && transaction.getAmount() <= maxAmount).collect(Collectors.toSet()); }
        if(type == TransactionType.CREDIT){
            transaccionesFiltradas = transaccionesFiltradas.stream().filter(transaction -> transaction.getType() == TransactionType.CREDIT).collect(Collectors.toSet()); }
        if (type == TransactionType.DEBIT){
            transaccionesFiltradas =transaccionesFiltradas.stream().filter(transaction -> transaction.getType() == TransactionType.DEBIT).collect(Collectors.toSet()); }

        return ResponseEntity.ok(transaccionesFiltradas.stream().map(TransactionDTO::new).sorted((a,b)->b.getDate().compareTo(a.getDate())).collect(toList())) ;
    }
}
