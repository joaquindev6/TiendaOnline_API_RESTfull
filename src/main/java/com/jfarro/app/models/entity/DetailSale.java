package com.jfarro.app.models.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

@Entity
@Table(name = "tbl_detalle_ventas")
@Data
public class DetailSale implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nombre_producto")
    @NotBlank
    private String nameProduct;

    @Column(name = "precio_producto")
    @NotNull
    private Double priceProduct;

    @Column(name = "cantidad")
    @NotNull
    @Min(0)
    private Integer amount;

    @Column(name = "sub_total")
    @NotNull
    @Min(0)
    private Double subTotal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_producto")
    @NotNull
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Product products;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_venta")
    @NotNull
    @JsonBackReference
    private Sale sale;
}
