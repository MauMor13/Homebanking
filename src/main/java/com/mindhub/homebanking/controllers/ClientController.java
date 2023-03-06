package com.mindhub.homebanking.controllers;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.mindhub.homebanking.utils.Utilitis.GenereteNumber;
import static java.util.stream.Collectors.toList;
@RequestMapping("/api")
@RestController
public class ClientController {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private AccountRepository accountRepository;
    @RequestMapping("/clients")
    public List<ClientDTO> getClients(){
        return clientRepository.findAll().stream().map(client -> new ClientDTO(client)).collect(toList());
    }
    @RequestMapping("clients/current")
    public ClientDTO getCurrentClient(Authentication authentication) {
        return new ClientDTO(clientRepository.findByEmail(authentication.getName()));
    }
    @RequestMapping("/clients/{id}")
    public Optional<ClientDTO> getClient(@PathVariable Long id){
        return clientRepository.findById(id).map(client -> new ClientDTO(client));
    }
    @RequestMapping(path = "/clients", method = RequestMethod.POST)
    public ResponseEntity<Object> register(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String email,
            @RequestParam String password) {
        if (firstName.isEmpty())
            return new ResponseEntity<>("Missing firstname", HttpStatus.BAD_REQUEST);
        else if (lastName.isEmpty())
            return new ResponseEntity<>("Missing lastname", HttpStatus.BAD_REQUEST);
        else if (email.isEmpty() )
            return new ResponseEntity<>("Missing email", HttpStatus.BAD_REQUEST);
        else if (password.isEmpty())
            return new ResponseEntity<>("Missing password", HttpStatus.BAD_REQUEST);
        if (clientRepository.findByEmail(email) !=  null)
            return new ResponseEntity<>("Email already in use", HttpStatus.FORBIDDEN);
        Account newAccount=new Account(GenereteNumber(accountRepository), LocalDateTime.now(),0);
        Client newClient=new Client(firstName, lastName, email, passwordEncoder.encode(password));
        newClient.addAccount(newAccount);
        clientRepository.save(newClient);
        accountRepository.save(newAccount);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}