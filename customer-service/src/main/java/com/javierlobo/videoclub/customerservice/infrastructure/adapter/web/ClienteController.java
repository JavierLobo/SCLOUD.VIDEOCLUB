package com.javierlobo.videoclub.customerservice.infrastructure.adapter.web;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.javierlobo.videoclub.customerservice.application.service.ConsultarClienteService;
import com.javierlobo.videoclub.customerservice.application.service.DeleteClientService;
import com.javierlobo.videoclub.customerservice.application.service.RegistrarClientService;
import com.javierlobo.videoclub.customerservice.infrastructure.adapter.persistence.mapper.ClientMapper;
import com.javierlobo.videoclub.customerservice.infrastructure.adapter.web.dto.ClientRequest;
import com.javierlobo.videoclub.customerservice.infrastructure.adapter.web.dto.ClientResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

@Validated
@RestController
@RequestMapping("/api/v1")
@Tag(name = "Clientes", description = "Operaciones sobre clientes")
public class ClienteController {

    private final RegistrarClientService registrarClientService;
    private final ConsultarClienteService consultarClienteService;
    private final DeleteClientService deleteClientService;
    private ClientMapper clientMapper;
    
    public ClienteController(RegistrarClientService registrarClientService,
                             ConsultarClienteService consultarClienteService,
                             DeleteClientService deleteClientService,
                             ClientMapper clientMapper) {
        this.registrarClientService = registrarClientService;
        this.consultarClienteService = consultarClienteService;
        this.deleteClientService = deleteClientService;
        this.clientMapper = clientMapper;
    }
    
    // --------------------------------------------------------------------------------
    // Métodos GET
    // --------------------------------------------------------------------------------
    /**
     * Consulta un cliente por su identificador.
     *
     * @param clientId identificador del cliente
     * @return datos del cliente
     */
    @Operation(
        summary = "Consultar cliente",
        description = "Obtiene los datos de un cliente a partir de su identificador."
    )
    @ApiResponse(
        responseCode = "200",
        description = "Cliente obtenido correctamente",
        content = @Content(schema = @Schema(implementation = ClientResponse.class))
    )
    @GetMapping("/cliente/{clientId}")
    public ResponseEntity<ClientResponse> consultar(@PathVariable @Positive Long clientId) {
        return ResponseEntity
            .ok(clientMapper.toResponse(consultarClienteService.consultar(clientId)));
    }
    
    /**
     * Consulta todos los clientes del sistema.
     *
     * @return lista de clientes
     */
    @Operation(
        summary = "Consultar todos los clientes",
        description = "Obtiene la lista de todos los clientes registrados en el sistema."
    )
    @ApiResponse(
        responseCode = "200",
        description = "Clientes obtenidos correctamente",
        content = @Content(schema = @Schema(implementation = ClientResponse.class))
    )
    @GetMapping("/cliente/getAll")
    public ResponseEntity<Iterable<ClientResponse>> getAllClients() {
        return ResponseEntity
            .ok(clientMapper.toResponseList(consultarClienteService.getAllClients()));
    }
    
    // --------------------------------------------------------------------------------
    // Métodos POST
    // --------------------------------------------------------------------------------
    /**
     * Crea un nuevo cliente en el sistema.
     *
     * @param request datos del nuevo cliente
     * @return cliente creado con su identificador
     * @throws Exception si ocurre un error de negocio
     */
    @Operation(
        summary = "Registrar cliente",
        description = "Crea un nuevo cliente a partir de los datos enviados en el cuerpo de la petición."
    )
    @ApiResponse(
        responseCode = "200",
        description = "Cliente registrado correctamente",
        content = @Content(schema = @Schema(implementation = ClientResponse.class))
    )
    @PostMapping("/cliente")
    public ResponseEntity<ClientResponse> registrar(@RequestBody @Valid ClientRequest request) throws Exception {
        return ResponseEntity.ok(clientMapper
            .toResponse(registrarClientService.registrar(clientMapper.fromRequest(request))));
        
    }
        
    // --------------------------------------------------------------------------------
    // Métodos DELETE
    // --------------------------------------------------------------------------------
    /**
     * Elimina un cliente del sistema.
     *
     * @param clientId identificador del cliente a eliminar
     * @return respuesta sin contenido
     */
    @Operation(
        summary = "Eliminar cliente",
        description = "Elimina un cliente a partir de su identificador."
    )
    @ApiResponse(
        responseCode = "204",
        description = "Cliente eliminado correctamente"
    )
    @DeleteMapping("/cliente/{clientId}")
    public ResponseEntity<Void> eliminar(@PathVariable @Positive Long clientId) {
        deleteClientService.eliminar(clientId);
        return ResponseEntity.noContent().build();
    }
}
