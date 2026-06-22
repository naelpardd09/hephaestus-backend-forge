package com.fif.finance_training.service;
import com.fif.finance_training.dto.CreateCustomerRequest;
import com.fif.finance_training.dto.CustomerResponse;
import com.fif.finance_training.dto.CustomerSummaryResponse;
import com.fif.finance_training.entity.CustomerEntity;
import com.fif.finance_training.exception.CustomerNotFoundException;
import com.fif.finance_training.exception.DuplicateCustomerException;
import com.fif.finance_training.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerService {
    
    private final CustomerRepository customerRepository;

    @Transactional
    public CustomerResponse createCustomer(CreateCustomerRequest request) {
        if (customerRepository.existsByNik(request.getNik())) {
            throw new DuplicateCustomerException("NIK already exists: " + request.getNik());
        }
        if (customerRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateCustomerException("Email already exists: " + request.getEmail());
        }

        CustomerEntity entity = CustomerEntity.builder()
                .fullName(request.getFullName())
                .nik(request.getNik())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .build();

        CustomerEntity saved = customerRepository.save(entity);
        return mapToResponse(saved);
    }

    @Transactional(readOnly = true)
    public CustomerResponse getCustomerById(Long id) {
        CustomerEntity entity = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + id));
        return mapToResponse(entity);
    }

    @Transactional(readOnly = true)
    public List<CustomerSummaryResponse> getAllCustomers() {
        return customerRepository.findAllActive().stream()
                .map(this::mapToSummaryResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CustomerSummaryResponse> searchCustomersByName(String name) {
        return customerRepository.findByFullNameContainingIgnoreCase(name).stream()
                .map(this::mapToSummaryResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void softDeleteCustomer(Long id) {
        CustomerEntity customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + id));
        customer.setDeletedAt(LocalDateTime.now());
        customerRepository.save(customer);
    }

    private CustomerResponse mapToResponse(CustomerEntity entity) {
        return CustomerResponse.builder()
                .id(entity.getId())
                .fullName(entity.getFullName())
                .nik(entity.getNik())
                .email(entity.getEmail())
                .phoneNumber(entity.getPhoneNumber())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
    // tes

    private CustomerSummaryResponse mapToSummaryResponse(CustomerEntity entity) {
        return CustomerSummaryResponse.builder()
                .id(entity.getId())
                .fullName(entity.getFullName())
                .email(entity.getEmail())
                .phoneNumber(entity.getPhoneNumber())
                .build();
    }
}