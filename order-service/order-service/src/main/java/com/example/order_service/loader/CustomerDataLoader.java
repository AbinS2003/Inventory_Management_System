package com.example.order_service.loader;

import com.example.order_service.dto.CustomerRequestDTO;
import com.example.order_service.repository.CustomerRepository;
import com.example.order_service.service.CustomerService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CustomerDataLoader implements CommandLineRunner {

    private final CustomerService customerService;
    private final CustomerRepository customerRepository;

    public CustomerDataLoader(CustomerService customerService, CustomerRepository customerRepository) {
        this.customerService = customerService;
        this.customerRepository = customerRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        if (customerRepository.count() == 0) {


            List<CustomerRequestDTO> sampleCustomers = List.of(
                    new CustomerRequestDTO("Alice Johnson", "alice.johnson@example.com", "Password123"),
                    new CustomerRequestDTO("Bob Smith", "bob.smith@example.com", "SecurePass1"),
                    new CustomerRequestDTO("Charlie Brown", "charlie.brown@example.com", "Charlie2025"),
                    new CustomerRequestDTO("Diana Prince", "diana.prince@example.com", "Wonder123"),
                    new CustomerRequestDTO("Ethan Hunt", "ethan.hunt@example.com", "Mission007"),
                    new CustomerRequestDTO("Fiona Gallagher", "fiona.g@example.com", "FionaPass9"),
                    new CustomerRequestDTO("George Martin", "george.martin@example.com", "GMartin123"),
                    new CustomerRequestDTO("Hannah Lee", "hannah.lee@example.com", "HannahPwd1"),
                    new CustomerRequestDTO("Ian Curtis", "ian.curtis@example.com", "IanPassword8"),
                    new CustomerRequestDTO("Julia Roberts", "julia.roberts@example.com", "JuliaPass77")
            );

            sampleCustomers.forEach(customerService::createCustomer);

            System.out.println("sample customers added!");
        }


    }
}
