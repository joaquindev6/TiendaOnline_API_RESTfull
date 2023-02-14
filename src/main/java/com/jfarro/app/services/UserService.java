package com.jfarro.app.services;

import com.jfarro.app.models.entity.Role;
import com.jfarro.app.models.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> findAllUsers();
    Page<User> findAllPagesUsers(Pageable pageable);
    Optional<User> findByIdUser(Long id);
    Optional<User> findByUsernameUser(String username);
    User saveUser(User user);
    void updateStateUser(Byte state, Long id);

    List<Role> findAllRoles();
    Optional<Role> findByIdRole(Long id);
    Page<Role> findAllPagesRoles(Pageable pageable);
    Role saveRole(Role role);
    void updateStateRole(Byte state, Long id);
}
