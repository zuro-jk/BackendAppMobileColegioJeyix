package com.jeyix.school_jeyix.features.customers.model;

import com.jeyix.school_jeyix.core.model.Auditable;
import com.jeyix.school_jeyix.core.security.model.User;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "customers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@AttributeOverride(name = "id", column = @Column(name = "customer_id"))
public class Customer extends Auditable {

    @OneToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;



}
