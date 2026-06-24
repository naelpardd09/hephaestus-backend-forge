package com.fif.finance_training;

import com.fif.finance_training.dto.CreateCustomerRequest;
import com.fif.finance_training.dto.CustomerResponse;
import com.fif.finance_training.dto.CustomerSummaryResponse;
import com.fif.finance_training.entity.CustomerEntity;
import com.fif.finance_training.exception.CustomerNotFoundException;
import com.fif.finance_training.exception.DuplicateCustomerException;
import com.fif.finance_training.web.StructuredLogger;
import com.fif.finance_training.repository.CustomerRepository;
import com.fif.finance_training.service.CustomerService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private StructuredLogger logger;

    @InjectMocks
    private CustomerService customerService;

    // ========== CREATE CUSTOMER ==========

    @Test
    void createCustomer_success() {
        CreateCustomerRequest request = new CreateCustomerRequest();
        request.setFullName("Budi Santoso");
        request.setNik("3173010101900001");
        request.setEmail("budi@mail.com");
        request.setPhoneNumber("08123456789");

        when(customerRepository.existsByNik("3173010101900001")).thenReturn(false);
        when(customerRepository.existsByEmail("budi@mail.com")).thenReturn(false);

        CustomerEntity savedEntity = CustomerEntity.builder()
                .id(1L)
                .fullName("Budi Santoso")
                .nik("3173010101900001")
                .email("budi@mail.com")
                .phoneNumber("08123456789")
                .build();
        when(customerRepository.save(any(CustomerEntity.class))).thenReturn(savedEntity);

        CustomerResponse response = customerService.createCustomer(request);

        // Assert SEMUA field (coverage mapToResponse 100%)
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Budi Santoso", response.getFullName());
        assertEquals("3173010101900001", response.getNik());        // <-- INI
        assertEquals("budi@mail.com", response.getEmail());
        assertEquals("08123456789", response.getPhoneNumber());

        verify(customerRepository, times(1)).save(any(CustomerEntity.class));
        // verify(logger, times(1)).info(eq("CUSTOMER_CREATE_ATTEMPT"), anyString(), any());
        // verify(logger, times(1)).info(eq("CUSTOMER_CREATED"), anyString(), any());
    }

    @Test
    void createCustomer_duplicateEmail_throwsException() {
        CreateCustomerRequest request = new CreateCustomerRequest();
        request.setFullName("Budi Santoso");
        request.setNik("3173010101900001");
        request.setEmail("budi@mail.com");
        request.setPhoneNumber("08123456789");

        when(customerRepository.existsByNik("3173010101900001")).thenReturn(false);
        when(customerRepository.existsByEmail("budi@mail.com")).thenReturn(true);

        DuplicateCustomerException exception = assertThrows(
                DuplicateCustomerException.class,
                () -> customerService.createCustomer(request)
        );

        assertEquals("Email already exists: budi@mail.com", exception.getMessage());
        verify(customerRepository, never()).save(any());
        // verify(logger, times(1)).warn(eq("CUSTOMER_CREATE_FAILED"), anyString(), any());
    }

    @Test
    void createCustomer_duplicateNik_throwsException() {
        CreateCustomerRequest request = new CreateCustomerRequest();
        request.setNik("3173010101900001");
        request.setEmail("budi@mail.com");

        when(customerRepository.existsByNik("3173010101900001")).thenReturn(true);

        DuplicateCustomerException exception = assertThrows(
                DuplicateCustomerException.class,
                () -> customerService.createCustomer(request)
        );

        assertEquals("NIK already exists: 3173010101900001", exception.getMessage());
        verify(customerRepository, never()).save(any());}
    //     verify(logger, times(1)).warn(eq("CUSTOMER_CREATE_FAILED"), anyString(), any());
    // }

    // ========== GET CUSTOMER ==========

    @Test
    void getCustomerById_success() {
        CustomerEntity entity = CustomerEntity.builder()
                .id(1L)
                .fullName("Budi Santoso")
                .nik("3173010101900001")
                .email("budi@mail.com")
                .phoneNumber("08123456789")
                .build();

        when(customerRepository.findById(1L)).thenReturn(Optional.of(entity));

        CustomerResponse response = customerService.getCustomerById(1L);

        // Assert SEMUA field (coverage mapToResponse 100%)
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Budi Santoso", response.getFullName());
        assertEquals("3173010101900001", response.getNik());        // <-- INI
        assertEquals("budi@mail.com", response.getEmail());
        assertEquals("08123456789", response.getPhoneNumber());

        verify(customerRepository, times(1)).findById(1L);
        // verify(logger, times(1)).info(eq("CUSTOMER_GET"), anyString(), any());
    }

    @Test
    void getCustomerById_notFound_throwsException() {
        when(customerRepository.findById(999L)).thenReturn(Optional.empty());

        CustomerNotFoundException exception = assertThrows(
                CustomerNotFoundException.class,
                () -> customerService.getCustomerById(999L)
        );

        assertEquals("Customer not found with id: 999", exception.getMessage());}
    //     verify(logger, times(1)).warn(eq("CUSTOMER_NOT_FOUND"), anyString(), any());
    // }

    // ========== GET ALL CUSTOMERS ==========

    @Test
    void getAllCustomers_success() {
        CustomerEntity customer1 = CustomerEntity.builder()
                .id(1L)
                .fullName("Budi")
                .email("budi@mail.com")
                .phoneNumber("08123456789")
                .build();
        CustomerEntity customer2 = CustomerEntity.builder()
                .id(2L)
                .fullName("Ani")
                .email("ani@mail.com")
                .phoneNumber("08123456788")
                .build();

        when(customerRepository.findAllActive()).thenReturn(Arrays.asList(customer1, customer2));

        List<CustomerSummaryResponse> responses = customerService.getAllCustomers();

        // Assert SEMUA field (coverage mapToSummaryResponse 100%)
        assertEquals(2, responses.size());
        assertEquals("Budi", responses.get(0).getFullName());
        assertEquals("budi@mail.com", responses.get(0).getEmail());      // <-- INI
        assertEquals("08123456789", responses.get(0).getPhoneNumber());   // <-- INI
        assertEquals("Ani", responses.get(1).getFullName());
        assertEquals("ani@mail.com", responses.get(1).getEmail());
        assertEquals("08123456788", responses.get(1).getPhoneNumber());}

    //     verify(logger, times(1)).info(eq("CUSTOMER_LIST"), anyString(), any());
    // }

    // ========== SEARCH CUSTOMERS (BARU, BELUM ADA) ==========

    @Test
    void searchCustomersByName_success() {
        // === GIVEN ===
        String searchName = "Budi";
        
        CustomerEntity customer1 = CustomerEntity.builder()
                .id(1L)
                .fullName("Budi Santoso")
                .email("budi@mail.com")
                .phoneNumber("08123456789")
                .build();
        CustomerEntity customer2 = CustomerEntity.builder()
                .id(2L)
                .fullName("Budi Wijaya")
                .email("budi.w@mail.com")
                .phoneNumber("08123456788")
                .build();

        when(customerRepository.findByFullNameContainingIgnoreCase("Budi"))
                .thenReturn(Arrays.asList(customer1, customer2));

        // === WHEN ===
        List<CustomerSummaryResponse> responses = customerService.searchCustomersByName(searchName);

        // === THEN ===
        assertEquals(2, responses.size());
        assertEquals("Budi Santoso", responses.get(0).getFullName());
        assertEquals("budi@mail.com", responses.get(0).getEmail());
        assertEquals("08123456789", responses.get(0).getPhoneNumber());
        assertEquals("Budi Wijaya", responses.get(1).getFullName());

        verify(customerRepository, times(1)).findByFullNameContainingIgnoreCase("Budi");
        // verify(logger, times(1)).info(eq("CUSTOMER_SEARCH"), anyString(), any());
    }

    // ========== SOFT DELETE ==========

    @Test
    void softDeleteCustomer_success() {
        CustomerEntity entity = CustomerEntity.builder()
                .id(1L)
                .fullName("Budi")
                .build();

        when(customerRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(customerRepository.save(any(CustomerEntity.class))).thenReturn(entity);

        customerService.softDeleteCustomer(1L);

        assertNotNull(entity.getDeletedAt());
        verify(customerRepository, times(1)).save(entity);
        // verify(logger, times(1)).info(eq("CUSTOMER_DELETE_ATTEMPT"), anyString(), any());
        // verify(logger, times(1)).info(eq("CUSTOMER_DELETED"), anyString(), any());
    }
}