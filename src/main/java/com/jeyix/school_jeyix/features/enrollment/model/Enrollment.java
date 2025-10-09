package com.jeyix.school_jeyix.features.enrollment.model;

import java.math.BigDecimal;
import java.util.List;

import com.jeyix.school_jeyix.core.model.Auditable;
import com.jeyix.school_jeyix.features.enrollment.enums.EnrollmentStatus;
import com.jeyix.school_jeyix.features.payment.model.Payment;
import com.jeyix.school_jeyix.features.student.model.Student;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
@Table(name = "enrollments")
@AttributeOverride(name = "id", column = @Column(name = "enrollment_id"))
public class Enrollment extends Auditable {

    @ManyToOne(optional = false)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @OneToMany(mappedBy = "enrollment", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Payment> payments;

    private String academicYear;
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    private EnrollmentStatus status;

}
