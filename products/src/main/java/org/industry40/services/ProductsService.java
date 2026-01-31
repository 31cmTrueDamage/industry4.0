package org.industry40.services;

import org.industry40.data.OutboxRepository;
import org.industry40.data.ProductsRepository;
import org.industry40.enums.ProductStatus;
import org.industry40.exceptions.UnexistingProductException;
import org.industry40.models.Product;
import org.industry40.models.ProductOutbox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductsService {

    @Autowired
    private ProductsRepository productsRepository;

    @Autowired
    private OutboxRepository outboxRepository;

    public List<Product> getAll() {
        return productsRepository.findAll();
    }

    public Product get(Integer id) throws UnexistingProductException {
        return  productsRepository.findById(id).orElseThrow(() -> new UnexistingProductException("Product not found"));
    }

    public Product add(Product product) {

        Product savedProduct =  productsRepository.save(product);

        ProductOutbox outbox = new ProductOutbox();
        outbox.setProductId(savedProduct.getId());
        outbox.setStatus(ProductStatus.PENDING);

        outboxRepository.save(outbox);

        return savedProduct;
    }

    public Product update(Product product) throws UnexistingProductException {
        if (!productsRepository.existsById(product.getId())) {
            throw new UnexistingProductException("Product with id " + product.getId() + " does not exist");
        }

        Product savedProduct = productsRepository.save(product);

        ProductOutbox outbox = new ProductOutbox();
        outbox.setProductId(savedProduct.getId());
        outbox.setStatus(ProductStatus.PENDING);

        outboxRepository.save(outbox);

        return savedProduct;
    }

    public void delete(Integer Id) throws UnexistingProductException {
        if (!productsRepository.existsById(Id)) {
            throw new UnexistingProductException("Product with id " + Id + " does not exist");
        }
        productsRepository.deleteById(Id);
    }
}
