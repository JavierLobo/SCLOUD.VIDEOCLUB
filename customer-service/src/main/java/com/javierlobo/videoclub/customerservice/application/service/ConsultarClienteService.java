package com.javierlobo.videoclub.customerservice.application.service;

import org.springframework.stereotype.Service;

import com.javierlobo.videoclub.customerservice.domain.exception.ClienteNoEncontradoException;
import com.javierlobo.videoclub.customerservice.domain.model.Client;
import com.javierlobo.videoclub.customerservice.domain.port.ClientRepositoryPort;

@Service
public class ConsultarClienteService {

    private final ClientRepositoryPort clientRepositoryPort;

    public ConsultarClienteService(ClientRepositoryPort clientRepositoryPort) {
        this.clientRepositoryPort = clientRepositoryPort;
    }

    public Client consultar(Long clientId) {
        return clientRepositoryPort
            .buscarPorId(clientId)
            .orElseThrow(() -> new ClienteNoEncontradoException(clientId));
    }

    public Iterable<Client> getAllClients() {
        return clientRepositoryPort.buscarTodos();
    }

}
