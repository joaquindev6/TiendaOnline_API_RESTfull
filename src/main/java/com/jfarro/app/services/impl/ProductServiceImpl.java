package com.jfarro.app.services.impl;

import com.jfarro.app.exceptions.FindByIdException;
import com.jfarro.app.exceptions.NotExistException;
import com.jfarro.app.models.entity.Category;
import com.jfarro.app.models.entity.Mark;
import com.jfarro.app.models.entity.Product;
import com.jfarro.app.models.entity.SubCategory;
import com.jfarro.app.repositories.CategoryRepository;
import com.jfarro.app.repositories.MarkRepository;
import com.jfarro.app.repositories.ProductRepository;
import com.jfarro.app.repositories.SubCategoryRepository;
import com.jfarro.app.services.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final CategoryRepository categoryRepository;
    private final MarkRepository markRepository;

    public ProductServiceImpl(ProductRepository productRepository, SubCategoryRepository subCategoryRepository,
                              CategoryRepository categoryRepository, MarkRepository markRepository) {
        this.productRepository = productRepository;
        this.subCategoryRepository = subCategoryRepository;
        this.categoryRepository = categoryRepository;
        this.markRepository = markRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> findAllProducts() {
        return this.productRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Product> findAllPagesProducts(Pageable pageable) {
        return this.productRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Product> findByIdProduct(Long id) {
        if (id == null || id <= 0) {
            throw new FindByIdException("Error findByIdProduct: El ID no puede ser null o menor igual a 0");
        }
        return this.productRepository.findById(id);
    }

    @Override
    @Transactional
    public Product saveProduct(Product product) {
        if (product == null) {
            throw new NullPointerException("Error saveProduct: El objeto Product es null");
        }
        return this.productRepository.save(product);
    }

    @Override
    @Transactional
    public void updateStateProduct(Byte state, Long id) {
        if (id == null || id <= 0) {
            throw new FindByIdException("Error updateStateProduct: El ID no puede ser null o menor igual a 0");
        }
        this.productRepository.findById(id).orElseThrow(() -> new NotExistException("Error updateStateProduct: El producto no existe en la base de datos."));
        this.productRepository.updateState(state, id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubCategory> findAllSubCategories() {
        return this.subCategoryRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SubCategory> findAllPagesSubCategory(Pageable pageable) {
        return this.subCategoryRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SubCategory> findByIdSubCategory(Long id) {
        if (id == null || id <= 0) {
            throw new FindByIdException("Error findByIdSubCategory: El ID no puede ser null o menor igual a 0");
        }
        return this.subCategoryRepository.findById(id);
    }

    @Override
    @Transactional
    public SubCategory saveSubCategory(SubCategory subCategory) {
        if (subCategory == null) {
            throw new NullPointerException("Error saveSubCategory: El objeto SubCategory es null");
        }
        return this.subCategoryRepository.save(subCategory);
    }

    @Override
    @Transactional
    public void updateStateSubCategory(Byte state, Long id) {
        if (id == null || id <= 0) {
            throw new FindByIdException("Error updateStateSubCategory: El ID no puede ser null o menor igual a 0");
        }
        this.subCategoryRepository.findById(id).orElseThrow(() -> new NotExistException("Error updateStateSubCategory: La sub categoria no existe en la base de datos."));
        this.subCategoryRepository.updateState(state, id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Category> findAllCategories() {
        return this.categoryRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Category> findAllPagesCategory(Pageable pageable) {
        return this.categoryRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Category> findByIdCategory(Long id) {
        if (id == null || id <= 0) {
            throw new FindByIdException("Error findByIdCategory: El ID no puede ser null o menor igual a 0");
        }
        return this.categoryRepository.findById(id);
    }

    @Override
    @Transactional
    public Category saveCategory(Category category) {
        if (category == null) {
            throw new NullPointerException("Error saveCategory: El objeto Category es null");
        }
        return this.categoryRepository.save(category);
    }

    @Override
    @Transactional
    public void updateStateCategory(Byte state, Long id) {
        if (id == null || id <= 0) {
            throw new FindByIdException("Error updateStateCategory: El ID no puede ser null o menor igual a 0");
        }
        this.categoryRepository.findById(id).orElseThrow(() -> new NotExistException("Error updateStateCategory: La categoria no existe en la base de datos."));
        this.categoryRepository.updateState(state, id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Mark> findAllMarks() {
        return this.markRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Mark> findAllPagesMarks(Pageable pageable) {
        return this.markRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Mark> findByIdMark(Long id) {
        if (id == null || id <= 0) {
            throw new FindByIdException("Error findByIdMark: El ID no puede ser null o menor igual a 0");
        }
        return this.markRepository.findById(id);
    }

    @Override
    @Transactional
    public Mark saveMark(Mark mark) {
        if (mark == null) {
            throw new NullPointerException("Error saveMark: El objeto Mark es null");
        }
        return this.markRepository.save(mark);
    }

    @Override
    @Transactional
    public void updateStateMark(Byte state, Long id) {
        if (id == null || id <= 0) {
            throw new FindByIdException("Error updateStateMark: El ID no puede ser null o menor igual a 0");
        }
        this.markRepository.findById(id).orElseThrow(() -> new NotExistException("Error updateStateMark: La marca no existe en la base de datos."));
        this.markRepository.updateState(state, id);
    }
}
