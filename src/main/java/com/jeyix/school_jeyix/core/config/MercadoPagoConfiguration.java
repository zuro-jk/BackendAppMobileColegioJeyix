package com.jeyix.school_jeyix.core.config;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.preference.PreferenceClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MercadoPagoConfiguration {

    @Value("${mercadopago.access-token}")
    private String accessToken;

    @Bean
    public PaymentClient paymentClient() {
        MercadoPagoConfig.setAccessToken(accessToken);
        return new PaymentClient();
    }

    @Bean
    public PreferenceClient preferenceClient() {
        return new PreferenceClient();
    }

}
