package com.javierlobo.videoclub.customerservice.application.service;

import org.springframework.stereotype.Service;

import com.javierlobo.videoclub.customerservice.domain.exception.ClienteNoEncontradoException;
import com.javierlobo.videoclub.customerservice.domain.port.ClientRepositoryPort;

@Service
public class DeleteClientService {

    private final ClientRepositoryPort clientRepositoryPort;

    public DeleteClientService(ClientRepositoryPort clientRepositoryPort) {
        this.clientRepositoryPort = clientRepositoryPort;
    }

    public void eliminar(Long clientId) {
        if (clientId == null || clientId <= 0) {
            throw new IllegalArgumentException("El ID del cliente no es valido.");
        }

        boolean exists = clientRepositoryPort.existsById(clientId);
        if (!exists) {
            throw new ClienteNoEncontradoException(clientId);
        }

        try {
            clientRepositoryPort.deleteById(clientId);
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar el cliente con ID: " + clientId, e);
        }
    }
}
