package com.mindhub.homebanking.controllers;
import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
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
    private AccountService accountService;
    @Autowired
    private ClientService clientService;
    @GetMapping("/accounts")
    public List<AccountDTO> getAccounts(){
        return accountService.findAll().stream().map(AccountDTO::new).collect(toList());
    }
    @GetMapping("/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id){
        return accountService.findById(id).map(AccountDTO::new).orElse(null);
    }
    @GetMapping("/clients/current/accounts")
    public List<AccountDTO> getCurrentAccount(Authentication authentication){
        return clientService.findByEmail(authentication.getName()).getAccountsActive().stream().map(AccountDTO::new).collect(toList());
    }
    @PostMapping("/clients/current/accounts")
    public ResponseEntity<Object> newAccount (
            Authentication authentication,
            @RequestParam AccountType accountType) {
        Client client=clientService.findByEmail(authentication.getName());
         if (client.getAccountsActive().size()>=3) {
            return new ResponseEntity<>("you have max account", HttpStatus.CONFLICT);
         }
        Account newAccount = new Account(GenereteNumber(accountService),LocalDateTime.now(),0,accountType);
        client.addAccount(newAccount);
        accountService.save(newAccount);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @PatchMapping ("/account-delete")
    public ResponseEntity<Object> deleteAccounts(
            Authentication authentication,
            @RequestParam String numberAccount){
        Client clientauth=clientService.findByEmail(authentication.getName());
        if (!accountService.existsAccountByNumber(numberAccount))
            return new ResponseEntity<>("The account number does not exist",HttpStatus.BAD_REQUEST);
        if (clientauth.getAccountsActive().stream().noneMatch(account -> account.getNumber().equals(numberAccount)))
            return new ResponseEntity<>("The account does not belong to the customer",HttpStatus.BAD_REQUEST);

        Account account = accountService.findByNumber(numberAccount);
        account.setActive(false);
        accountService.save(account);
        return new ResponseEntity<>("Account removed successfully",HttpStatus.ACCEPTED);
    }
}
