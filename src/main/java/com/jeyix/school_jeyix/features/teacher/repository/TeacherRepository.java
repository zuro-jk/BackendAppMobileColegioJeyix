package com.jeyix.school_jeyix.features.teacher.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jeyix.school_jeyix.features.teacher.model.Teacher;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
}