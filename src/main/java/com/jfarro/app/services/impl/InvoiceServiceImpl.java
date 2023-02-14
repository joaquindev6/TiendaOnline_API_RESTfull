package com.jfarro.app.services.impl;

import com.jfarro.app.exceptions.FindByIdException;
import com.jfarro.app.models.entity.DetailSale;
import com.jfarro.app.models.entity.Sale;
import com.jfarro.app.repositories.DetailSaleRepository;
import com.jfarro.app.repositories.SaleRepository;
import com.jfarro.app.services.InvoiceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    private final SaleRepository saleRepository;
    private final DetailSaleRepository detailSaleRepository;

    public InvoiceServiceImpl(SaleRepository saleRepository, DetailSaleRepository detailSaleRepository) {
        this.saleRepository = saleRepository;
        this.detailSaleRepository = detailSaleRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Sale> findAllSales() {
        return this.saleRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Sale> findAllPagesSales(Pageable pageable) {
        return this.saleRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Sale> findByIdSale(Long id) {
        if (id == null || id <= 0) {
            throw new FindByIdException("Error findByIdSale: El ID no puede ser null o menor igual a 0");
        }
        return this.saleRepository.findById(id);
    }

    @Override
    @Transactional
    public Sale saveSale(Sale sale) {
        if (sale == null) {
            throw new NullPointerException("Error saveSale: El objeto Sale es null");
        }
        return this.saleRepository.save(sale);
    }

    @Override
    @Transactional
    public Boolean deleteSale(Long id) {
        if (id == null || id <= 0) {
            throw new FindByIdException("Error deleteSale: El ID no puede ser null o menor igual a 0");
        }
        Optional<Sale> saleOptional = findByIdSale(id);
        if (saleOptional.isPresent()) {
            this.saleRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    @Transactional(readOnly = true)
    public List<DetailSale> findAllDetailSale() {
        return this.detailSaleRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DetailSale> findAllPagesDetailSales(Pageable pageable) {
        return this.detailSaleRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DetailSale> findByIdDetailSale(Long id) {
        if (id == null || id <= 0) {
            throw new FindByIdException("Error findByIdDetailSale: El ID no puede ser null o menor igual a 0");
        }
        return this.detailSaleRepository.findById(id);
    }

    @Override
    @Transactional
    public DetailSale saveDetailSale(DetailSale detailSale) {
        if (detailSale == null) {
            throw new NullPointerException("Error saveDetailSale: El objeto DetailSale es null");
        }
        return this.detailSaleRepository.save(detailSale);
    }

    @Override
    @Transactional
    public Boolean deleteDetailSale(Long id) {
        if (id == null || id <= 0) {
            throw new FindByIdException("Error deleteDetailSale: El ID no puede ser null o menor igual a 0");
        }
        Optional<DetailSale> detailSaleOptional = findByIdDetailSale(id);
        if (detailSaleOptional.isPresent()) {
            this.detailSaleRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
