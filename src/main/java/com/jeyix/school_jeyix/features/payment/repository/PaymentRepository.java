package com.jeyix.school_jeyix.features.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jeyix.school_jeyix.features.payment.model.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

}
