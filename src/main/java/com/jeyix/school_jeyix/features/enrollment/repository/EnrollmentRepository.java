package com.jeyix.school_jeyix.features.enrollment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jeyix.school_jeyix.features.enrollment.model.Enrollment;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    boolean existsByStudent_IdAndAcademicYear(Long studentId, String academicYear);
}