package com.jfarro.app.controllers;

import com.jfarro.app.models.entity.Role;
import com.jfarro.app.services.UserService;
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
@RequestMapping("/roles")
public class RoleRestController {

    private final UserService userService;

    public RoleRestController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Obtiene la lista de roles
     * @return Json con la estructura del objeto {@Link Role}
     */
    @GetMapping
    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    public ResponseEntity<List<Role>> list() {
        return ResponseEntity.ok(this.userService.findAllRoles());
    }

    /**
     * Obtiene la lista de roles por paginacion
     * @param page numero de pagina
     * @param size cantidad de filas por pagina
     * @return Json con la estructura del objeto {@Link Role}
     */
    @GetMapping("/pagination")
    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    public ResponseEntity<List<Role>> listPages(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size
    ) {
        Pageable pageRequest = PageRequest.of(page, size);
        Page<Role> roles = this.userService.findAllPagesRoles(pageRequest);
        return ResponseEntity.ok(roles.getContent());
    }

    /**
     * Obtiene el rol por medio del ID
     * @param id identificador del rol
     * @return Json con estructura del objeto o con los mensajes de validacion {@link Role}
     */
    @GetMapping("/{id}")
    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    public ResponseEntity<Object> findById(@PathVariable("id") Long id) {
        if (id <= 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "El ID del rol debe ser mayor que cero."));
        }

        Optional<Role> roleOptional = this.userService.findByIdRole(id);
        if (roleOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "El rol no se encuentra registrado en la base de datos."));
        }

        return ResponseEntity.ok(roleOptional.get());
    }

    /**
     * Registra el rol en la base de datos
     * @param role objeto de tipo Role para el registro de datos
     * @param bindingResult valida los datos que se pasan
     * @return Json con la estructura del objeto o con los mensajes de validacion {@Link Role}
     */
    @PostMapping
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<Object> persistAndUpdate(@RequestBody @Valid Role role, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<Object, Object> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(err -> errors.put(err.getField(), err.getDefaultMessage()));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }

        Role roleResponse = null;
        try {
            roleResponse = this.userService.saveRole(role);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }

        return ResponseEntity.ok(roleResponse);
    }

    /**
     * Actualiza el estado del rol
     * @param id identificador del rol
     * @return Json con el mensaje de confirmacion
     */
    @GetMapping("/state/{id}")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<Object> updateState(@PathVariable("id") Long id) {
        Map<String, Object> data = new HashMap<>();

        if (id <= 0) {
            data.put("error", "El ID del rol debe ser mayor que cero.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(data);
        }

        try {
            byte state = 0;
            this.userService.updateStateRole(state, id);
            data.put("mensaje", "Estado del rol modificado.");
        } catch (Exception ex) {
            data.put("error", ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(data);
        }

        return ResponseEntity.ok(data);
    }
}
