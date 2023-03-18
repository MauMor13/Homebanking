package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.Card;

public interface CardService {
    Boolean existsCardByNumber(String numberCard);
    void save(Card card);
    Card findByNumber(String numberCard);
}
