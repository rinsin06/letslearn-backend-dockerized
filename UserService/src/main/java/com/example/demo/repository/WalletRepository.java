package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.WalletInfo;



@Repository
public interface WalletRepository extends JpaRepository<WalletInfo, Long> {
    WalletInfo findById(int id);
}

