package com.jeyix.school_jeyix.features.payment.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeyix.school_jeyix.features.payment.dto.customer.response.PaymentPreferenceResponse;
import com.jeyix.school_jeyix.features.payment.model.Payment;
import com.jeyix.school_jeyix.features.payment.repository.PaymentRepository;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.client.preference.PreferenceRequest.PreferenceRequestBuilder;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.preference.Preference;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentGatewayService {

    private final PreferenceClient preferenceClient;
    private final PaymentClient paymentClient;
    private final PaymentRepository paymentRepository;

    @Value("${app.android.deeplink.success}")
    private String successDeepLink;

    @Value("${app.android.deeplink.failure}")
    private String failureDeepLink;

    @Value("${myapp.backend.url.notification:#{null}}")
    private String notificationUrl;

    @Value("${mercadopago.public-key}")
    private String mercadoPagoPublicKey;

    @Transactional
    public PaymentPreferenceResponse createPaymentPreference(Long paymentId) throws MPException, MPApiException {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new EntityNotFoundException("Pago con ID " + paymentId + " no encontrado."));

        if (payment.getPaid()) {
            throw new IllegalStateException("Este pago ya ha sido realizado.");
        }

        PreferenceItemRequest itemRequest = PreferenceItemRequest.builder()
                .id(payment.getId().toString())
                .title("Pago de cuota escolar")
                .description(
                        "Cuota para la matrícula de " + payment.getEnrollment().getStudent().getUser().getFirstName())
                .quantity(1)
                .currencyId("PEN")
                .unitPrice(payment.getAmount())
                .build();

        PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest.builder()
                .success(successDeepLink)
                .failure(failureDeepLink)
                .pending("")
                .build();

        PreferenceRequestBuilder requestBuilder = PreferenceRequest.builder()
                .items(List.of(itemRequest))
                .backUrls(backUrls)
                .externalReference(payment.getId().toString());

        if (notificationUrl != null && !notificationUrl.isBlank()) {
            requestBuilder.notificationUrl(notificationUrl);
        }

        PreferenceRequest preferenceRequest = requestBuilder.build();

        Preference preference = preferenceClient.create(preferenceRequest);

        return PaymentPreferenceResponse.builder()
                .preferenceId(preference.getId())
                .publicKey(mercadoPagoPublicKey)
                .build();
    }

    @Transactional
    public void handleWebhookNotification(Map<String, Object> notification) {
        log.info("Webhook recibido: {}", notification);
        String topic = (String) notification.get("topic");
        String action = (String) notification.get("action");

        if ("payment".equals(topic) || (action != null && action.startsWith("payment."))) {
            Map<String, Object> data = (Map<String, Object>) notification.get("data");
            String mpPaymentIdStr = (String) data.get("id");

            try {
                com.mercadopago.resources.payment.Payment mpPayment = paymentClient.get(Long.parseLong(mpPaymentIdStr));

                String internalPaymentId = mpPayment.getExternalReference();
                Payment localPayment = paymentRepository.findById(Long.parseLong(internalPaymentId))
                        .orElseThrow(
                                () -> new EntityNotFoundException("Pago interno no encontrado: " + internalPaymentId));

                if ("approved".equals(mpPayment.getStatus()) && !localPayment.getPaid()) {
                    localPayment.setPaid(true);
                    localPayment.setPaymentDate(LocalDate.now());
                    localPayment.setPaymentMethod("MercadoPago");
                    localPayment.setReferenceNumber(mpPaymentIdStr);
                    paymentRepository.save(localPayment);
                    log.info("Pago ID {} actualizado a pagado.", localPayment.getId());
                } else {
                    log.warn("El pago {} no fue aprobado o ya estaba pagado. Estado: {}", mpPayment.getId(),
                            mpPayment.getStatus());
                }

            } catch (Exception e) {
                log.error("Error procesando webhook de Mercado Pago", e);
            }
        }
    }

}
