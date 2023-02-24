package com.mindhub.homebanking.controllers;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.util.Arrays;

import static com.mindhub.homebanking.utils.Utilitis.randomNumberCard;
import static com.mindhub.homebanking.utils.Utilitis.returnCvvNumber;

@RequestMapping("/api")
@RestController
public class CardController {
    @Autowired
    public CardRepository cardRepository;
    @Autowired
    public ClientRepository clientRepository;
    @RequestMapping(path = "/clients/current/cards", method = RequestMethod.POST)
    public ResponseEntity<Object> newCard(
            Authentication authentication,
            @RequestParam CardType type,
            @RequestParam CardColor color){

        if (clientRepository.findByEmail(authentication.getName()).getCards().stream().filter(card -> card.getColor()==color&&card.getType()==type)!=null){
            return new ResponseEntity<>("you have already the same card", HttpStatus.BAD_REQUEST);
        }

        Card card=new Card(clientRepository.findByEmail(authentication.getName()), type, color,randomNumberCard(cardRepository) , returnCvvNumber(), LocalDate.now(),LocalDate.now().plusYears(5));
        clientRepository.findByEmail(authentication.getName()).addCard(card);
        cardRepository.save(card);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
