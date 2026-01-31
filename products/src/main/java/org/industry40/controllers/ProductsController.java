package org.industry40.controllers;

import jakarta.validation.Valid;
import org.industry40.dtos.ProductDTO;
import org.industry40.exceptions.UnexistingProductException;
import org.industry40.models.Product;
import org.industry40.services.ProductsService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/products")
public class ProductsController {

    @Autowired
    private ProductsService productsService;

    private ModelMapper modelMapper;

    public ProductsController(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @GetMapping
    ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<Product> products = productsService.getAll();
        return ResponseEntity.status(HttpStatus.OK).body(products.stream().map(c -> modelMapper.map(c, ProductDTO.class))
                .collect(Collectors.toList())
        );
    }

    @GetMapping("/{id}")
    ResponseEntity<?> get(@PathVariable("id") Integer Id) {
        Product product;

        try {
            product = productsService.get(Id);
        } catch (UnexistingProductException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.OK).body(modelMapper.map(product, ProductDTO.class));
    }

    @PostMapping
    ResponseEntity<?> add(@Valid @RequestBody ProductDTO customerDTO) {
        Product newProduct;
        try {
            newProduct = productsService.add(modelMapper.map(customerDTO, Product.class));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
        return new ResponseEntity<>(
                modelMapper.map(newProduct, ProductDTO.class), HttpStatus.CREATED
        );
    }

    @PutMapping("/{Id}")
    ResponseEntity<?> update(@Valid @PathVariable Integer Id, @RequestBody ProductDTO productDTO) {
        Product updatedProduct;

        if (productDTO.getId() != Id) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Customer Id is invalid");
        }

        try {
            updatedProduct = productsService.update(modelMapper.map(productDTO, Product.class));
        } catch (UnexistingProductException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

        return new ResponseEntity<>(
                modelMapper.map(updatedProduct, ProductDTO.class), HttpStatus.OK
        );
    }

    @DeleteMapping("/{Id}")
    ResponseEntity<?> delete(@PathVariable Integer Id) {
        try {
            productsService.delete(Id);
        } catch (UnexistingProductException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}