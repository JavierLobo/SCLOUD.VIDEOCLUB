package com.javierlobo.videoclub.customerservice.infrastructure.adapter.web.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class ClientResponse {

    private Long id;
    private String name;
    private String lastName;
    private String dni;
    private String email;
    private String phone;
    private LocalDate registrationDate;
    private boolean active;

}
