package com.jfarro.app.controllers;

import com.jfarro.app.models.entity.Product;
import com.jfarro.app.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/products")
public class ProductRestController {

    private final ProductService productService;

    public ProductRestController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Obtiene la lista de productos
     * @return Json con la estructura del objeto {@Link Product}
     */
    @GetMapping
    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    public ResponseEntity<List<Product>> list() {
        return ResponseEntity.ok(this.productService.findAllProducts());
    }

    /**
     * Obtiene la lista de productos con paginacion
     * @param page numero de la pagina
     * @param size cantidad de filas por pagina
     * @return Json con la estructura del objeto {@Link Product}
     */
    @GetMapping("/pagination")
    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    public ResponseEntity<List<Product>> listPage(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size
    ) {
        Pageable pageRequest = PageRequest.of(page, size);
        Page<Product> products = this.productService.findAllPagesProducts(pageRequest);
        return ResponseEntity.ok(products.getContent());
    }

    /**
     * Obtiene el producto por medio del ID
     * @param id identificador del producto
     * @return Json con la estructura del objeto o con mensajes de validacion {@Link Product}
     */
    @GetMapping("/{id}")
    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    public ResponseEntity<Object> findById(@PathVariable("id") Long id) {
        if (id <= 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "El ID del producto debe ser mayor que cero."));
        }

        Optional<Product> productOptional = this.productService.findByIdProduct(id);
        if (productOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "El producto no se encuentra registrado en la base de datos."));
        }

        return ResponseEntity.ok(productOptional.get());
    }

    /**
     * Registra un producto en la base de datos
     * @param product objeto producto para el registro de sus datos
     * @param bindingResult valida los datos que se pasan
     * @return Json con la estructura del objeto o con los mensajes de validacion {@Link Product}
     */
    @PostMapping
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<Object> persistAndUpdate(@RequestBody @Valid Product product, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<Object, Object> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(err -> errors.put(err.getField(), err.getDefaultMessage()));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }

        Product productResponse = null;
        try {
            productResponse = this.productService.saveProduct(product);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }

        return ResponseEntity.ok(productResponse);
    }

    /**
     * Actualiza el estado del producto
     * @param id identificador del producto
     * @return Json con el mensaje de confirmacion
     */
    @GetMapping("/state/{id}")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<Object> updateState(@PathVariable("id") Long id) {
        Map<String, Object> data = new HashMap<>();

        if (id <= 0) {
            data.put("error", "El ID del producto debe ser mayor que cero.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(data);
        }

        try {
            byte state = 0;
            this.productService.updateStateProduct(state, id);
            data.put("mensaje", "Estado del producto modificado.");
        } catch (Exception ex) {
            data.put("error", ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(data);
        }

        return ResponseEntity.ok(data);
    }
}
