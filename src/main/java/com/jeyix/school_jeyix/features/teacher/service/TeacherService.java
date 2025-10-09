package com.jeyix.school_jeyix.features.teacher.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeyix.school_jeyix.core.security.model.User;
import com.jeyix.school_jeyix.core.security.repository.UserRepository;
import com.jeyix.school_jeyix.features.teacher.dto.teacher.request.TeacherRequest;
import com.jeyix.school_jeyix.features.teacher.dto.teacher.response.TeacherResponse;
import com.jeyix.school_jeyix.features.teacher.model.Teacher;
import com.jeyix.school_jeyix.features.teacher.repository.TeacherRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TeacherService {

    private final TeacherRepository teacherRepository;
    private final UserRepository userRepository;

    @Transactional
    public TeacherResponse create(TeacherRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Teacher teacher = new Teacher();
        teacher.setUser(user);
        teacher.setSpecialty(request.getSpecialty());
        teacher.setCourse(request.getCourse());

        teacherRepository.save(teacher);

        return toResponse(teacher);
    }

    @Transactional(readOnly = true)
    public List<TeacherResponse> getAll() {
        return teacherRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TeacherResponse getById(Long id) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Profesor no encontrado"));
        return toResponse(teacher);
    }

    @Transactional
    public TeacherResponse update(Long id, TeacherRequest request) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Profesor no encontrado"));

        teacher.setSpecialty(request.getSpecialty());
        teacher.setCourse(request.getCourse());

        teacherRepository.save(teacher);

        return toResponse(teacher);
    }

    @Transactional
    public void delete(Long id) {
        teacherRepository.deleteById(id);
    }

    private TeacherResponse toResponse(Teacher teacher) {
        return TeacherResponse.builder()
                .id(teacher.getId())
                .username(teacher.getUser().getUsername())
                .fullName(teacher.getUser().getFirstName() + " " + teacher.getUser().getLastName())
                .specialty(teacher.getSpecialty())
                .course(teacher.getCourse())
                .build();
    }

}
