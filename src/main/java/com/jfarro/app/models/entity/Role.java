package com.jfarro.app.models.entity;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Entity
@Table(name = "tbl_roles")
@Data
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "codigo")
    private String code;

    @Column(name = "nombre")
    private String name;

    @Column(name = "descripcion")
    private String description;

    @Embedded
    private UserDataHistory userDataHistory;

    @PostConstruct
    private void postConstruct() {
        this.userDataHistory = new UserDataHistory();
    }
}