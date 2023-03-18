package com.mindhub.homebanking.services.impl;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
@Service
public class ClientServiceImpl implements ClientService {
    @Autowired
    private ClientRepository clientRepository;
    @Override
    public List<Client> findAll() {
        return clientRepository.findAll();
    }
    @Override
    public Client findByEmail(String email) {
       return clientRepository.findByEmail(email);
    }
    @Override
    public Optional<Client> findById(Long id) {
       return clientRepository.findById(id);
    }
    @Override
    public void save(Client client) {
        clientRepository.save(client);
    }
}
