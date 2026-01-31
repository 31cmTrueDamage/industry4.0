package org.industry40.controllers;

import jakarta.validation.Valid;
import org.industry40.dtos.CustomerDTO;
import org.industry40.exceptions.UnexistingCustomerException;
import org.industry40.models.Customer;
import org.industry40.services.CustomersService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customers")
public class CustomersController {

    @Autowired
    private CustomersService customersService;

    private ModelMapper modelMapper;

    public CustomersController(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @GetMapping("/{id}")
    ResponseEntity<?> get(@PathVariable("id") Integer Id) {
        Customer customer;

        try {
            customer = customersService.get(Id);
        } catch (UnexistingCustomerException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.OK).body(modelMapper.map(customer, CustomerDTO.class));
    }

    @PostMapping
    ResponseEntity<?> add(@Valid @RequestBody CustomerDTO customerDTO) {
        Customer newCustomer;
        try {
            newCustomer = customersService.add(modelMapper.map(customerDTO, Customer.class));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
        return new ResponseEntity<>(
                modelMapper.map(newCustomer, CustomerDTO.class), HttpStatus.CREATED
        );
    }

    @PutMapping("/{Id}")
    ResponseEntity<?> update(@Valid @PathVariable Integer Id, @RequestBody CustomerDTO customerDTO) {
        Customer updatedCustomer;

        if (customerDTO.getId() != Id) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Customer Id is invalid");
        }

        try {
            updatedCustomer = customersService.update(modelMapper.map(customerDTO, Customer.class));
        } catch (UnexistingCustomerException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

        return new ResponseEntity<>(
                modelMapper.map(updatedCustomer, CustomerDTO.class), HttpStatus.OK
        );
    }

    @DeleteMapping("/{Id}")
    ResponseEntity<?> delete(@PathVariable Integer Id) {
        try {
            customersService.delete(Id);
        } catch (UnexistingCustomerException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
