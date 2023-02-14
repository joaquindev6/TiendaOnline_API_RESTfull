package com.jfarro.app.controllers;

import com.jfarro.app.models.entity.Client;
import com.jfarro.app.services.ClientService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/clients")
public class ClientRestController {

    private final ClientService clientService;

    public ClientRestController(ClientService clientService) {
        this.clientService = clientService;
    }

    /**
     * Obtiene toda la lista de clientes
     * @return Json con la estructura {@Link Client}
     */
    @GetMapping
    public ResponseEntity<List<Client>> list() {
        return ResponseEntity.ok(this.clientService.findAllClient());
    }

    /**
     * Obtiene la lista de clientes con paginacion
     * @param page numero de pagina
     * @param size cantidad de filas
     * @return Json con la estructura {@link Client}
     */
    @GetMapping("/pagination")
    public ResponseEntity<List<Client>> listPage(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size
    ) {
        Pageable pageRequest = PageRequest.of(page, size);
        Page<Client> pages = this.clientService.findAllPagesClient(pageRequest);
        return ResponseEntity.ok(pages.getContent());
    }

    /**
     * Obtiene el cliente por medio de su ID
     * @param id identificado del cliente
     * @return Json con la estructura o con los mensajes de validacion {@Link Client}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable("id") Long id) {
        if (id <= 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "El ID del cliente debe ser mayor que cero."));
        }

        Optional<Client> clientOptional = this.clientService.findByIdClient(id);
        if (clientOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "El cliente no se encuentra registrado en la base de datos."));
        }

        return ResponseEntity.ok(clientOptional.get());
    }

    /**
     * Registra el cliente en la base de datos
     * @param client objeto cliente para el registro de datos
     * @param bindingResult valida los datos antes del registro
     * @return Json con la estructura o con los mensajes de validacion {@Link Client}
     */
    @PostMapping
    public ResponseEntity<Object> persistAndUpdate(@RequestBody @Valid Client client, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<Object, Object> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(err -> errors.put(err.getField(), err.getDefaultMessage()));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }

        Client clientResponse = null;
        try {
            clientResponse = this.clientService.saveClient(client);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }

        return ResponseEntity.ok(clientResponse);
    }

    /**
     * Actualiza el estado del cliente
     * @param id identificador del cliente
     * @return Json con el mensaje de confirmacion
     */
    @GetMapping("/state/{id}")
    public ResponseEntity<Object> updateState(@PathVariable("id") Long id) {
        Map<String, Object> data = new HashMap<>();

        if (id <= 0) {
            data.put("error", "El ID del cliente debe ser mayor que cero.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(data);
        }

        try {
            byte state = 0;
            this.clientService.updateStateClient(state, id);
            data.put("mensaje", "Estado del usuario modificado.");
        } catch (Exception ex) {
            data.put("error", ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(data);
        }

        return ResponseEntity.ok(data);
    }
}
