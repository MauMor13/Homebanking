package com.mindhub.homebanking.services.impl;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.services.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class CardServiceImpl implements CardService {
    @Autowired
    private CardRepository cardRepository;
    @Override
    public Boolean existsCardByNumber(String numberCard) {
        return cardRepository.existsCardByNumber(numberCard);
    }
    @Override
    public void save(Card card) {
        cardRepository.save(card);
    }
    @Override
    public Card findByNumber(String numberCard) {
        return cardRepository.findByNumber(numberCard);
    }
}
