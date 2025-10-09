package com.jeyix.school_jeyix.features.student.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jeyix.school_jeyix.features.student.model.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findByParentId(Long parentId);
}