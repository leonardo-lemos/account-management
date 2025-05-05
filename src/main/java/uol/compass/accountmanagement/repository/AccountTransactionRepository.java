package uol.compass.accountmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uol.compass.accountmanagement.model.AccountTransaction;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AccountTransactionRepository extends JpaRepository<AccountTransaction, Long> {

    @Query("""
            SELECT at FROM AccountTransaction at
            WHERE at.account.id = :accountId
            AND at.timestamp BETWEEN :startDate AND :endDate
            """)
    List<AccountTransaction> findAllByAccountAndPeriod(Long accountId, LocalDateTime startDate, LocalDateTime endDate);
}
