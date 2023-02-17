package com.mindhub.homebanking.dtos;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import java.time.LocalDate;
public class CardDTO {
    private Long id;
    private CardType type;
    private CardColor color;
    private String number;
    private int cvv;
    private LocalDate fromDate;
    private LocalDate thruDate;
    private String cardHolder;
    public CardDTO() {}
    public CardDTO(Card card) {
        this.id = card.getId();

        this.type = card.getType();

        this.color = card.getColor();

        this.number = card.getNumber();

        this.cvv = card.getCvv();

        this.fromDate = card.getFromDate();

        this.thruDate = card.getThruDate();

        this.cardHolder = card.getCardHolder();
    }

    public Long getId() {
        return id;
    }
    public CardType getType() {
        return type;
    }
    public CardColor getColor() {
        return color;
    }
    public String getNumber() {
        return number;
    }
    public int getCvv() {
        return cvv;
    }
    public LocalDate getFromDate() {
        return fromDate;
    }
    public LocalDate getThruDate() {
        return thruDate;
    }
    public String getCardHolder() {
        return cardHolder;
    }
}
