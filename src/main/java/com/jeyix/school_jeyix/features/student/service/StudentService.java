package com.jeyix.school_jeyix.features.student.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeyix.school_jeyix.core.security.model.User;
import com.jeyix.school_jeyix.core.security.repository.UserRepository;
import com.jeyix.school_jeyix.features.parent.model.Parent;
import com.jeyix.school_jeyix.features.parent.repository.ParentRepository;
import com.jeyix.school_jeyix.features.student.dto.student.request.StudentRequest;
import com.jeyix.school_jeyix.features.student.dto.student.response.StudentResponse;
import com.jeyix.school_jeyix.features.student.model.Student;
import com.jeyix.school_jeyix.features.student.repository.StudentRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final ParentRepository parentRepository;
    private final UserRepository userRepository;

    @Transactional
    public StudentResponse create(StudentRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Parent parent = parentRepository.findById(request.getParentId())
                .orElseThrow(() -> new RuntimeException("Padre no encontrado"));

        Student student = new Student();
        student.setUser(user);
        student.setParent(parent);
        student.setGradeLevel(request.getGradeLevel());
        student.setSection(request.getSection());

        studentRepository.save(student);

        return toResponse(student);
    }

    @Transactional(readOnly = true)
    public List<StudentResponse> getAll() {
        return studentRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<StudentResponse> getByParent(Long parentId) {
        return studentRepository.findByParentId(parentId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public StudentResponse getById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado"));
        return toResponse(student);
    }

    @Transactional
    public StudentResponse update(Long id, StudentRequest request) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Estudiante con ID " + id + " no encontrado."));

        if (!student.getParent().getId().equals(request.getParentId())) {
            Parent newParent = parentRepository.findById(request.getParentId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Padre con ID " + request.getParentId() + " no encontrado."));
            student.setParent(newParent);
        }

        if (!student.getUser().getId().equals(request.getUserId())) {
            User newUser = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Usuario con ID " + request.getUserId() + " no encontrado."));
            student.setUser(newUser);
        }

        student.setGradeLevel(request.getGradeLevel());
        student.setSection(request.getSection());

        Student updatedStudent = studentRepository.save(student);

        return toResponse(updatedStudent);
    }

    @Transactional
    public void delete(Long id) {
        studentRepository.deleteById(id);
    }

    private StudentResponse toResponse(Student s) {
        return StudentResponse.builder()
                .id(s.getId())
                .username(s.getUser().getUsername())
                .fullName(s.getUser().getFirstName() + " " + s.getUser().getLastName())
                .gradeLevel(s.getGradeLevel())
                .section(s.getSection())
                .parentId(s.getParent() != null ? s.getParent().getId() : null)
                .parentName(s.getParent() != null
                        ? s.getParent().getUser().getFirstName() + " " + s.getParent().getUser().getLastName()
                        : null)
                .build();
    }

}
