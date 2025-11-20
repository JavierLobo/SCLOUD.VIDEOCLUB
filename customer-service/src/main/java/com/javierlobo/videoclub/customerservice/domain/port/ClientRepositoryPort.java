package com.javierlobo.videoclub.customerservice.domain.port;

import java.util.List;
import java.util.Optional;

import com.javierlobo.videoclub.customerservice.domain.model.Client;

public interface ClientRepositoryPort {

    Optional<Client> buscarPorId(Long id);

    boolean existsById(Long clientId);

    Optional<Client> buscarPorEmail(String email);

    List<Client> listarActivos();

    Iterable<Client> buscarTodos();

    Client guardar(Client cliente) throws Exception;
    
    void deleteById(Long clientId) throws Exception;

}
