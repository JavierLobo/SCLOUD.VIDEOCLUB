package com.javierlobo.videoclub.customerservice.infrastructure.adapter.persistence.mapper;

import org.mapstruct.Mapper;
import com.javierlobo.videoclub.customerservice.domain.model.Client;
import com.javierlobo.videoclub.customerservice.infrastructure.adapter.persistence.entity.ClientEntity;
import com.javierlobo.videoclub.customerservice.infrastructure.adapter.web.dto.ClientRequest;
import com.javierlobo.videoclub.customerservice.infrastructure.adapter.web.dto.ClientResponse;

@Mapper(componentModel = "spring")
public interface ClientMapper {

    Client toClient(ClientEntity clientEntity);
    ClientEntity toClientEntity(Client client);
    
    ClientResponse toResponse(Client client);
    Client fromRequest(ClientRequest request);

    Iterable<ClientResponse> toResponseList(Iterable<Client> allClients);
    Iterable<Client> fromResponse(Iterable<ClientResponse> clientResponses);
}
