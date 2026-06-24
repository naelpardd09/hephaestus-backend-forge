package com.fif.finance_training;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fif.finance_training.controller.CustomerController;
import com.fif.finance_training.dto.*;
import com.fif.finance_training.exception.CustomerNotFoundException;
import com.fif.finance_training.exception.DuplicateCustomerException;
import com.fif.finance_training.service.CustomerService;
import com.fif.finance_training.web.StructuredLogger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CustomerControllerMockTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private CustomerService customerService;

    @Mock
    private StructuredLogger logger;

    @InjectMocks
    private CustomerController customerController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(customerController)
                .setControllerAdvice(new com.fif.finance_training.exception.GlobalExceptionHandler(logger))
                .build();
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
    }

    private CustomerResponse buildCustomerResponse() {
        return CustomerResponse.builder()
                .id(1L)
                .fullName("Budi Santoso")
                .nik("3173010101900001")
                .email("budi@mail.com")
                .phoneNumber("08123456789")
                .createdAt(ZonedDateTime.now())
                .updatedAt(ZonedDateTime.now())
                .build();
    }

    private CreateCustomerRequest buildValidRequest() {
        CreateCustomerRequest req = new CreateCustomerRequest();
        req.setFullName("Budi Santoso");
        req.setNik("3173010101900001");
        req.setEmail("budi@mail.com");
        req.setPhoneNumber("08123456789");
        return req;
    }

    private CustomerSummaryResponse buildSummary(Long id, String name, String email, String phone) {
        return CustomerSummaryResponse.builder()
                .id(id)
                .fullName(name)
                .email(email)
                .phoneNumber(phone)
                .build();
    }

    // ========== GET ALL ==========

    @Test
    void getAllCustomers_shouldReturn200WithCustomerList() throws Exception {
        CustomerSummaryResponse c1 = buildSummary(1L, "Budi Santoso", "budi@mail.com", "08123456789");
        CustomerSummaryResponse c2 = buildSummary(2L, "Ani Rahayu", "ani@mail.com", "08198765432");

        when(customerService.getAllCustomers()).thenReturn(Arrays.asList(c1, c2));

        mockMvc.perform(get("/api/v1/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Customers retrieved successfully"))
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].full_name").value("Budi Santoso"))
                .andExpect(jsonPath("$.data[0].email").value("budi@mail.com"))
                .andExpect(jsonPath("$.data[0].phone_number").value("08123456789"))
                .andExpect(jsonPath("$.data[1].id").value(2))
                .andExpect(jsonPath("$.data[1].full_name").value("Ani Rahayu"));

        verify(customerService).getAllCustomers();
    }

    @Test
    void getAllCustomers_shouldReturn200_whenEmptyList() throws Exception {
        when(customerService.getAllCustomers()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.length()").value(0));

        verify(customerService).getAllCustomers();
    }

    @Test
    void getAllCustomers_withNameParam_shouldTriggerSearch() throws Exception {
        CustomerSummaryResponse c1 = buildSummary(1L, "Budi Santoso", "budi@mail.com", "08123456789");

        when(customerService.searchCustomersByName("Budi")).thenReturn(Arrays.asList(c1));

        mockMvc.perform(get("/api/v1/customers").param("name", "Budi"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.length()").value(1))
                .andExpect(jsonPath("$.data[0].full_name").value("Budi Santoso"));

        verify(customerService).searchCustomersByName("Budi");
        verify(customerService, never()).getAllCustomers();
    }

    // ========== GET BY ID ==========

    @Test
    void getCustomerById_shouldReturn200WithCustomer() throws Exception {
        CustomerResponse response = buildCustomerResponse();

        when(customerService.getCustomerById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/v1/customers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Customer retrieved successfully"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.full_name").value("Budi Santoso"))
                .andExpect(jsonPath("$.data.nik").value("3173010101900001"))
                .andExpect(jsonPath("$.data.email").value("budi@mail.com"))
                .andExpect(jsonPath("$.data.phone_number").value("08123456789"));

        verify(customerService).getCustomerById(1L);
    }

    @Test
    void getCustomerById_shouldReturn404_whenNotFound() throws Exception {
        when(customerService.getCustomerById(999L))
                .thenThrow(new CustomerNotFoundException("Customer not found with id: 999"));

        mockMvc.perform(get("/api/v1/customers/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value("CUSTOMER_NOT_FOUND"));

        verify(customerService).getCustomerById(999L);
    }

    // ========== SEARCH ==========

    @Test
    void searchCustomers_shouldReturn200WithFilteredCustomers() throws Exception {
        CustomerSummaryResponse c1 = buildSummary(1L, "Budi Santoso", "budi@mail.com", "08123456789");

        when(customerService.searchCustomersByName("Budi")).thenReturn(Arrays.asList(c1));

        mockMvc.perform(get("/api/v1/customers/search").param("name", "Budi"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Customers search completed successfully"))
                .andExpect(jsonPath("$.data.length()").value(1))
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].full_name").value("Budi Santoso"))
                .andExpect(jsonPath("$.data[0].email").value("budi@mail.com"))
                .andExpect(jsonPath("$.data[0].phone_number").value("08123456789"));

        verify(customerService).searchCustomersByName("Budi");
    }

    @Test
    void searchCustomers_shouldReturn200_whenEmptyResult() throws Exception {
        when(customerService.searchCustomersByName("XYZ")).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/customers/search").param("name", "XYZ"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.length()").value(0));

        verify(customerService).searchCustomersByName("XYZ");
    }

    // ========== CREATE ==========

    @Test
    void createCustomer_shouldReturn201WithCreatedCustomer() throws Exception {
        CreateCustomerRequest request = buildValidRequest();
        CustomerResponse response = buildCustomerResponse();

        when(customerService.createCustomer(any(CreateCustomerRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Customer created successfully"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.full_name").value("Budi Santoso"))
                .andExpect(jsonPath("$.data.nik").value("3173010101900001"))
                .andExpect(jsonPath("$.data.email").value("budi@mail.com"))
                .andExpect(jsonPath("$.data.phone_number").value("08123456789"));

        verify(customerService).createCustomer(any(CreateCustomerRequest.class));
    }

    @Test
    void createCustomer_shouldReturn400_whenValidationFails() throws Exception {
        CreateCustomerRequest request = buildValidRequest();
        request.setFullName("");

        mockMvc.perform(post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));

        verify(customerService, never()).createCustomer(any());
    }

    @Test
    void createCustomer_shouldReturn400_whenDuplicate() throws Exception {
        CreateCustomerRequest request = buildValidRequest();

        when(customerService.createCustomer(any(CreateCustomerRequest.class)))
                .thenThrow(new DuplicateCustomerException("NIK already exists: 3173010101900001"));

        mockMvc.perform(post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value("DUPLICATE_CUSTOMER"));

        verify(customerService).createCustomer(any(CreateCustomerRequest.class));
    }

    // ========== DELETE ==========

    @Test
    void deleteCustomer_shouldReturn200_whenSuccess() throws Exception {
        doNothing().when(customerService).softDeleteCustomer(1L);

        mockMvc.perform(delete("/api/v1/customers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Customer deleted successfully"))
                .andExpect(jsonPath("$.data").doesNotExist());

        verify(customerService).softDeleteCustomer(1L);
    }

    @Test
    void deleteCustomer_shouldReturn404_whenNotFound() throws Exception {
        doThrow(new CustomerNotFoundException("Customer not found with id: 99"))
                .when(customerService).softDeleteCustomer(99L);

        mockMvc.perform(delete("/api/v1/customers/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value("CUSTOMER_NOT_FOUND"));

        verify(customerService).softDeleteCustomer(99L);
    }
}