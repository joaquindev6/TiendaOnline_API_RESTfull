package com.jfarro.app.controllers;

import com.jfarro.app.models.entity.Role;
import com.jfarro.app.models.entity.User;
import com.jfarro.app.services.UserService;
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
@RequestMapping("/users")
public class UserRestController {

    private final UserService userService;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Obtiene toda la lista de usuarios
     * @return Json con la estructura de User {@Link User}
     */
    @GetMapping
    public ResponseEntity<List<User>> listar() {
        return ResponseEntity.ok(this.userService.findAllUsers());
    }

    /**
     * Obtiene una lista de usuarios con paginacion
     * @param page numero de pagina
     * @param size cantidad de filas
     * @return Json con la estructura {@Link User}
     */
    @GetMapping("/pagination")
    public ResponseEntity<List<User>> listPages(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size
    ) {
        Pageable pageRequest = PageRequest.of(page, size);
        Page<User> users = this.userService.findAllPagesUsers(pageRequest);
        return ResponseEntity.ok(users.getContent());
    }

    /**
     * Obtiene el usuario por medio del ID
     * @param id identificador del usuario
     * @return Json con la estructura o con los mensajes de validacion {@Link User}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable("id") Long id) {
        Map<String, Object> data = new HashMap<>();

        if (id <= 0) {
            data.put("error", "El ID del usuario debe ser mayor que cero.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(data);
        }

        Optional<User> userOptional = this.userService.findByIdUser(id);
        if (userOptional.isEmpty()) {
            data.put("error", "El usuario no se encuentra registrado en la base de datos.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(data);
        }

        return ResponseEntity.ok(userOptional.get());
    }

    /**
     * Registra el usuario en la base de datos y retorna el usuario registrado
     * @param user usuario para el registro de datos
     * @param bindingResult valida los datos que pasan
     * @return Json con la estructura o con los mensajes de validacion {@Link User}
     */
    @PostMapping
    public ResponseEntity<Object> persistAndUpdate(@RequestBody @Valid User user, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            Map<Object, Object> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(err -> errors.put(err.getField(), err.getDefaultMessage()));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(errors);
        }

        User userResponse = null;
        try {
            List<Role> findRolesById = new ArrayList<>();
            user.getRoles().forEach(rol -> findRolesById.add(this.userService.findByIdRole(rol.getId()).orElse(null)));
            user.setRoles(findRolesById);
            userResponse = this.userService.saveUser(user);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ex.getMessage());
        }

        return ResponseEntity.ok(userResponse);
    }

    /**
     * Actualiza el estado del usuario
     * @param id identificador del usuario
     * @return Json con el mensaje de confirmacion
     */
    @GetMapping("/state/{id}")
    public ResponseEntity<Object> updateState(@PathVariable("id") Long id) {
        Map<String, Object> data = new HashMap<>();

        if (id <= 0) {
            data.put("error", "El ID del usuario debe ser mayor que cero.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(data);
        }

        try {
            byte state = 0;
            this.userService.updateStateUser(state, id);
            data.put("mensaje", "Estado del usuario modificado.");
        } catch (Exception ex) {
            data.put("error", ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(data);
        }

        return ResponseEntity.ok(data);
    }
}
