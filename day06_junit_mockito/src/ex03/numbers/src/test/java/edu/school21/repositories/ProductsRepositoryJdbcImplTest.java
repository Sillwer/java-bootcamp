package edu.school21.repositories;

import edu.school21.models.Product;
import org.junit.jupiter.api.*;

import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class ProductsRepositoryJdbcImplTest {
    private static EmbeddedDatabase db;
    private static ProductsRepositoryJdbcImpl productsRepository;

    final List<Product> EXPECTED_FIND_ALL_PRODUCTS = new ArrayList<Product>(Arrays.asList(
            new Product(0L, "Laptop", 999.99),
            new Product(1L, "Smartphone", 699.50),
            new Product(2L, "Headphones", 149.99),
            new Product(3L, "Keyboard", 59.95),
            new Product(4L, "Mouse", 25.00))
    );

    final Product EXPECTED_FIND_BY_ID_2_PRODUCT = new Product(2L, "Headphones", 149.99);

    final Product EXPECTED_UPDATE_PRODUCT = new Product(1L, "Super Laptop 21", 1999.21);

    final Product EXPECTED_SAVE_PRODUCT = new Product(5L, "Catgirl wife", 999999999);

    @BeforeEach
    void init() {
        db = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.HSQL)
                .setName("Eldorado")
                .setScriptEncoding("UTF-8")
                .addScript("schema.sql")
                .addScript("data.sql")
                .build();

        productsRepository = new ProductsRepositoryJdbcImpl(db);
    }

    @AfterEach
    void close() {
        db.shutdown();
    }

    @Test
    void findAllTest() {
        Assertions.assertEquals(EXPECTED_FIND_ALL_PRODUCTS, productsRepository.findAll());
    }

    @Test
    void findByIdTest() {
        Long id = 2L;
        Optional<Product> product = productsRepository.findById(id);
        if (!product.isPresent()) {
            fail("Product with id=" + id + " not found");
        }

        Assertions.assertEquals(EXPECTED_FIND_BY_ID_2_PRODUCT, product.get());
    }

    @Test
    void updateTest() {
        Long id = 1L;
        Optional<Product> ProductOrigin = productsRepository.findById(id); // (1L, "Laptop", 999.99)
        if (!ProductOrigin.isPresent()) {
            fail("Product with id=" + id + " not found");
        }

        Product product = ProductOrigin.get();
        product.setName(EXPECTED_UPDATE_PRODUCT.getName());
        product.setPrice(EXPECTED_UPDATE_PRODUCT.getPrice());
        productsRepository.update(product);

        Optional<Product> productUpdated = productsRepository.findById(id); // new name "Super Laptop 21", new price 1999.21
        if (!productUpdated.isPresent()) {
            fail("Product with id=" + id + " not found");
        }

        Assertions.assertEquals(EXPECTED_UPDATE_PRODUCT, productUpdated.get());
    }

    @Test
    void saveTest() {
        Product newProduct = new Product(
                null,
                EXPECTED_SAVE_PRODUCT.getName(),
                EXPECTED_SAVE_PRODUCT.getPrice()
        );
        productsRepository.save(newProduct);

        Optional<Product> product = productsRepository.findById(EXPECTED_SAVE_PRODUCT.getId());
        if (!product.isPresent()) {
            fail("Product with expected id=" + EXPECTED_SAVE_PRODUCT.getId() + " not found");
        }
        Assertions.assertEquals(EXPECTED_SAVE_PRODUCT, product.get());
    }

    @Test
    void deleteTest() {
        Long deleteId = 0L;

        Optional<Product> productOptional = productsRepository.findById(deleteId);
        if (!productOptional.isPresent()) {
            fail("Product with id=" + deleteId + " not found");
        }
        productsRepository.delete(deleteId);

        Optional<Product> productDeleted = productsRepository.findById(deleteId);
        assertFalse(productDeleted.isPresent());
    }

}
