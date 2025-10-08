package com.jeyix.school_jeyix.features.customers.controller;

import com.jeyix.school_jeyix.core.dto.response.PagedResponse;
import com.jeyix.school_jeyix.core.security.dto.ApiResponse;
import com.jeyix.school_jeyix.features.customers.dto.customer.request.CustomerRequest;
import com.jeyix.school_jeyix.features.customers.dto.customer.response.CustomerResponse;
import com.jeyix.school_jeyix.features.customers.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping
    public ResponseEntity<ApiResponse<PagedResponse<CustomerResponse>>> getAllCustomers(Pageable pageable) {
        PagedResponse<CustomerResponse> response = customerService.getAllCustomers(pageable);
        return ResponseEntity.ok(new ApiResponse<>(true, "Clientes obtenidos correctamente", response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CustomerResponse>> getCustomer(@PathVariable Long id) {
        CustomerResponse customer = customerService.getCustomer(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Cliente obtenido correctamente", customer));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CustomerResponse>> createCustomer(@Valid @RequestBody CustomerRequest dto) {
        CustomerResponse customer = customerService.createCustomer(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Cliente creado correctamente", customer));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CustomerResponse>> updateCustomer(
            @PathVariable Long id, @Valid @RequestBody CustomerRequest dto) {
        CustomerResponse customer = customerService.updateCustomer(id, dto);
        return ResponseEntity.ok(new ApiResponse<>(true, "Cliente actualizado correctamente", customer));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Cliente eliminado correctamente", null));
    }

}
