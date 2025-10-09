package com.jeyix.school_jeyix.features.payment.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.jeyix.school_jeyix.core.model.Auditable;
import com.jeyix.school_jeyix.features.parent.model.Parent;
import com.jeyix.school_jeyix.features.student.model.Student;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "payments")
@AttributeOverride(name = "id", column = @Column(name = "payment_id"))
public class Payment extends Auditable {

    @ManyToOne(optional = false)
    @JoinColumn(name = "parent_id", nullable = false)
    private Parent parent;

    @ManyToOne(optional = false)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    private BigDecimal amount;
    private LocalDate dueDate;
    private LocalDate paymentDate;
    private String paymentMethod;
    private Boolean paid;
    private String referenceNumber;
}