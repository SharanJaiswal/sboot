package com.example.exampleApps.cac.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PaymentRequest {

//    @JsonProperty("exact_name_in_json") // to map the non-auto-mapplable entities to specific attribute in the DTO classes. Used in request DTO, or where Json to DTO mapping is required. Names are identified in Spring boot using Jackson or GSON.
    private long paymentId;

    public long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(long paymentId) {
        this.paymentId = paymentId;
    }
}
