package com.fif.finance_training.service;

import com.fif.finance_training.dto.CreateCustomerRequest;
import com.fif.finance_training.dto.CustomerResponse;
import com.fif.finance_training.dto.CustomerSummaryResponse;
import com.fif.finance_training.entity.CustomerEntity;
import com.fif.finance_training.exception.CustomerNotFoundException;
import com.fif.finance_training.exception.DuplicateCustomerException;
import com.fif.finance_training.web.StructuredLogger;
import com.fif.finance_training.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerService {
    
    private final CustomerRepository customerRepository;
    private final StructuredLogger logger;

    @Transactional
    public CustomerResponse createCustomer(CreateCustomerRequest request) {
        logger.info("CUSTOMER_CREATE_ATTEMPT", "Creating new customer",
                "email", request.getEmail(),
                "nik", request.getNik(),
                "fullName", request.getFullName());

        if (customerRepository.existsByNik(request.getNik())) {
            logger.warn("CUSTOMER_CREATE_FAILED", "NIK already exists",
                    "nik", request.getNik());
            throw new DuplicateCustomerException("NIK already exists: " + request.getNik());
        }
        if (customerRepository.existsByEmail(request.getEmail())) {
            logger.warn("CUSTOMER_CREATE_FAILED", "Email already exists",
                    "email", request.getEmail());
            throw new DuplicateCustomerException("Email already exists: " + request.getEmail());
        }

        CustomerEntity entity = CustomerEntity.builder()
                .fullName(request.getFullName())
                .nik(request.getNik())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .build();

        CustomerEntity saved = customerRepository.save(entity);
        
        logger.info("CUSTOMER_CREATED", "Customer created successfully",
                "customerId", saved.getId().toString(),
                "email", saved.getEmail(),
                "nik", saved.getNik());

        return mapToResponse(saved);
    }

    @Transactional(readOnly = true)
    public CustomerResponse getCustomerById(Long id) {
        logger.info("CUSTOMER_GET", "Fetching customer",
                "customerId", id.toString());

        CustomerEntity entity = customerRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("CUSTOMER_NOT_FOUND", "Customer not found",
                            "customerId", id.toString());
                    return new CustomerNotFoundException("Customer not found with id: " + id);
                });

        return mapToResponse(entity);
    }

    @Transactional(readOnly = true)
    public List<CustomerSummaryResponse> getAllCustomers() {
        logger.info("CUSTOMER_LIST", "Fetching all active customers");
        
        return customerRepository.findAllActive().stream()
                .map(this::mapToSummaryResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CustomerSummaryResponse> searchCustomersByName(String name) {
        logger.info("CUSTOMER_SEARCH", "Searching customers by name",
                "searchTerm", name);

        return customerRepository.findByFullNameContainingIgnoreCase(name).stream()
                .map(this::mapToSummaryResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void softDeleteCustomer(Long id) {
        logger.info("CUSTOMER_DELETE_ATTEMPT", "Soft deleting customer",
                "customerId", id.toString());

        CustomerEntity customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + id));
        
        customer.setDeletedAt(ZonedDateTime.now());
        customerRepository.save(customer);
        
        logger.info("CUSTOMER_DELETED", "Customer soft deleted",
                "customerId", id.toString(),
                "deletedAt", ZonedDateTime.now().toString());
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

    private CustomerSummaryResponse mapToSummaryResponse(CustomerEntity entity) {
        return CustomerSummaryResponse.builder()
                .id(entity.getId())
                .fullName(entity.getFullName())
                .email(entity.getEmail())
                .phoneNumber(entity.getPhoneNumber())
                .build();
    }
}