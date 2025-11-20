package com.javierlobo.videoclub.customerservice.domain.exception;

public class ClientesNoEncontradosException extends RuntimeException {

    public ClientesNoEncontradosException() {
        super("No se encontraron clientes.");
    }

}
