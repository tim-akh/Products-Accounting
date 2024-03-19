package ru.isu.productsaccounting.converter;


import org.springframework.beans.factory.annotation.Autowired;
import ru.isu.productsaccounting.model.Product;

import org.springframework.core.convert.converter.Converter;
import ru.isu.productsaccounting.repository.ProductRepository;

import java.util.Optional;

public class ProductConverter implements Converter<Long, Optional<Product>> {


    private final ProductRepository productRepository;

    public ProductConverter(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Optional<Product> convert(Long id) {
        return productRepository.findById(id);
    }
}
