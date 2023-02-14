package com.jfarro.app.controllers;

import com.jfarro.app.models.entity.DocumentType;
import com.jfarro.app.services.ClientService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/document_type")
public class DocumentTypeRestController {

    private final ClientService clientService;

    public DocumentTypeRestController(ClientService clientService) {
        this.clientService = clientService;
    }

    /**
     * Obtiene la lista de los tipos de documento
     * @return Json con la estructura {@Link DocumentType}
     */
    @GetMapping
    public ResponseEntity<List<DocumentType>> list() {
        return ResponseEntity.ok(this.clientService.findAllDocumentTypes());
    }

    /**
     * Muestra la lista de los tipos de documento con paginacion
     * @param page numero de pagina
     * @param size cantidad de filas
     * @return Json con la estructura {@Link DocumentType}
     */
    @GetMapping("/pagination")
    public ResponseEntity<List<DocumentType>> listPage(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size
    ) {
        Pageable pageRequest = PageRequest.of(page, size);
        Page<DocumentType> documentTypes = this.clientService.findAllDocumentType(pageRequest);
        return ResponseEntity.ok(documentTypes.getContent());
    }

    /**
     * Obtiene el tipo de documento por medio de su ID
     * @param id identificador del tipo de documento
     * @return Json con la estructura o con los mensajes de validacion {@Link DocumentType}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable("id") Long id) {
        if (id <= 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "El ID del tipo de documento debe ser mayor que cero."));
        }

        Optional<DocumentType> documentTypeOptional = this.clientService.findByIdDocumentType(id);
        if (documentTypeOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "El cliente no se encuentra registrado en la base de datos."));
        }

        return ResponseEntity.ok(documentTypeOptional.get());
    }
}
