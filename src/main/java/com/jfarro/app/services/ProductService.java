package com.jfarro.app.services;

import com.jfarro.app.models.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<Product> findAllProducts();
    Page<Product> findAllPagesProducts(Pageable pageable);
    Optional<Product> findByIdProduct(Long id);
    Product saveProduct(Product product);
    void updateStateProduct(Byte state, Long id);

    List<SubCategory> findAllSubCategories();
    Page<SubCategory> findAllPagesSubCategory(Pageable pageable);
    Optional<SubCategory> findByIdSubCategory(Long id);
    SubCategory saveSubCategory(SubCategory subCategory);
    void updateStateSubCategory(Byte state, Long id);

    List<Category> findAllCategories();
    Page<Category> findAllPagesCategory(Pageable pageable);
    Optional<Category> findByIdCategory(Long id);
    Category saveCategory(Category category);
    void updateStateCategory(Byte state, Long id);

    List<Mark> findAllMarks();
    Page<Mark> findAllPagesMarks(Pageable pageable);
    Optional<Mark> findByIdMark(Long id);
    Mark saveMark(Mark mark);
    void updateStateMark(Byte state, Long id);
}
