package com.jfarro.app.services.impl;

import com.jfarro.app.exceptions.FIndByStringException;
import com.jfarro.app.exceptions.FindByIdException;
import com.jfarro.app.exceptions.NotExistException;
import com.jfarro.app.models.entity.User;
import com.jfarro.app.repositories.UserRepository;
import com.jfarro.app.services.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findAllUsers() {
        return this.userRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByIdUser(Long id) {
        if (id == null || id <= 0) {
            throw new FindByIdException("Error findByIdUser: El ID no puede ser null o menor igual a 0");
        }
        return this.userRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByUsernameUser(String username) {
        if (username == null || username.isBlank()) {
            throw new FIndByStringException("Error findByUsernameUser: El username no puede ser null o estar en blanco");
        }
        return this.userRepository.findByUsername(username);
    }

    @Override
    @Transactional
    public void saveUser(User user) {
        if (user == null) {
            throw new NullPointerException("Error saveUser: El objeto User es null");
        }
        this.userRepository.save(user);
    }

    @Override
    @Transactional
    public void updateStateUser(Byte state, Long id) {
        if (id == null || id <= 0) {
            throw new FindByIdException("Error updateStateUser: El ID no puede ser null o menor igual a 0");
        }
        this.userRepository.findById(id).orElseThrow(() -> new NotExistException("Error updateStateUser: El usuario no existe en la base de datos."));
        this.userRepository.updateState(state, id);
    }
}
