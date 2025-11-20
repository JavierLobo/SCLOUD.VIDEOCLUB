package com.javierlobo.videoclub.customerservice.application.service;

import org.springframework.stereotype.Service;

import com.javierlobo.videoclub.customerservice.domain.model.Client;
import com.javierlobo.videoclub.customerservice.domain.port.ClientRepositoryPort;

@Service
public class RegistrarClientService {

    private final ClientRepositoryPort clientRepository;

    public RegistrarClientService(ClientRepositoryPort clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Client registrar(Client nuevoCliente) throws Exception {
        // aquí validaciones de negocio, etc.
        return clientRepository.guardar(nuevoCliente);
    }
}
