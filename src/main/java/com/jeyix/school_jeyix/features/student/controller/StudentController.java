package com.jeyix.school_jeyix.features.student.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jeyix.school_jeyix.core.security.dto.ApiResponse;
import com.jeyix.school_jeyix.features.student.dto.student.request.StudentRequest;
import com.jeyix.school_jeyix.features.student.dto.student.response.StudentResponse;
import com.jeyix.school_jeyix.features.student.service.StudentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<StudentResponse>>> getAll() {
        return ResponseEntity.ok(new ApiResponse<>(true, "Lista de estudiantes", studentService.getAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<StudentResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Estudiante encontrado", studentService.getById(id)));
    }

    @GetMapping("/parent/{parentId}")
    public ResponseEntity<ApiResponse<List<StudentResponse>>> getByParent(@PathVariable Long parentId) {
        return ResponseEntity
                .ok(new ApiResponse<>(true, "Lista de estudiantes por padre", studentService.getByParent(parentId)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<StudentResponse>> create(@RequestBody StudentRequest request) {
        return ResponseEntity
                .ok(new ApiResponse<>(true, "Estudiante creado exitosamente", studentService.create(request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        studentService.delete(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Estudiante eliminado exitosamente", null));
    }
}
