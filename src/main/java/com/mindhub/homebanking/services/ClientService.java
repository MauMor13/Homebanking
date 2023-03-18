package com.mindhub.homebanking.services;
import com.mindhub.homebanking.models.Client;
import java.util.List;
import java.util.Optional;

public interface ClientService {
    List<Client> findAll();
    Client findByEmail(String email);
    Optional<Client> findById(Long id);
    void save(Client client);
}
