package com.jfarro.app.models.entity;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @NotBlank
    private String names;

    @Column(name = "ape_paterno")
    @NotBlank
    private String apePat;

    @Column(name = "ape_materno")
    @NotBlank
    private String apeMat;

    @Column(name = "nro_documento")
    @NotBlank
    private String nroDocu;

    @Column(name = "email")
    @NotBlank
    private String email;

    @Column(name = "observacion")
    private String observation;

    @OneToOne
    @JoinColumn(name = "id_tipo_documento")
    @NotNull
    private DocumentType documentType;

    @Embedded
    private UserDataHistory userDataHistory;

    @PostConstruct
    private void postConstruct() {
        this.userDataHistory = new UserDataHistory();
    }
}
