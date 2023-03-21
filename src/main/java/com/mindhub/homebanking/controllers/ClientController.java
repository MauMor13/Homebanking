package com.mindhub.homebanking.controllers;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.mindhub.homebanking.models.AccountType.SAVING;
import static com.mindhub.homebanking.utils.Utilitis.GenereteNumber;
import static java.util.stream.Collectors.toList;
@RequestMapping("/api")
@RestController
public class ClientController {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ClientService clientService;
    @Autowired
    private AccountService accountService;
    @GetMapping("/clients")
    public List<ClientDTO> getClients(){
        return clientService.findAll().stream().map(ClientDTO::new).collect(toList());
    }
    @GetMapping("clients/current")
    public ClientDTO getCurrentClient(Authentication authentication) {
        return new ClientDTO(clientService.findByEmail(authentication.getName()));
    }
    @GetMapping("/clients/{id}")
    public Optional<ClientDTO> getClient(@PathVariable Long id){
        return clientService.findById(id).map(ClientDTO::new);
    }
    @PostMapping("/clients")
    public ResponseEntity<Object> register(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String email,
            @RequestParam String password) {
        if (firstName.isEmpty())
            return new ResponseEntity<>("Missing firstname", HttpStatus.BAD_REQUEST);
        else if (lastName.isEmpty())
            return new ResponseEntity<>("Missing lastname", HttpStatus.BAD_REQUEST);
        else if (email.isEmpty())
            return new ResponseEntity<>("Missing email", HttpStatus.BAD_REQUEST);
        else if (password.isEmpty())
            return new ResponseEntity<>("Missing password", HttpStatus.BAD_REQUEST);
        if (clientService.findByEmail(email) != null)
            return new ResponseEntity<>("Email already in use", HttpStatus.FORBIDDEN);
        Account newAccount = new Account(GenereteNumber(accountService), LocalDateTime.now(), 0, SAVING);
        Client newClient = new Client(firstName, lastName, email, passwordEncoder.encode(password));
        newClient.addAccount(newAccount);
        clientService.save(newClient);
        accountService.save(newAccount);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}