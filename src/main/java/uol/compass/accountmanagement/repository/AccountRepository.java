package uol.compass.accountmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uol.compass.accountmanagement.model.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

}
