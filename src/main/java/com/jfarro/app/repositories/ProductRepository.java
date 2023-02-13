package com.jfarro.app.repositories;

import com.jfarro.app.models.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Modifying
    @Query("UPDATE Product p SET p.userDataHistory.state = :state WHERE p.id = :id")
    void updateState(@Param("state") Byte state, @Param("id") Long id);
}
