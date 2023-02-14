package com.jfarro.app.controllers;

import com.jfarro.app.models.entity.Sale;
import com.jfarro.app.services.InvoiceService;
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
@RequestMapping("/sales")
public class SaleRestController {

    private final InvoiceService invoiceService;

    public SaleRestController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    /**
     * Obtiene toda la lista de ventas
     * @return Json con la estructura de User {@Link Sale}
     */
    @GetMapping
    public ResponseEntity<List<Sale>> listar() {
        return ResponseEntity.ok(this.invoiceService.findAllSales());
    }

    /**
     * Obtiene una lista de ventas con paginacion
     * @param page numero de pagina
     * @param size cantidad de filas
     * @return Json con la estructura {@Link Sale}
     */
    @GetMapping("/pagination")
    public ResponseEntity<List<Sale>> listPages(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size
    ) {
        Pageable pageRequest = PageRequest.of(page, size);
        Page<Sale> ventas = this.invoiceService.findAllPagesSales(pageRequest);
        return ResponseEntity.ok(ventas.getContent());
    }

    /**
     * Obtiene la venta por medio del ID
     * @param id identificador de la venta
     * @return Json con la estructura o con los mensajes de validacion {@Link Sale}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable("id") Long id) {
        Map<String, Object> data = new HashMap<>();

        if (id <= 0) {
            data.put("error", "El ID de la venta debe ser mayor que cero.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(data);
        }

        Optional<Sale> saleOptional = this.invoiceService.findByIdSale(id);
        if (saleOptional.isEmpty()) {
            data.put("error", "La venta no se encuentra registrado en la base de datos.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(data);
        }

        return ResponseEntity.ok(saleOptional.get());
    }

    /**
     * Registra la venta en la base de datos y retorna el la venta registrada
     * @param sale venta para el registro de datos
     * @param bindingResult valida los datos que pasan
     * @return Json con la estructura o con los mensajes de validacion {@Link Sale}
     */
    @PostMapping
    public ResponseEntity<Object> persistAndUpdate(@RequestBody @Valid Sale sale, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            Map<Object, Object> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(err -> errors.put(err.getField(), err.getDefaultMessage()));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(errors);
        }

        Sale saleResponse = null;
        try {
            saleResponse = this.invoiceService.saveSale(sale);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ex.getMessage());
        }

        return ResponseEntity.ok(saleResponse);
    }

    /**
     * elimina la venta de la base de datos
     * @param id identificador de la venta
     * @return Json con el mensaje de confirmacion
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> updateState(@PathVariable("id") Long id) {
        Map<String, Object> data = new HashMap<>();

        if (id <= 0) {
            data.put("error", "El ID de la venta debe ser mayor que cero.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(data);
        }

        try {
            if (this.invoiceService.deleteSale(id)) {
                data.put("mensaje", "La venta se eliminó exitosamente.");
            }
        } catch (Exception ex) {
            data.put("error", ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(data);
        }

        return ResponseEntity.ok(data);
    }
}
