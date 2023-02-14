package com.jfarro.app.services;

import com.jfarro.app.models.entity.DetailSale;
import com.jfarro.app.models.entity.Product;
import com.jfarro.app.models.entity.Sale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface InvoiceService {
    List<Sale> findAllSales();
    Page<Sale> findAllPagesSales(Pageable pageable);
    Optional<Sale> findByIdSale(Long id);
    Sale saveSale(Sale sale);
    Boolean deleteSale(Long id);

    List<DetailSale> findAllDetailSale();
    Page<DetailSale> findAllPagesDetailSales(Pageable pageable);
    Optional<DetailSale> findByIdDetailSale(Long id);
    DetailSale saveDetailSale(DetailSale detailSale);
    Boolean deleteDetailSale(Long id);
}
