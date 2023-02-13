package com.jfarro.app.services;

import com.jfarro.app.models.entity.DetailSale;
import com.jfarro.app.models.entity.Sale;

import java.util.List;
import java.util.Optional;

public interface InvoiceService {
    List<Sale> findAllSales();
    Optional<Sale> findByIdSale(Long id);
    Sale saveSale(Sale sale);
    Boolean deleteSale(Long id);

    List<DetailSale> findAllDetailSale();
    Optional<DetailSale> findByIdDetailSale(Long id);
    DetailSale saveDetailSale(DetailSale detailSale);
    Boolean deleteDetailSale(Long id);
}
