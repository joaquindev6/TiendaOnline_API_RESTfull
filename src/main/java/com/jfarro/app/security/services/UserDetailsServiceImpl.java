package com.jfarro.app.security.services;

import com.jfarro.app.models.entity.User;
import com.jfarro.app.security.UserDetailsImpl;
import com.jfarro.app.services.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;

    public UserDetailsServiceImpl(UserService userService) {
        this.userService = userService;
    }

    /**
     * Obtengo el usuario a traves del username y lo registro en el UserDetails
     * @param username
     * @return objeto de tipo UserDatails
     * @throws UsernameNotFoundException
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userService.findByUsernameUser(username)
                .orElseThrow(() -> new UsernameNotFoundException("El usuario '".concat(username).concat("' no existe.")));
        return new UserDetailsImpl(user);
    }
}
