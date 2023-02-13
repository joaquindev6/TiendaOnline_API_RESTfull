package com.jfarro.app.models.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Data;

import java.time.LocalDate;

@Embeddable
@Data
public class UserDataHistory {

    @Column(name = "estado")
    private Byte state;

    @Column(name = "user_reg")
    private String userReg;

    @Column(name = "fecha_reg")
    private LocalDate dateReg;

    @Column(name = "user_mod")
    private String userMod;

    @Column(name = "fecha_mod")
    private LocalDate dateMod;

    @PrePersist
    private void prePersist() {
        this.state = 1;
        this.dateReg = LocalDate.now();
    }

    @PreUpdate
    private void preUpdate() {
        this.dateMod = LocalDate.now();
    }
}
