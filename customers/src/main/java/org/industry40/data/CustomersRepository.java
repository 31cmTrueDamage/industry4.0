package org.industry40.data;

import org.industry40.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomersRepository extends JpaRepository<Customer, Integer> {
}
