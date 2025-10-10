package com.jeyix.school_jeyix.features.enrollment.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.jeyix.school_jeyix.core.security.model.User;
import com.jeyix.school_jeyix.features.enrollment.dto.enrollment.request.EnrollmentRequest;
import com.jeyix.school_jeyix.features.enrollment.dto.enrollment.response.EnrollmentResponse;
import com.jeyix.school_jeyix.features.enrollment.dto.enrollment.response.PaymentSummary;
import com.jeyix.school_jeyix.features.enrollment.enums.EnrollmentStatus;
import com.jeyix.school_jeyix.features.enrollment.model.Enrollment;
import com.jeyix.school_jeyix.features.enrollment.repository.EnrollmentRepository;
import com.jeyix.school_jeyix.features.parent.dto.parent.response.StudentSummary;
import com.jeyix.school_jeyix.features.parent.model.Parent;
import com.jeyix.school_jeyix.features.parent.repository.ParentRepository;
import com.jeyix.school_jeyix.features.payment.model.Payment;
import com.jeyix.school_jeyix.features.student.model.Student;
import com.jeyix.school_jeyix.features.student.repository.StudentRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EnrollmentService {
    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final ParentRepository parentRepository;

    public EnrollmentResponse findById(Long id) {
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Matrícula con ID " + id + " no encontrada."));
        return toResponse(enrollment);
    }

    public List<EnrollmentResponse> findAll() {
        return enrollmentRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<EnrollmentResponse> findAllByAuthenticatedParent(User user) {
        String username = user.getUsername();

        Parent parent = parentRepository.findByUser_Username(username)
                .orElseThrow(
                        () -> new EntityNotFoundException("No se encontró un padre asociado al usuario: " + username));

        List<Enrollment> enrollments = enrollmentRepository.findAllByStudent_Parent_Id(parent.getId());

        return enrollments.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public EnrollmentResponse create(EnrollmentRequest request) {
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Estudiante con ID " + request.getStudentId() + " no encontrado."));

        if (enrollmentRepository.existsByStudent_IdAndAcademicYear(request.getStudentId(), request.getAcademicYear())) {
            throw new IllegalStateException(
                    "El estudiante ya tiene una matrícula para el año académico " + request.getAcademicYear());
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setAcademicYear(request.getAcademicYear());
        enrollment.setTotalAmount(request.getTotalAmount());
        enrollment.setStatus(EnrollmentStatus.PENDING_PAYMENT);

        List<Payment> payments = generateInstallments(enrollment, request.getTotalAmount(),
                request.getNumberOfInstallments());
        enrollment.setPayments(payments);

        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);

        return toResponse(savedEnrollment);
    }

    private List<Payment> generateInstallments(Enrollment enrollment, BigDecimal totalAmount,
            int numberOfInstallments) {
        List<Payment> payments = new ArrayList<>();
        BigDecimal installmentAmount = totalAmount.divide(new BigDecimal(numberOfInstallments), 2,
                RoundingMode.HALF_UP);

        LocalDate dueDate = LocalDate.now().withDayOfMonth(1).plusMonths(1);

        for (int i = 0; i < numberOfInstallments; i++) {
            Payment payment = new Payment();
            payment.setEnrollment(enrollment);
            payment.setAmount(installmentAmount);
            payment.setDueDate(dueDate.plusMonths(i));
            payment.setPaid(false);
            payments.add(payment);
        }

        BigDecimal totalCalculated = installmentAmount.multiply(new BigDecimal(numberOfInstallments));
        BigDecimal difference = totalAmount.subtract(totalCalculated);
        if (difference.compareTo(BigDecimal.ZERO) != 0) {
            Payment lastPayment = payments.get(payments.size() - 1);
            lastPayment.setAmount(lastPayment.getAmount().add(difference));
        }

        return payments;
    }

    private EnrollmentResponse toResponse(Enrollment enrollment) {
        return EnrollmentResponse.builder()
                .id(enrollment.getId())
                .student(toStudentSummary(enrollment.getStudent()))
                .academicYear(enrollment.getAcademicYear())
                .totalAmount(enrollment.getTotalAmount())
                .status(enrollment.getStatus())
                .payments(enrollment.getPayments().stream().map(this::toPaymentSummary).collect(Collectors.toList()))
                .build();
    }

    private StudentSummary toStudentSummary(Student student) {
        var user = student.getUser();
        return new StudentSummary(
                student.getId(),
                user.getFirstName() + " " + user.getLastName(),
                student.getGradeLevel());
    }

    private PaymentSummary toPaymentSummary(Payment payment) {
        return PaymentSummary.builder()
                .id(payment.getId())
                .amount(payment.getAmount())
                .dueDate(payment.getDueDate())
                .isPaid(payment.getPaid())
                .build();
    }
}
