package com.jeyix.school_jeyix.features.parent.model;

import java.util.List;

import com.jeyix.school_jeyix.core.model.Auditable;
import com.jeyix.school_jeyix.core.security.model.User;
import com.jeyix.school_jeyix.features.payment.model.Payment;
import com.jeyix.school_jeyix.features.student.model.Student;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "parents")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@AttributeOverride(name = "id", column = @Column(name = "parent_id"))
public class Parent extends Auditable {

    @OneToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Student> children;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Payment> payments;

}
