package com.jfarro.app.services;

import com.jfarro.app.models.entity.Category;
import com.jfarro.app.models.entity.Mark;
import com.jfarro.app.models.entity.Product;
import com.jfarro.app.models.entity.SubCategory;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<Product> findAllProducts();
    Optional<Product> findByIdProduct(Long id);
    Product saveProduct(Product product);
    void updateStateProduct(Byte state, Long id);

    List<SubCategory> findAllSubCategories();
    Optional<SubCategory> findByIdSubCategory(Long id);
    SubCategory saveSubCategory(SubCategory subCategory);
    void updateStateSubCategory(Byte state, Long id);

    List<Category> findAllCategories();
    Optional<Category> findByIdCategory(Long id);
    Category saveCategory(Category category);
    void updateStateCategory(Byte state, Long id);

    List<Mark> findAllMarks();
    Optional<Mark> findByIdMark(Long id);
    Mark saveMark(Mark mark);
    void updateStateMark(Byte state, Long id);
}
