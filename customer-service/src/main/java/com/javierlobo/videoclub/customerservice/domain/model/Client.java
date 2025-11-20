package com.javierlobo.videoclub.customerservice.domain.model;

import java.time.LocalDate;
import lombok.Data;

@Data
public class Client {
    private Long id;
    private String name;
    private String lastName;
    private String dni;
    private String email;
    private String phone;
    private LocalDate registrationDate;
    //private TipoMembresia membresia;
    private boolean active;
}