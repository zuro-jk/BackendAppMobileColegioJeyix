package com.jeyix.school_jeyix.features.enrollment.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jeyix.school_jeyix.core.security.dto.ApiResponse;
import com.jeyix.school_jeyix.core.security.model.User;
import com.jeyix.school_jeyix.features.enrollment.dto.enrollment.request.EnrollmentRequest;
import com.jeyix.school_jeyix.features.enrollment.dto.enrollment.response.EnrollmentResponse;
import com.jeyix.school_jeyix.features.enrollment.service.EnrollmentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/enrollments")
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<EnrollmentResponse>>> getAllEnrollments() {
        List<EnrollmentResponse> response = enrollmentService.findAll();
        return ResponseEntity.ok(new ApiResponse<>(true, "Lista de matrículas obtenida.", response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EnrollmentResponse>> getEnrollmentById(@PathVariable Long id) {
        EnrollmentResponse response = enrollmentService.findById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Matrícula encontrada.", response));
    }


    @GetMapping("/my-enrollments")
    public ResponseEntity<ApiResponse<List<EnrollmentResponse>>> getMyEnrollments(
            @AuthenticationPrincipal User user) {
        List<EnrollmentResponse> response = enrollmentService.findAllByAuthenticatedParent(user);
        return ResponseEntity.ok(new ApiResponse<>(true, "Matrículas del padre obtenidas.", response));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<EnrollmentResponse>> createEnrollment(
            @Valid @RequestBody EnrollmentRequest request) {
        EnrollmentResponse response = enrollmentService.create(request);
        return new ResponseEntity<>(
                new ApiResponse<>(true, "Matrícula creada exitosamente.", response),
                HttpStatus.CREATED);
    }

}
