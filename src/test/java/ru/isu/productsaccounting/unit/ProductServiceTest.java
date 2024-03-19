package ru.isu.productsaccounting.unit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.isu.productsaccounting.model.Product;
import ru.isu.productsaccounting.repository.ProductRepository;
import ru.isu.productsaccounting.service.ProductService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;

    private ProductService underTest;

    @BeforeEach
    void setUp() {
        underTest = new ProductService(productRepository);
    }

    @Test
    void shouldGetAllProducts() {
        List<Product> products = new ArrayList<>();

        products.add(new Product("name1", "descr1"));
        products.add(new Product("name2", "descr2"));
        products.add(new Product("name3", ""));

        when(productRepository.findAll()).thenReturn(products);

        List<Product> expected = underTest.getAllProducts();
        Assertions.assertEquals(products, expected);
    }

    @Test
    void shouldGetProductByName() {
        final String name = "name1";
        final Product product = new Product("name1", "descr1");

        when(productRepository.findProductByName(name)).thenReturn(Optional.of(product));

        final Optional<Product> expectedOptional = underTest.findProductByName(name);

        Assertions.assertNotNull(expectedOptional);
    }

    @Test
    void shouldSaveProductSuccessfully() {

        final Product product = new Product("name1", "descr1");

        underTest.saveProduct(product);
        verify(productRepository).save(any(Product.class));
    }


    @Test
    void shouldDeleteProduct() {
        final Product product = new Product("name1", "descr1");

        underTest.deleteProduct(product);

        verify(productRepository).delete(product);
    }
}
