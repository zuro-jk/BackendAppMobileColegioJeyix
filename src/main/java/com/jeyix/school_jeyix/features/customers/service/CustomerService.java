package com.jeyix.school_jeyix.features.customers.service;

import com.jeyix.school_jeyix.core.dto.response.PagedResponse;
import com.jeyix.school_jeyix.core.exceptions.CustomerAlreadyExistsException;
import com.jeyix.school_jeyix.core.exceptions.ResourceNotFoundException;
import com.jeyix.school_jeyix.core.security.model.User;
import com.jeyix.school_jeyix.core.security.repository.UserRepository;
import com.jeyix.school_jeyix.features.customers.dto.customer.request.CustomerRequest;
import com.jeyix.school_jeyix.features.customers.dto.customer.response.CustomerResponse;
import com.jeyix.school_jeyix.features.customers.model.Customer;
import com.jeyix.school_jeyix.features.customers.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;

    /* -------------------- Clientes -------------------- */

    @Transactional(readOnly = true)
    public PagedResponse<CustomerResponse> getAllCustomers(Pageable pageable) {
        Page<Customer> page = customerRepository.findAll(pageable);
        List<CustomerResponse> content = page.getContent().stream()
                .map(this::mapToResponse)
                .toList();
        log.debug("Se obtuvieron {} clientes", content.size());
        return buildPagedResponse(page, content);
    }

    @Transactional(readOnly = true)
    public CustomerResponse getCustomer(Long id) {
        Customer customer = findCustomerById(id);
        log.debug("Cliente obtenido: {}", customer.getId());
        return mapToResponse(customer);
    }

    @Transactional
    public CustomerResponse createCustomer(CustomerRequest dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        if (customerRepository.existsByUser_Id(user.getId())) {
            throw new CustomerAlreadyExistsException("El usuario ya tiene un cliente asociado");
        }

        Customer customer = Customer.builder()
                .user(user)
                .build();

        Customer saved = customerRepository.save(customer);
        log.info("Cliente creado: {} ({})", saved.getId(), user.getUsername());

        return mapToResponse(saved);
    }

    @Transactional
    public CustomerResponse updateCustomer(Long id, CustomerRequest dto) {
        Customer customer = findCustomerById(id);

        // Actualización solo de campos permitidos (puntos se maneja solo por PointsHistoryService)
        Customer updated = customerRepository.save(customer);
        log.info("Cliente actualizado: {}", updated.getId());
        return mapToResponse(updated);
    }

    @Transactional
    public void deleteCustomer(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cliente no encontrado");
        }
        customerRepository.deleteById(id);
        log.info("Cliente eliminado: {}", id);
    }

    /* -------------------- Puntos -------------------- */

    @Transactional
    public CustomerResponse applyEvent(Long customerId, String eventName, Double purchaseAmount, int numberOfPeople) {
        Customer customer = findCustomerById(customerId);
        return mapToResponse(customer);
    }



    private Customer findCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado"));
    }

    private CustomerResponse mapToResponse(Customer customer) {
        return CustomerResponse.builder()
                .id(customer.getId())
                .userId(customer.getUser().getId())
                .fullName(customer.getUser().getFullName())
                .email(customer.getUser().getEmail())
                .build();
    }

    private <T> PagedResponse<T> buildPagedResponse(Page<?> page, List<T> content) {
        return PagedResponse.<T>builder()
                .content(content)
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .build();
    }
}
