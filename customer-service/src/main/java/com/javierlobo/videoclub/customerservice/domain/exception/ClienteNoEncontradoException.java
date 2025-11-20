package com.javierlobo.videoclub.customerservice.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ClienteNoEncontradoException extends RuntimeException {

    public ClienteNoEncontradoException(Long clientId) {
        super("Cliente no encontrado con ID: " + clientId);
    }
}
