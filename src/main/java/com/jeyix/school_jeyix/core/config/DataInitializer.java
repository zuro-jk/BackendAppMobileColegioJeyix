package com.jeyix.school_jeyix.core.config;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.jeyix.school_jeyix.core.security.enums.AuthProvider;
import com.jeyix.school_jeyix.core.security.model.Role;
import com.jeyix.school_jeyix.core.security.model.User;
import com.jeyix.school_jeyix.core.security.repository.RoleRepository;
import com.jeyix.school_jeyix.core.security.repository.UserRepository;
import com.jeyix.school_jeyix.features.parent.model.Parent;
import com.jeyix.school_jeyix.features.parent.repository.ParentRepository;
import com.jeyix.school_jeyix.features.student.model.Student;
import com.jeyix.school_jeyix.features.student.repository.StudentRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Profile("dev")
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

        private final RoleRepository roleRepository;
        private final UserRepository userRepository;
        private final PasswordEncoder passwordEncoder;
        private final ParentRepository parentRepository;
        private final StudentRepository studentRepository;

        @Override
        public void run(String... args) throws Exception {
                initRoles();
                initAdminUser();
                initParentsAndStudents();
        }

        private void initRoles() {
                String[] roles = {
                                "ROLE_ADMIN",
                                "ROLE_PARENT",
                                "ROLE_STUDENT",
                                "ROLE_TEACHER",
                                "ROLE_ACCOUNTANT"
                };

                for (String roleName : roles) {
                        roleRepository.findByName(roleName)
                                        .orElseGet(() -> roleRepository.save(new Role(null, roleName)));
                }
                log.info(">>> Roles inicializados correctamente");
        }

        private void initAdminUser() {
                if (userRepository.findByUsername("admin").isEmpty()) {
                        Role adminRole = roleRepository.findByName("ROLE_ADMIN").get();

                        User admin = User.builder()
                                        .username("admin")
                                        .email("DarckProyect8@gmail.com")
                                        .firstName("Joe")
                                        .lastName("Luna")
                                        .password(passwordEncoder.encode("admin123"))
                                        .roles(Collections.singleton(adminRole))
                                        .provider(AuthProvider.LOCAL)
                                        .enabled(true)
                                        .build();

                        userRepository.save(admin);
                        log.info(">>> Usuario admin creado: admin / admin123");
                }
        }

        private void initParentsAndStudents() {
                if (parentRepository.count() > 0 || studentRepository.count() > 0) {
                        log.info("ℹ️ Datos de prueba (padres y estudiantes) ya existen, se omite creación.");
                        return;
                }

                Role parentRole = roleRepository.findByName("ROLE_PARENT").get();
                Role studentRole = roleRepository.findByName("ROLE_STUDENT").get();

                // ---- PADRE 1 ----
                User parentUser1 = User.builder()
                                .username("parent1")
                                .email("parent1@example.com")
                                .firstName("Carlos")
                                .lastName("Ramírez")
                                .password(passwordEncoder.encode("parent123"))
                                .roles(Collections.singleton(parentRole))
                                .phone("987654321")
                                .provider(AuthProvider.LOCAL)
                                .enabled(true)
                                .build();

                userRepository.save(parentUser1);

                Parent parent1 = Parent.builder()
                                .user(parentUser1)
                                .build();

                parentRepository.save(parent1);

                // ---- HIJOS DEL PADRE 1 ----
                User studentUser1 = User.builder()
                                .username("student1")
                                .email("student1@example.com")
                                .firstName("Luis")
                                .lastName("Ramírez")
                                .password(passwordEncoder.encode("student123"))
                                .roles(Collections.singleton(studentRole))
                                .provider(AuthProvider.LOCAL)
                                .enabled(true)
                                .build();

                userRepository.save(studentUser1);

                Student s1 = Student.builder()
                                .user(studentUser1)
                                .gradeLevel("3ro Primaria")
                                .parent(parent1)
                                .build();

                User studentUser2 = User.builder()
                                .username("student2")
                                .email("student2@example.com")
                                .firstName("Andrea")
                                .lastName("Ramírez")
                                .password(passwordEncoder.encode("student123"))
                                .roles(Collections.singleton(studentRole))
                                .provider(AuthProvider.LOCAL)
                                .enabled(true)
                                .build();

                userRepository.save(studentUser2);

                Student s2 = Student.builder()
                                .user(studentUser2)
                                .gradeLevel("5to Primaria")
                                .parent(parent1)
                                .build();

                studentRepository.saveAll(Arrays.asList(s1, s2));

                // ---- PADRE 2 ----
                User parentUser2 = User.builder()
                                .username("parent2")
                                .email("parent2@example.com")
                                .firstName("María")
                                .lastName("López")
                                .password(passwordEncoder.encode("parent123"))
                                .roles(Collections.singleton(parentRole))
                                .provider(AuthProvider.LOCAL)
                                .enabled(true)
                                .build();

                userRepository.save(parentUser2);

                Parent parent2 = Parent.builder()
                                .user(parentUser2)
                                .build();

                parentRepository.save(parent2);

                // ---- HIJOS DEL PADRE 2 ----
                User studentUser3 = User.builder()
                                .username("student3")
                                .email("student3@example.com")
                                .firstName("Sofía")
                                .lastName("López")
                                .password(passwordEncoder.encode("student123"))
                                .roles(Collections.singleton(studentRole))
                                .provider(AuthProvider.LOCAL)
                                .enabled(true)
                                .build();

                userRepository.save(studentUser3);

                Student s3 = Student.builder()
                                .user(studentUser3)
                                .gradeLevel("1ro Secundaria")
                                .parent(parent2)
                                .build();

                User studentUser4 = User.builder()
                                .username("student4")
                                .email("student4@example.com")
                                .firstName("Diego")
                                .lastName("López")
                                .password(passwordEncoder.encode("student123"))
                                .roles(Collections.singleton(studentRole))
                                .provider(AuthProvider.LOCAL)
                                .enabled(true)
                                .build();

                userRepository.save(studentUser4);

                Student s4 = Student.builder()
                                .user(studentUser4)
                                .gradeLevel("2do Secundaria")
                                .parent(parent2)
                                .build();

                studentRepository.saveAll(Arrays.asList(s3, s4));

                log.info("✅ Padres, estudiantes y usuarios creados correctamente para entorno dev");
        }
}