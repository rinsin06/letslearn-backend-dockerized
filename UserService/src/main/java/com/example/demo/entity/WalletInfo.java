package com.example.demo.entity;

import java.math.BigDecimal;
import java.util.List;

import com.example.demo.dto.TransactionDto;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
public class WalletInfo {

    @Id
    
    private int Id;

    private BigDecimal balance;
    
    
    
    @OneToMany(mappedBy = "walletInfo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Transaction> transactions;

}
