package com.javierlobo.videoclub.customerservice.infrastructure.adapter.persistence.adapter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.javierlobo.videoclub.customerservice.domain.model.Client;
import com.javierlobo.videoclub.customerservice.domain.port.ClientRepositoryPort;
import com.javierlobo.videoclub.customerservice.infrastructure.adapter.persistence.mapper.ClientMapper;
import com.javierlobo.videoclub.customerservice.infrastructure.adapter.persistence.repository.JpaClientRepository;

@Repository
public class ClientRepositoryAdapter implements ClientRepositoryPort {

    @Autowired
    private ClientMapper clientMapper;

    private final JpaClientRepository jpaClientRepository;

    public ClientRepositoryAdapter(JpaClientRepository jpaClientRepository, ClientMapper clientMapper) {
        this.jpaClientRepository = jpaClientRepository;
        this.clientMapper = clientMapper;
    }

    @Override
    public Client guardar(Client Client) throws Exception {
        return clientMapper
            .toClient(jpaClientRepository
                .saveAndFlush(clientMapper.toClientEntity(Client)));
    }

    @Override
    public Optional<Client> buscarPorId(Long id) {
        return jpaClientRepository
            .findById(id)
            .map(clientMapper::toClient);
    }

    @Override
    public boolean existsById(Long clientId) {
        return jpaClientRepository.existsById(clientId);
    }

    @Override
    public Optional<Client> buscarPorEmail(String email) {
        return jpaClientRepository
            .findByEmail(email)
            .map(clientMapper::toClient);
    }

    @Override
    public List<Client> listarActivos() {
        return jpaClientRepository
            .findByActiveTrue().stream()
                .map(clientMapper::toClient)
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Client> buscarTodos() {
        return jpaClientRepository
            .findAll().stream()
                .map(clientMapper::toClient)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long clientId) throws Exception {
        if (!jpaClientRepository.existsById(clientId)) {
            throw new Exception("Cliente no encontrado");
        }
        jpaClientRepository.deleteById(clientId);
    }
}
