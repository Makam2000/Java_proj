package edu.arch.shop.customer.service;

import edu.arch.shop.customer.dto.CustomerRequest;
import edu.arch.shop.customer.dto.CustomerResponse;
import edu.arch.shop.customer.entity.Customer;
import edu.arch.shop.customer.repository.CustomerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class CustomerService {

    private static final String EMAIL_REGEX = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$";

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<CustomerResponse> findAll() {
        return customerRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public CustomerResponse findById(Long id) {
        return toResponse(findCustomer(id));
    }

    @Transactional
    public CustomerResponse create(CustomerRequest request) {
        validate(request);
        Customer customer = new Customer();
        apply(customer, request);
        return toResponse(customerRepository.save(customer));
    }

    @Transactional
    public CustomerResponse update(Long id, CustomerRequest request) {
        validate(request);
        Customer customer = findCustomer(id);
        apply(customer, request);
        return toResponse(customerRepository.save(customer));
    }

    @Transactional
    public void delete(Long id) {
        Customer customer = findCustomer(id);
        customerRepository.delete(customer);
    }

    private Customer findCustomer(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    private void validate(CustomerRequest request) {
        if (request.name() == null || request.name().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name is required");
        }
        if (request.name().length() > 100) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name is too long");
        }
        if (request.email() == null || !request.email().matches(EMAIL_REGEX)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid email");
        }
        if (request.birthDate() != null && request.birthDate().isAfter(LocalDate.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Birth date cannot be in the future");
        }
    }

    private void apply(Customer customer, CustomerRequest request) {
        customer.setName(request.name());
        customer.setEmail(request.email());
        customer.setPhone(request.phone());
        customer.setBirthDate(request.birthDate());
        customer.setStreet(request.street());
        customer.setCity(request.city());
        customer.setPostalCode(request.postalCode());
    }

    private CustomerResponse toResponse(Customer customer) {
        return new CustomerResponse(
                customer.getId(),
                customer.getName(),
                customer.getEmail(),
                customer.getPhone(),
                customer.getBirthDate(),
                customer.getStreet(),
                customer.getCity(),
                customer.getPostalCode(),
                customer.isActive());
    }
}