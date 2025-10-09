package com.jeyix.school_jeyix.features.student.model;

import java.util.List;

import com.jeyix.school_jeyix.core.model.Auditable;
import com.jeyix.school_jeyix.core.security.model.User;
import com.jeyix.school_jeyix.features.enrollment.model.Enrollment;
import com.jeyix.school_jeyix.features.parent.model.Parent;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "students")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@AttributeOverride(name = "id", column = @Column(name = "student_id"))
public class Student extends Auditable {

    @OneToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "parent_id", nullable = false)
    private Parent parent;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Enrollment> enrollments;

    private String gradeLevel;
    private String section;
}