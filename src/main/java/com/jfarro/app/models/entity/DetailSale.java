package com.jfarro.app.models.entity;

import jakarta.persistence.*;
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
    private String nameProduct;

    @Column(name = "precio_producto")
    private String priceProduct;

    @Column(name = "cantidad")
    private Integer amount;

    @Column(name = "sub_total")
    private Double subTotal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_producto")
    private Product products;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_venta")
    private Sale sale;
}
