package org.lordrose.vrms.repositories;

import org.lordrose.vrms.domains.IncurredExpense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IncurredExpenseRepository extends JpaRepository<IncurredExpense, Long> {
}
