package com.mindhub.homebanking.controllers;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import static com.mindhub.homebanking.utils.Utilitis.GenereteNumber;
import static java.util.stream.Collectors.toList;

@RequestMapping("/api")
@RestController
public class ClientController {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ClientRepository clientRepository;
    @RequestMapping("/clients")
    public List<ClientDTO> getClients(){
        return clientRepository.findAll().stream().map(client -> new ClientDTO(client)).collect(toList());
    }
    @RequestMapping("clients/current")
    public ClientDTO getAll(Authentication authentication) {
        return new ClientDTO(clientRepository.findByEmail(authentication.getName()));
    }
    @RequestMapping("/clients/{id}")
    public ClientDTO getClient(@PathVariable Long id){
        return clientRepository.findById(id).map(client -> new ClientDTO(client)).orElse(null);
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

        Client client=new Client(firstName, lastName, email, passwordEncoder.encode(password));
        client.addAccount(new Account(GenereteNumber(), LocalDateTime.now(),0));
        clientRepository.save(client);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}