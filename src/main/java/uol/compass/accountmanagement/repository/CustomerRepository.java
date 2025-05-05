package uol.compass.accountmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uol.compass.accountmanagement.model.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
