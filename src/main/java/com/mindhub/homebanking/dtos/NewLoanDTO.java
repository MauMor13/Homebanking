package com.mindhub.homebanking.dtos;
import java.util.ArrayList;
public class NewLoanDTO {
    private String name;
    private Double maxAmount;
    private ArrayList<Integer> payments;
    private float percentage;
    public NewLoanDTO(String name, Double maxAmount, ArrayList<Integer> payments, float percentage) {
        this.name = name;
        this.maxAmount = maxAmount;
        this.payments = payments;
        this.percentage = percentage;
    }
    public String getName() { return name; }
    public double getMaxAmount() { return maxAmount; }
    public ArrayList<Integer> getPayments() { return payments; }
    public float getPercentage() { return percentage; }
}
