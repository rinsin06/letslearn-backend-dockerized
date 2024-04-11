package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Transaction;
import com.example.demo.entity.WalletInfo;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

}
