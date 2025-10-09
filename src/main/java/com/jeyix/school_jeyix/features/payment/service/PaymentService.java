package com.jeyix.school_jeyix.features.payment.service;

import org.springframework.stereotype.Service;

import com.jeyix.school_jeyix.features.payment.repository.PaymentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

}
