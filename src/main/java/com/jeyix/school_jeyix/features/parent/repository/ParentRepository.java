package com.jeyix.school_jeyix.features.parent.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jeyix.school_jeyix.features.parent.model.Parent;

@Repository
public interface ParentRepository extends JpaRepository<Parent, Long>{

    Optional<Parent> findByUser_Id(Long userId);
    Optional<Parent> findByUser_Username(String username);
    boolean existsByUser_Id(Long userId);

}
