package com.jfarro.app.models.entity;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "tbl_usuarios")
@Data
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nombres")
    private String names;

    @Column(name = "ape_paterno")
    private String apePat;

    @Column(name = "ape_materno")
    private String apeMat;

    @Column(name = "email")
    private String email;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "observacion")
    private String observation;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "tbl_usuarios_roles",
            joinColumns = @JoinColumn(name = "id_usuario"),
            inverseJoinColumns = @JoinColumn(name = "id_rol"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"id_usuario", "id_rol"})
    )
    private List<Role> roles;

    @Embedded
    private UserDataHistory userDataHistory;

    @PostConstruct
    private void postConstruct() {
        this.userDataHistory = new UserDataHistory();
    }
}
