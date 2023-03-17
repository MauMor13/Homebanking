package com.mindhub.homebanking.dtos;

import java.time.LocalDateTime;

public class NewPayDTO {
    private String number;
    private Integer cvv;
    private double amount;
    private String description;

    public NewPayDTO(NewPayDTO newPayDTO) {
        this.number = newPayDTO.getNumber(); ;
        this.cvv = newPayDTO.getCvv();
        this.amount = newPayDTO.getAmount();
        this.description = newPayDTO.getDescription();
    }
    public String getNumber() { return number; }
    public Integer getCvv() { return cvv; }
    public Double getAmount() { return amount; }
    public String getDescription() { return description; }
}
