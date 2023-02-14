package com.jfarro.app.controllers;

import com.jfarro.app.models.entity.Category;
import com.jfarro.app.services.ProductService;
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
@RequestMapping("/categories")
public class CategoryRestController {

    private final ProductService productService;

    public CategoryRestController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Obtiene toda la lista de categorias
     * @return Json con la estructura de User {@Link Category}
     */
    @GetMapping
    public ResponseEntity<List<Category>> listar() {
        return ResponseEntity.ok(this.productService.findAllCategories());
    }

    /**
     * Obtiene una lista de categorias con paginacion
     * @param page numero de pagina
     * @param size cantidad de filas
     * @return Json con la estructura {@Link Category}
     */
    @GetMapping("/pagination")
    public ResponseEntity<List<Category>> listPages(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size
    ) {
        Pageable pageRequest = PageRequest.of(page, size);
        Page<Category> categories = this.productService.findAllPagesCategory(pageRequest);
        return ResponseEntity.ok(categories.getContent());
    }

    /**
     * Obtiene la categoria por medio del ID
     * @param id identificador de la categoria
     * @return Json con la estructura o con los mensajes de validacion {@Link Category}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable("id") Long id) {
        Map<String, Object> data = new HashMap<>();

        if (id <= 0) {
            data.put("error", "El ID de la categoria debe ser mayor que cero.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(data);
        }

        Optional<Category> categoryOptional = this.productService.findByIdCategory(id);
        if (categoryOptional.isEmpty()) {
            data.put("error", "La categoria no se encuentra registrado en la base de datos.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(data);
        }

        return ResponseEntity.ok(categoryOptional.get());
    }

    /**
     * Registra la categoria en la base de datos y retorna la categoria registrada
     *
     * @param category categoria para el registro de datos
     * @param bindingResult valida los datos que pasan
     * @return Json con la estructura o con los mensajes de validacion {@Link Category}
     */
    @PostMapping
    public ResponseEntity<Object> persistAndUpdate(@RequestBody @Valid Category category, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            Map<Object, Object> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(err -> errors.put(err.getField(), err.getDefaultMessage()));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(errors);
        }

        Category categoryResponse = null;
        try {
            categoryResponse = this.productService.saveCategory(category);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ex.getMessage());
        }

        return ResponseEntity.ok(categoryResponse);
    }

    /**
     * Actualiza el estado de la categoria
     * @param id identificador de la categoria
     * @return Json con el mensaje de confirmacion
     */
    @GetMapping("/state/{id}")
    public ResponseEntity<Object> updateState(@PathVariable("id") Long id) {
        Map<String, Object> data = new HashMap<>();

        if (id <= 0) {
            data.put("error", "El ID de la categoria debe ser mayor que cero.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(data);
        }

        try {
            byte state = 0;
            this.productService.updateStateCategory(state, id);
            data.put("mensaje", "Estado de la categoria modificado.");
        } catch (Exception ex) {
            data.put("error", ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(data);
        }

        return ResponseEntity.ok(data);
    }
}
