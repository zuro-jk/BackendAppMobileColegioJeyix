package com.jeyix.school_jeyix.features.teacher.model;

import com.jeyix.school_jeyix.core.model.Auditable;
import com.jeyix.school_jeyix.core.security.model.User;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "teachers")
@AttributeOverride(name = "id", column = @Column(name = "teacher_id"))
public class Teacher extends Auditable {

    @OneToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String specialty;
    private String course;
}