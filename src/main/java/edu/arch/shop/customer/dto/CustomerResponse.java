package edu.arch.shop.customer.dto;

import java.time.LocalDate;

public record CustomerResponse(
        Long id,
        String name,
        String email,
        String phone,
        LocalDate birthDate,
        String street,
        String city,
        String postalCode,
        boolean active
) {}