package com.jfarro.app.models.entity;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Entity
@Table(name = "tbl_clientes")
@Data
public class Client implements Serializable {

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

    @Column(name = "nro_documento")
    private String nroDocu;

    @Column(name = "email")
    private String email;

    @Column(name = "observacion")
    private String observation;

    @OneToOne
    @JoinColumn(name = "id_tipo_documento")
    private DocumentType documentType;

    @Embedded
    private UserDataHistory userDataHistory;

    @PostConstruct
    private void postConstruct() {
        this.userDataHistory = new UserDataHistory();
    }
}
