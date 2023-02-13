package com.jfarro.app.services;

import com.jfarro.app.models.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> findAllUsers();
    Optional<User> findByIdUser(Long id);
    Optional<User> findByUsernameUser(String username);
    void saveUser(User user);
    void updateStateUser(Byte state, Long id);
}
