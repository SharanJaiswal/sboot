package com.example.exampleApps.cac.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PaymentResponse {
    @JsonProperty("payment_id") // During serialization instead of paymentId, the key payment_id  will be sent
    private Long paymentId;
    private double amount;
    private String currency;

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
