import service.CustomerService;

import model.Customer;
import java.util.*;


public class Main {
    public void main(String[] args) {
        CustomerService customerService = new CustomerService();
        customerService.createCustomer("Budi Santuy", "buuy@mail.com", "08123456789");
        customerService.createCustomer("Siti Aminah", "sitam@mail.com", "082222222222");

        System.out.println("All Customers:\n");
        List<Customer> customers = customerService.getAllCustomers();
        for (Customer customer : customers) {
            System.out.println(customer.getFullName() + " - " + customer.getEmail() + " - " + customer.getPhoneNumber());
        }
        System.out.println();

        System.out.println("Customer Detail:");
        System.out.println(customerService.getCustomerById(1L).getDisplayName());


    }
}