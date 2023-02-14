package com.jfarro.app.controllers;

import com.jfarro.app.models.entity.Mark;
import com.jfarro.app.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/marks")
public class MarkRestController {

    private final ProductService productService;

    public MarkRestController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Obtiene toda la lista de marcas
     * @return Json con la estructura de User {@Link Mark}
     */
    @GetMapping
    public ResponseEntity<List<Mark>> listar() {
        return ResponseEntity.ok(this.productService.findAllMarks());
    }

    /**
     * Obtiene una lista de marcas con paginacion
     * @param page numero de pagina
     * @param size cantidad de filas
     * @return Json con la estructura {@Link Mark}
     */
    @GetMapping("/pagination")
    public ResponseEntity<List<Mark>> listPages(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size
    ) {
        Pageable pageRequest = PageRequest.of(page, size);
        Page<Mark> users = this.productService.findAllPagesMarks(pageRequest);
        return ResponseEntity.ok(users.getContent());
    }

    /**
     * Obtiene la marca por medio del ID
     * @param id identificador de la marca
     * @return Json con la estructura o con los mensajes de validacion {@Link Mark}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable("id") Long id) {
        Map<String, Object> data = new HashMap<>();

        if (id <= 0) {
            data.put("error", "El ID de la marca debe ser mayor que cero.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(data);
        }

        Optional<Mark> markOptional = this.productService.findByIdMark(id);
        if (markOptional.isEmpty()) {
            data.put("error", "La marca no se encuentra registrado en la base de datos.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(data);
        }

        return ResponseEntity.ok(markOptional.get());
    }

    /**
     * Registra la marca en la base de datos y retorna la marca registrada
     *
     * @param mark marca para el registro de datos
     * @param bindingResult valida los datos que pasan
     * @return Json con la estructura o con los mensajes de validacion {@Link Mark}
     */
    @PostMapping
    public ResponseEntity<Object> persistAndUpdate(@RequestBody @Valid Mark mark, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            Map<Object, Object> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(err -> errors.put(err.getField(), err.getDefaultMessage()));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(errors);
        }

        Mark markResponse = null;
        try {
            markResponse = this.productService.saveMark(mark);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ex.getMessage());
        }

        return ResponseEntity.ok(markResponse);
    }

    /**
     * Actualiza el estado de la marca
     * @param id identificador de la marca
     * @return Json con el mensaje de confirmacion
     */
    @GetMapping("/state/{id}")
    public ResponseEntity<Object> updateState(@PathVariable("id") Long id) {
        Map<String, Object> data = new HashMap<>();

        if (id <= 0) {
            data.put("error", "El ID de la marca debe ser mayor que cero.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(data);
        }

        try {
            byte state = 0;
            this.productService.updateStateMark(state, id);
            data.put("mensaje", "Estado de la marca modificado.");
        } catch (Exception ex) {
            data.put("error", ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(data);
        }

        return ResponseEntity.ok(data);
    }
}
