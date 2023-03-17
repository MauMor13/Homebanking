package com.mindhub.homebanking.dtos;
import java.util.ArrayList;
public class NewLoanDTO {
    private String name;
    private Double maxAmount;
    private ArrayList<Integer> payments;
    private float percentage;
    public NewLoanDTO(NewLoanDTO newLoanDTO) {
        this.name = newLoanDTO.getName();

        this.maxAmount = newLoanDTO.getMaxAmount();

        this.payments = newLoanDTO.getPayments();

        this.percentage = newLoanDTO.getPercentage();
    }
    public String getName() { return name; }
    public double getMaxAmount() { return maxAmount; }
    public ArrayList<Integer> getPayments() { return payments; }
    public float getPercentage() { return percentage; }
}
