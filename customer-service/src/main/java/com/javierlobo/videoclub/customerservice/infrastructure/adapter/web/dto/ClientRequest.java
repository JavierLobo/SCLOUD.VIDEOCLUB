package com.javierlobo.videoclub.customerservice.infrastructure.adapter.web.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ClientRequest {

    @NotBlank @Size(max = 80)
    private String name;
    
    @NotBlank @Size(max = 120)
    private String lastName;

    @Pattern(regexp = "^\\d{8}[A-Za-z]$", message = "Formato de DNI inválido")
    private String dni;

    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Formato de email inválido")
    private String email;

    @Pattern(regexp = "^\\+?[0-9 .-]{6,20}$", message = "Formato de teléfono inválido")
    private String phone;

    private LocalDate registrationDate;
    
    private boolean active;

}
