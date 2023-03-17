package com.mindhub.homebanking.controllers;
import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

import static com.mindhub.homebanking.models.AccountType.SAVING;
import static com.mindhub.homebanking.utils.Utilitis.GenereteNumber;
import static java.util.stream.Collectors.toList;

@RequestMapping("/api")
@RestController
public class AccountController {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ClientRepository clientRepository;
    @GetMapping("/accounts")
    public List<AccountDTO> getAccounts(){
        return accountRepository.findAll().stream().map(AccountDTO::new).collect(toList());
    }
    @GetMapping("/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id){
        return accountRepository.findById(id).map(AccountDTO::new).orElse(null);
    }
    @GetMapping("/clients/current/accounts")
    public List<AccountDTO> getCurrentAccount(Authentication authentication){
        return clientRepository.findByEmail(authentication.getName()).getAccountsActive().stream().map(AccountDTO::new).collect(toList());
    }
    @PostMapping("/clients/current/accounts")//probar servlet
    public ResponseEntity<Object> newAccount (
            Authentication authentication,
            @RequestParam AccountType accountType) {
        Client client=clientRepository.findByEmail(authentication.getName());
         if (client.getAccounts().size()>=3) {
            return new ResponseEntity<>("you have max account", HttpStatus.CONFLICT);
         }
        Account newAccount = new Account(GenereteNumber(accountRepository),LocalDateTime.now(),0,accountType);
        client.addAccount(newAccount);
        accountRepository.save(newAccount);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @PatchMapping ("/accounts-delete")//nuevo servlet
    public ResponseEntity<Object> deleteAccounts(
            Authentication authentication,
            @RequestParam String numberAccount){
        Client clientauth=clientRepository.findByEmail(authentication.getName());
        if (!accountRepository.existsAccountByNumber(numberAccount))
            return new ResponseEntity<>("The account number does not exist",HttpStatus.BAD_REQUEST);
        if (clientauth.getAccountsActive().stream().noneMatch(account -> account.getNumber().equals(numberAccount)))
            return new ResponseEntity<>("The account does not belong to the customer",HttpStatus.BAD_REQUEST);

        Account account = accountRepository.findByNumber(numberAccount);
        account.setActive(false);
        accountRepository.save(account);
        return new ResponseEntity<>("Account removed successfully",HttpStatus.ACCEPTED);
    }
}
