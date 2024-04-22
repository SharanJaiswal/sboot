package com.example.exampleApps.cac.Controller;

import com.example.exampleApps.cac.DTO.PaymentRequest;
import com.example.exampleApps.cac.DTO.PaymentResponse;
import com.example.exampleApps.cac.Service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    @Autowired  // Field injection is not suggested. Instead, use class constructor to assign the instance bean to class private final variables
    PaymentService paymentService;

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponse> getPaymentById(@PathVariable Long id) {

        // map incoming data to request DTO
        PaymentRequest internalRequestObj = new PaymentRequest();
        internalRequestObj.setPaymentId(id);
        
        // pass this internalRequestObj to further layer of processing
        PaymentResponse payment = paymentService.getPaymentDetailsById(internalRequestObj);

        // return the response DTO
        return ResponseEntity.ok(payment);
    }
}