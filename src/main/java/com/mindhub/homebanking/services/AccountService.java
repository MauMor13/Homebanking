package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.Account;

import java.util.List;
import java.util.Optional;

public interface AccountService {
    Boolean existsAccountByNumber(String number);
    void save(Account account);
     List<Account> findAll();
    Optional<Account> findById(Long id);
    Account findByNumber(String numberAccount);
}
