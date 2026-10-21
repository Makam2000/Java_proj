package edu.arch.shop.customer.dto;

import java.time.LocalDate;

public record CustomerRequest(
        String name,
        String email,
        String phone,
        LocalDate birthDate,
        String street,
        String city,
        String postalCode
) {}