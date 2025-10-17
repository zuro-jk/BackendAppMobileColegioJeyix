package com.jeyix.school_jeyix.features.parent.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeyix.school_jeyix.core.aws.service.FileService;
import com.jeyix.school_jeyix.core.security.model.User;
import com.jeyix.school_jeyix.core.security.repository.UserRepository;
import com.jeyix.school_jeyix.features.parent.dto.parent.request.ParentRequest;
import com.jeyix.school_jeyix.features.parent.dto.parent.response.ParentDetailResponse;
import com.jeyix.school_jeyix.features.parent.dto.parent.response.ParentResponse;
import com.jeyix.school_jeyix.features.parent.dto.parent.response.StudentSummary;
import com.jeyix.school_jeyix.features.parent.dto.parent.response.UserDetailSummary;
import com.jeyix.school_jeyix.features.parent.dto.parent.response.UserSummary;
import com.jeyix.school_jeyix.features.parent.model.Parent;
import com.jeyix.school_jeyix.features.parent.repository.ParentRepository;
import com.jeyix.school_jeyix.features.student.model.Student;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ParentService {

    private final ParentRepository parentRepository;
    private final UserRepository userRepository;
    private final FileService fileService;

    public List<ParentResponse> findAll() {
        return parentRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ParentResponse findById(Long id) {
        Parent parent = parentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Padre con ID " + id + " no encontrado."));
        return toResponse(parent);
    }

    @Transactional(readOnly = true)
    public ParentDetailResponse findDetailsById(Long id) {
        Parent parent = parentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Padre con ID " + id + " no encontrado."));
        return toDetailResponse(parent);
    }

    @Transactional
    public ParentResponse create(ParentRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(
                        () -> new EntityNotFoundException("Usuario con ID " + request.getUserId() + " no encontrado."));

        if (parentRepository.existsByUser_Id(request.getUserId())) {
            throw new IllegalStateException("Ya existe un padre asociado a este usuario.");
        }

        Parent parent = new Parent();
        parent.setUser(user);

        userRepository.save(user);

        parent = parentRepository.save(parent);

        return toResponse(parent);
    }

    @Transactional
    public ParentResponse update(Long id, ParentRequest request) {
        Parent parent = parentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Padre con ID " + id + " no encontrado."));

        User user = parent.getUser();
        if (!user.getId().equals(request.getUserId())) {
            throw new IllegalArgumentException("No se puede cambiar el usuario asociado a un padre.");
        }

        userRepository.save(user);

        parentRepository.save(parent);

        return toResponse(parent);
    }

    public void delete(Long id) {
        if (!parentRepository.existsById(id)) {
            throw new EntityNotFoundException("Padre con ID " + id + " no encontrado.");
        }
        parentRepository.deleteById(id);
    }

    private ParentResponse toResponse(Parent parent) {
        var user = parent.getUser();
        return ParentResponse.builder()
                .id(parent.getId())
                .user(new UserSummary(
                        user.getId(),
                        user.getUsername(),
                        user.getEmail(),
                        user.getFirstName() + " " + user.getLastName(),
                        user.getPhone(),
                        user.getProfileImageId() != null ? fileService.getFileUrl(user.getProfileImageId()) : null))
                .children(
                        parent.getChildrenSafe().stream()
                                .map(this::toStudentSummary)
                                .collect(Collectors.toList()))
                .build();
    }

    private ParentDetailResponse toDetailResponse(Parent parent) {
        User user = parent.getUser();

        UserDetailSummary userDetail = UserDetailSummary.builder()
                .id(user.getId())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .enabled(user.isEnabled())
                .emailVerified(user.isEmailVerified())
                .profileImageUrl(
                        user.getProfileImageId() != null ? fileService.getFileUrl(user.getProfileImageId()) : null)
                .build();

        List<StudentSummary> children = parent.getChildrenSafe().stream()
                .map(student -> new StudentSummary(
                        student.getId(),
                        student.getUser().getFullName(),
                        student.getGradeLevel()))
                .collect(Collectors.toList());

        return ParentDetailResponse.builder()
                .id(parent.getId())
                .user(userDetail)
                .children(children)
                .build();
    }

    private StudentSummary toStudentSummary(Student student) {
        return new StudentSummary(
                student.getId(),
                student.getUser().getFirstName() + " " + student.getUser().getLastName(),
                student.getGradeLevel());
    }

}
