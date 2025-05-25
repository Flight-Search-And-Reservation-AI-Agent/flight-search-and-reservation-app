package com.mycompany.flightapp.controller;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/payment")
public class PaymentOrderController {

    @PostMapping("/order")
    public ResponseEntity<Map<String, String>> createPaymentOrder(@RequestBody Map<String, Object> request) {
        try {
            RazorpayClient razorpay = new RazorpayClient("YOUR_KEY_ID", "YOUR_SECRET");

            int amount = (int) request.get("amount");

            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", amount); // in paise
            orderRequest.put("currency", "INR");
            orderRequest.put("receipt", UUID.randomUUID().toString());

            Order order = razorpay.orders.create(orderRequest);

            Map<String, String> response = new HashMap<>();
            response.put("orderId", order.get("id"));
            response.put("amount", order.get("amount").toString());
            response.put("currency", order.get("currency"));

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Failed to create Razorpay order"));
        }
    }

}

