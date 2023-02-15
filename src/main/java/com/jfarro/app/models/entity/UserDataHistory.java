package com.jfarro.app.models.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Embeddable
@Data
public class UserDataHistory {

    @Column(name = "estado")
    @NotNull
    private Byte state;

    @Column(name = "user_reg")
    @NotBlank
    private String userReg;

    @Column(name = "fecha_reg")
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateReg;

    @Column(name = "user_mod")
    @NotBlank
    private String userMod;

    @Column(name = "fecha_mod")
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
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
