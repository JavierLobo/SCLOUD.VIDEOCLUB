package com.javierlobo.videoclub.customerservice.infrastructure.adapter.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.javierlobo.videoclub.customerservice.infrastructure.adapter.persistence.entity.ClientEntity;

public interface JpaClientRepository extends JpaRepository<ClientEntity, Long> {

    Optional<ClientEntity> findByEmail(String email);

    List<ClientEntity> findByActiveTrue();

    void deleteById(Long clientId);
}

