package com.jeyix.school_jeyix.features.payment.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jeyix.school_jeyix.core.security.dto.ApiResponse;
import com.jeyix.school_jeyix.features.payment.dto.customer.request.PaymentInitiationRequest;
import com.jeyix.school_jeyix.features.payment.dto.customer.response.PaymentPreferenceResponse;
import com.jeyix.school_jeyix.features.payment.service.PaymentGatewayService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

    private final PaymentGatewayService paymentGatewayService;

    @PostMapping("/initiate")
    public ResponseEntity<ApiResponse<PaymentPreferenceResponse>> initiatePayment(
            @Valid @RequestBody PaymentInitiationRequest request) {
        try {
            PaymentPreferenceResponse response = paymentGatewayService.createPaymentPreference(request.getPaymentId());
            return ResponseEntity.ok(new ApiResponse<>(true, "Preferencia de pago creada para Android.", response));
        } catch (Exception e) {
            log.error("Error al crear preferencia de pago", e);
            return new ResponseEntity<>(
                    new ApiResponse<>(false, "Error al crear preferencia de pago: " + e.getMessage(), null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/webhook/mercadopago")
    public ResponseEntity<Void> mercadoPagoWebhook(@RequestBody Map<String, Object> notification) {
        paymentGatewayService.handleWebhookNotification(notification);
        return ResponseEntity.ok().build();
    }

}
