package com.farhanalwahid.backend.repository;

import com.farhanalwahid.backend.model.BorrowingTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BorrowingTransactionRepository extends JpaRepository<BorrowingTransaction, Long> {
}