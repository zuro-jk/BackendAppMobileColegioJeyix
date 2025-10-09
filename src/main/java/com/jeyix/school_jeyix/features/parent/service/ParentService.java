package com.jeyix.school_jeyix.features.parent.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.jeyix.school_jeyix.core.security.model.User;
import com.jeyix.school_jeyix.core.security.repository.UserRepository;
import com.jeyix.school_jeyix.features.parent.dto.parent.request.ParentRequest;
import com.jeyix.school_jeyix.features.parent.dto.parent.response.ParentResponse;
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

    public List<ParentResponse> findAll() {
        return parentRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public ParentResponse findById(Long id) {
        Parent parent = parentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Parent not found"));
        return toResponse(parent);
    }

    public ParentResponse create(ParentRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Parent parent = new Parent();
        parent.setUser(user);
        parent = parentRepository.save(parent);

        return toResponse(parent);
    }

    public ParentResponse update(Long id, ParentRequest request) {
        Parent parent = parentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Parent not found"));

        parentRepository.save(parent);
        return toResponse(parent);
    }

    public void delete(Long id) {
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
                        user.getFirstName() + " " + user.getLastName()))
                .childrenIds(
                        parent.getChildren() == null ? List.of()
                                : parent.getChildren().stream().map(Student::getId).collect(Collectors.toList()))
                .build();
    }

}
