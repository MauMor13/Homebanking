package com.mindhub.homebanking.dtos;
import java.util.List;

public class NewLoanDTO {
    private String name;
    private Double maxAmount;
    private List<Integer> payments;
    private float percentage;
    public NewLoanDTO(String name, Double maxAmount, List<Integer> payments, float percentage) {
        this.name = name;
        this.maxAmount = maxAmount;
        this.payments = payments;
        this.percentage = percentage;
    }
    public String getName() { return name; }
    public double getMaxAmount() { return maxAmount; }
    public List<Integer> getPayments() { return payments; }
    public float getPercentage() { return percentage; }
}
