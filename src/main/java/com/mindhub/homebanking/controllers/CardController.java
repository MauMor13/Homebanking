package com.mindhub.homebanking.controllers;
import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import static com.mindhub.homebanking.utils.Utilitis.randomNumberCard;
import static com.mindhub.homebanking.utils.Utilitis.returnCvvNumber;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
@RequestMapping("/api")
@RestController
public class CardController {
    @Autowired
    public CardRepository cardRepository;
    @Autowired
    public ClientRepository clientRepository;
    @GetMapping("/clients/current/cards")
    public List<CardDTO> getCurrentAccount(Authentication authentication){
        return clientRepository.findByEmail(authentication.getName()).getCards().stream().map(card -> new CardDTO(card)).collect(toList());
    }
    @PostMapping("/clients/current/cards")
    public ResponseEntity<Object> newCard(
            Authentication authentication,
            @RequestParam CardType type,
            @RequestParam CardColor color){
        Client clientauth=clientRepository.findByEmail(authentication.getName());
        Set<Card> cards=clientauth.getCards().stream().filter(card -> card.getType()==type).collect(toSet());
        if(cards.size()>=3){
            return new ResponseEntity<>("You have already reached the limit of 3 "+type+" cards, you cannot be given another one", HttpStatus.BAD_REQUEST);
        }
        if (clientauth.getCards().stream().anyMatch(card -> card.getColor()==color&&card.getType()==type)){
            return new ResponseEntity<>("you have already the same card", HttpStatus.BAD_REQUEST);
        }
        Card card = new Card(clientauth, type, color,randomNumberCard(cardRepository) , returnCvvNumber(), LocalDate.now(),LocalDate.now().plusYears(5));
        clientauth.addCard(card);
        cardRepository.save(card);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
