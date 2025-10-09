package com.jeyix.school_jeyix.features.parent.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
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
import com.jeyix.school_jeyix.features.parent.dto.parent.request.ParentRequest;
import com.jeyix.school_jeyix.features.parent.dto.parent.response.ParentResponse;
import com.jeyix.school_jeyix.features.parent.service.ParentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/parents")
@RequiredArgsConstructor
public class ParentController {

     private final ParentService parentService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ParentResponse>>> getAll() {
        return ResponseEntity.ok(new ApiResponse<>(true, "Lista de padres obtenida.", parentService.findAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ParentResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Padre encontrado.", parentService.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ParentResponse>> create(@Valid @RequestBody ParentRequest request) {
        ParentResponse createdParent = parentService.create(request);
        return new ResponseEntity<>(
            new ApiResponse<>(true, "Padre creado exitosamente.", createdParent), 
            HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ParentResponse>> update(@PathVariable Long id,
            @Valid @RequestBody ParentRequest request) {
        return ResponseEntity
                .ok(new ApiResponse<>(true, "Padre actualizado exitosamente.", parentService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> delete(@PathVariable Long id) {
        parentService.delete(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Padre eliminado exitosamente.", null));
    }

}
