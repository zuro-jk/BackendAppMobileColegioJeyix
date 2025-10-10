package com.jeyix.school_jeyix.features.teacher.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jeyix.school_jeyix.core.security.dto.ApiResponse;
import com.jeyix.school_jeyix.features.teacher.dto.teacher.request.TeacherRequest;
import com.jeyix.school_jeyix.features.teacher.dto.teacher.response.TeacherResponse;
import com.jeyix.school_jeyix.features.teacher.service.TeacherService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/teachers")
@RequiredArgsConstructor
public class TeacherController {
    private final TeacherService teacherService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<TeacherResponse>>> getAll() {
        return ResponseEntity.ok(new ApiResponse<>(true, "Lista de profesores", teacherService.getAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TeacherResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Profesor encontrado", teacherService.getById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<TeacherResponse>> create(@Valid @RequestBody TeacherRequest request) {
        return ResponseEntity
                .ok(new ApiResponse<>(true, "Profesor creado exitosamente", teacherService.create(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TeacherResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody TeacherRequest request) {
        return ResponseEntity
                .ok(new ApiResponse<>(true, "Profesor actualizado exitosamente", teacherService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        teacherService.delete(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Profesor eliminado exitosamente", null));
    }
}
