package org.industry40.services;

import org.industry40.data.CustomersRepository;
import org.industry40.exceptions.UnexistingCustomerException;
import org.industry40.models.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomersService {

    @Autowired
    private CustomersRepository customersRepository;

    public List<Customer> findAll() {
        return customersRepository.findAll();
    }

    public Customer get(Integer Id) throws UnexistingCustomerException {
        return customersRepository.findById(Id).orElseThrow(() ->  new UnexistingCustomerException("Customer with id " + Id + " does not exist"));
    }

    public Customer add(Customer customer) {
        return customersRepository.save(customer);
    }

    public Customer update(Customer customer) throws UnexistingCustomerException {
        if (!customersRepository.existsById(customer.getId())) {
            throw new UnexistingCustomerException("Customer with id " + customer.getId() + " does not exist");
        }
        return customersRepository.save(customer);
    }

    public void delete(Integer Id) throws UnexistingCustomerException {
        if (!customersRepository.existsById(Id)) {
            throw new UnexistingCustomerException("Customer with id " + Id + " does not exist");
        }
        customersRepository.deleteById(Id);
    }
}
