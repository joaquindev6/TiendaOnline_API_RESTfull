package com.jfarro.app.controllers;

import com.jfarro.app.models.entity.SubCategory;
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
@RequestMapping("/sub_categories")
public class SubCategoryRestController {

    private final ProductService productService;

    public SubCategoryRestController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Obtiene toda la lista de sub categorias
     * @return Json con la estructura de User {@Link SubCategory}
     */
    @GetMapping
    public ResponseEntity<List<SubCategory>> listar() {
        return ResponseEntity.ok(this.productService.findAllSubCategories());
    }

    /**
     * Obtiene una lista de sub categorias con paginacion
     * @param page numero de pagina
     * @param size cantidad de filas
     * @return Json con la estructura {@Link SubCategory}
     */
    @GetMapping("/pagination")
    public ResponseEntity<List<SubCategory>> listPages(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size
    ) {
        Pageable pageRequest = PageRequest.of(page, size);
        Page<SubCategory> subCategories = this.productService.findAllPagesSubCategory(pageRequest);
        return ResponseEntity.ok(subCategories.getContent());
    }

    /**
     * Obtiene la sub categoria por medio del ID
     * @param id identificador de la sub categoria
     * @return Json con la estructura o con los mensajes de validacion {@Link SubCategory}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable("id") Long id) {
        Map<String, Object> data = new HashMap<>();

        if (id <= 0) {
            data.put("error", "El ID de la sub categoria debe ser mayor que cero.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(data);
        }

        Optional<SubCategory> subCategoryOptional = this.productService.findByIdSubCategory(id);
        if (subCategoryOptional.isEmpty()) {
            data.put("error", "La sub categoria no se encuentra registrado en la base de datos.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(data);
        }

        return ResponseEntity.ok(subCategoryOptional.get());
    }

    /**
     * Registra la sub categoria en la base de datos y retorna la sub categoria registrada
     *
     * @param subCategory sub categoria para el registro de datos
     * @param bindingResult valida los datos que pasan
     * @return Json con la estructura o con los mensajes de validacion {@Link SubCategory}
     */
    @PostMapping
    public ResponseEntity<Object> persistAndUpdate(@RequestBody @Valid SubCategory subCategory, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            Map<Object, Object> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(err -> errors.put(err.getField(), err.getDefaultMessage()));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(errors);
        }

        SubCategory subCategoryResponse = null;
        try {
            subCategoryResponse = this.productService.saveSubCategory(subCategory);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ex.getMessage());
        }

        return ResponseEntity.ok(subCategoryResponse);
    }

    /**
     * Actualiza el estado de la sub categoria
     * @param id identificador de la sub categoria
     * @return Json con el mensaje de confirmacion
     */
    @GetMapping("/state/{id}")
    public ResponseEntity<Object> updateState(@PathVariable("id") Long id) {
        Map<String, Object> data = new HashMap<>();

        if (id <= 0) {
            data.put("error", "El ID de la sub categoria debe ser mayor que cero.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(data);
        }

        try {
            byte state = 0;
            this.productService.updateStateSubCategory(state, id);
            data.put("mensaje", "Estado de la sub categoria modificado.");
        } catch (Exception ex) {
            data.put("error", ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(data);
        }

        return ResponseEntity.ok(data);
    }
}
