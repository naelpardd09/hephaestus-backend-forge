package com.example.training.service;

import com.example.training.dto.*;
import com.example.training.model.*;

import java.time.ZonedDateTime;
import java.util.*;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.stereotype.Service;

import com.example.training.exception.CustomerNotFoundException;

@Service
public class CustomerService {

    
    private Map<Long, Customer> customerStorage = new HashMap<>();

    private Long sequence = 1L;

    public PageResponse<CustomerResponse> getCustomers(String email, int page, int size) {
    List<CustomerResponse> all = new ArrayList<>();
    for (Customer customer : customerStorage.values()) {
        if (email != null && !customer.getEmail().equalsIgnoreCase(email)) {
            continue;
        }
        CustomerResponse cr = new CustomerResponse();
        cr.setId(customer.getId());
        cr.setFullName(customer.getFullName());
        cr.setEmail(customer.getEmail());
        cr.setPhoneNumber(customer.getPhoneNumber());
        cr.setCreatedAt(customer.getCreatedAt());
        cr.setUpdatedAt(customer.getUpdatedAt());
        all.add(cr);
    }

    // manual pagination
    int fromIndex = page * size;
    int toIndex = Math.min(fromIndex + size, all.size());
    List<CustomerResponse> paged = fromIndex >= all.size() ? new ArrayList<>() : all.subList(fromIndex, toIndex);

    return new PageResponse<>(paged, page, size, all.size());
    // note: total_pages dihitung dari all.size(), bukan paged
}

    // Method ini untuk membuat Customer baru berdasarkan data yang dikirim dari request.
    // Customer disimpan ke customerStorage, lalu dikembalikan sebagai CustomerResponse.
    public CustomerResponse createCustomer(CreateCustomerRequest entity) {
        ZonedDateTime now = ZonedDateTime.now();
        Customer newCustomer = new Customer(sequence, entity.getFullName(), entity.getEmail(), entity.getPhoneNumber(), now, now);
        
        customerStorage.put(sequence, newCustomer);
        sequence++;

        CustomerResponse response = new CustomerResponse();

        response.setId(sequence); //
        response.setFullName(newCustomer.getFullName());
        response.setEmail(newCustomer.getEmail());
        response.setPhoneNumber(newCustomer.getPhoneNumber());
        response.setCreatedAt(newCustomer.getCreatedAt());
        response.setUpdatedAt(newCustomer.getUpdatedAt());

        return response;
    }

    // Method ini untuk mengambil satu data Customer berdasarkan ID.
    // Kalau customer dengan ID tersebut tidak ditemukan, block if-nya kosong sehingga tidak ada penanganan error,
    // dan kode akan lanjut jalan dan menyebabkan NullPointerException saat mengakses customer yang null
    public CustomerResponse getCustomerById(@PathVariable Long id) throws CustomerNotFoundException {
        Customer customer = customerStorage.get(id);

        if(customer == null) {
            throw new CustomerNotFoundException("CUSTOMER_NOT_FOUND", "Customer not found with id: " +id, null);
        }

        CustomerResponse response = new CustomerResponse();
        response.setId(customer.getId());
        response.setFullName(customer.getFullName());
        response.setEmail(customer.getEmail());
        response.setPhoneNumber(customer.getPhoneNumber());
        response.setCreatedAt(customer.getCreatedAt());
        response.setUpdatedAt(customer.getUpdatedAt());
        return response;
    }

    
    public CustomerResponse deleteCustomer(long id) throws CustomerNotFoundException {
        Customer customer = customerStorage.get(id);
        if (customer == null) {
            throw new CustomerNotFoundException("CUSTOMER_NOT_FOUND", "Customer not found with id: " + id, null);
            // throw new CustomerNotFoundException(String.format("Customer not found with id: %s", id), null, null);
        }
        
        customerStorage.remove(id);
        CustomerResponse response = new CustomerResponse(customer.getId(), customer.getFullName(), customer.getEmail(), customer.getPhoneNumber(), customer.getCreatedAt(), customer.getUpdatedAt());
        return response;
    }
    public CustomerResponse updateCustomer(@PathVariable Long id, CreateCustomerRequest entity) throws CustomerNotFoundException {
        Customer customer = customerStorage.get(id);

        if(customer == null) {
            throw new CustomerNotFoundException("CUSTOMER_NOT_FOUND", "Customer not found with id: " + id, null);
        }
        customer.setFullName(entity.getFullName());
        customer.setEmail(entity.getEmail());
        customer.setPhoneNumber(entity.getPhoneNumber());
        customer.setCreatedAt(ZonedDateTime.now());
        customer.setUpdatedAt(ZonedDateTime.now());
        customerStorage.put(id, customer);

        CustomerResponse response = new CustomerResponse(customer.getId(), customer.getFullName(), customer.getEmail(), customer.getPhoneNumber(), customer.getCreatedAt(), customer.getUpdatedAt());

        return response;
        
    }

    public CustomerResponse patchCustomer(@PathVariable Long id, PatchCustomerRequest broski) {
        Customer customer = customerStorage.get(id);

        if(customer == null) {
            throw new CustomerNotFoundException("CUSTOMER_NOT_FOUND", "Customer dengan id " + id + "tidak ditemukan", null);

        }
        if(broski.getFullName() != null){
            customer.setFullName(broski.getFullName());
        }
        if(broski.getEmail() != null){
            customer.setEmail(broski.getEmail());
        }
        if(broski.getPhoneNumber() != null){
            customer.setPhoneNumber(broski.getPhoneNumber());
        }
        if(broski.getCreatedAt() != null){
            customer.setCreatedAt(broski.getCreatedAt());
        }
        if(broski.getUpdatedAt() != null){
            customer.setUpdatedAt(broski.getUpdatedAt());
        }

        customerStorage.put(id, customer);

        CustomerResponse response = new CustomerResponse(customer.getId(), customer.getFullName(), customer.getEmail(), customer.getPhoneNumber(), customer.getCreatedAt(), customer.getUpdatedAt());

        return response;
    }

    // Method ini untuk mengembalikan data Customer default (hardcoded),
    // biasanya dipakai untuk testing atau sebagai fallback data
    public CustomerResponse getDefaultCustomer() {
        return buatResponKustomer(1L, "Rico Simanjuntak", "rjuntak@gmail.com", "081111");
    }

    // Method private helper ini untuk membuat objek CustomerResponse dari parameter yang diberikan,
    // tujuannya supaya tidak perlu menulis ulang kode yang sama setiap kali mau bikin CustomerResponse
    private CustomerResponse buatResponKustomer(Long id, String fullName, String email, String phoneNumber) {
        CustomerResponse response = new CustomerResponse();
        response.setId(id);
        response.setFullName(fullName);
        response.setEmail(email);
        response.setPhoneNumber(phoneNumber);
        // response.setCreatedAt(createdAt);
        // response.setUpdatedAt(updatedAt);

        return response;
    }

}