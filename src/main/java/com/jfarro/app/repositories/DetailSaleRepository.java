package com.jfarro.app.repositories;

import com.jfarro.app.models.entity.DetailSale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetailSaleRepository extends JpaRepository<DetailSale, Long> {
}