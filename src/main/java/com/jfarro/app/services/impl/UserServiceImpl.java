package com.jfarro.app.services.impl;

import com.jfarro.app.exceptions.FIndByStringException;
import com.jfarro.app.exceptions.FindByIdException;
import com.jfarro.app.exceptions.NotExistException;
import com.jfarro.app.models.entity.Role;
import com.jfarro.app.models.entity.User;
import com.jfarro.app.repositories.RoleRepository;
import com.jfarro.app.repositories.UserRepository;
import com.jfarro.app.services.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findAllUsers() {
        return this.userRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<User> findAllPagesUsers(Pageable pageable) {
        return this.userRepository.findAll(pageable);
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
    public User saveUser(User user) {
        if (user == null) {
            throw new NullPointerException("Error saveUser: El objeto User es null");
        }
        return this.userRepository.save(user);
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

    @Override
    @Transactional(readOnly = true)
    public List<Role> findAllRoles() {
        return this.roleRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Role> findByIdRole(Long id) {
        if (id == null || id <= 0) {
            throw new FindByIdException("Error findByIdRole: El ID no puede ser null o menor igual a 0");
        }
        return this.roleRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Role> findAllPagesRoles(Pageable pageable) {
        return this.roleRepository.findAll(pageable);
    }

    @Override
    @Transactional
    public Role saveRole(Role role) {
        if (role == null) {
            throw new NullPointerException("Error saveRole: El objeto Role es null");
        }
        return this.roleRepository.save(role);
    }

    @Override
    @Transactional
    public void updateStateRole(Byte state, Long id) {
        if (id == null || id <= 0) {
            throw new FindByIdException("Error updateStateRole: El ID no puede ser null o menor igual a 0");
        }
        this.roleRepository.findById(id).orElseThrow(() -> new NotExistException("Error updateStateRole: El rol no existe en la base de datos."));
        this.roleRepository.updateState(state, id);
    }
}
