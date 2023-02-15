package com.jfarro.app.controllers;

import com.jfarro.app.models.entity.DetailSale;
import com.jfarro.app.services.InvoiceService;
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
@RequestMapping("/detail_sales")
public class DetailSaleRestController {

    private final InvoiceService invoiceService;

    public DetailSaleRestController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    /**
     * Obtiene toda la lista de detalles de ventas
     * @return Json con la estructura de User {@Link DetailSale}
     */
    @GetMapping
    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    public ResponseEntity<List<DetailSale>> listar() {
        return ResponseEntity.ok(this.invoiceService.findAllDetailSale());
    }

    /**
     * Obtiene una lista de detalles de ventas con paginacion
     * @param page numero de pagina
     * @param size cantidad de filas
     * @return Json con la estructura {@Link DetailSale}
     */
    @GetMapping("/pagination")
    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    public ResponseEntity<List<DetailSale>> listPages(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size
    ) {
        Pageable pageRequest = PageRequest.of(page, size);
        Page<DetailSale> detailSales = this.invoiceService.findAllPagesDetailSales(pageRequest);
        return ResponseEntity.ok(detailSales.getContent());
    }

    /**
     * Obtiene el detalle de venta por medio del ID
     * @param id identificador del detalle de venta
     * @return Json con la estructura o con los mensajes de validacion {@Link DetailSale}
     */
    @GetMapping("/{id}")
    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    public ResponseEntity<Object> findById(@PathVariable("id") Long id) {
        Map<String, Object> data = new HashMap<>();

        if (id <= 0) {
            data.put("error", "El ID del detalle de venta debe ser mayor que cero.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(data);
        }

        Optional<DetailSale> detailSaleOptional = this.invoiceService.findByIdDetailSale(id);
        if (detailSaleOptional.isEmpty()) {
            data.put("error", "El detalle de venta no se encuentra registrado en la base de datos.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(data);
        }

        return ResponseEntity.ok(detailSaleOptional.get());
    }

    /**
     * Registra el detalle de venta en la base de datos y retorna el objeto registrada
     *
     * @param detailSale detalle de venta para el registro de datos
     * @param bindingResult valida los datos que pasan
     * @return Json con la estructura o con los mensajes de validacion {@Link DetailSale}
     */
    @PostMapping
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<Object> persistAndUpdate(@RequestBody @Valid DetailSale detailSale, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            Map<Object, Object> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(err -> errors.put(err.getField(), err.getDefaultMessage()));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(errors);
        }

        DetailSale detailSaleResponse = null;
        try {
            detailSaleResponse = this.invoiceService.saveDetailSale(detailSale);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ex.getMessage());
        }

        return ResponseEntity.ok(detailSaleResponse);
    }

    /**
     * elimina el detalle de la venta de la base de datos
     * @param id identificador del detalle de venta
     * @return Json con el mensaje de confirmacion
     */
    @DeleteMapping("/{id}")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<Object> updateState(@PathVariable("id") Long id) {
        Map<String, Object> data = new HashMap<>();

        if (id <= 0) {
            data.put("error", "El ID del detalle de venta debe ser mayor que cero.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(data);
        }

        try {
            if (this.invoiceService.deleteDetailSale(id)) {
                data.put("mensaje", "El detalle de venta se eliminó exitosamente.");
            }
        } catch (Exception ex) {
            data.put("error", ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(data);
        }

        return ResponseEntity.ok(data);
    }
}
