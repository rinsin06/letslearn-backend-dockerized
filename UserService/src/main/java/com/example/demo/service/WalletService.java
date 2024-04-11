package com.example.demo.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Transaction;
import com.example.demo.entity.WalletInfo;
import com.example.demo.repository.TransactionRepository;
import com.example.demo.repository.WalletRepository;



@Service
public class WalletService {

    @Autowired
    private WalletRepository walletRepository;
    
    @Autowired
    private TransactionRepository transactionRepository;

    public WalletInfo getWalletByUserId(int id) {
        return walletRepository.findById(id);
    }

    public void deposit(int Id, BigDecimal amount) {
        WalletInfo wallet = getWalletByUserId(Id);
         
        if(wallet != null)
        {
        	 wallet.setBalance(wallet.getBalance().add(amount));
        	 
        	 Transaction transaction = new Transaction();
             transaction.setDate(new Date()); // Set the date
             transaction.setDeposit(true); // Assuming it's a deposit
             transaction.setAmount(amount);
             
             // Set the wallet reference in the transaction
             transaction.setWalletInfo(wallet);
             
             // Save or update the wallet
             walletRepository.save(wallet);

             // Save the transaction
             transactionRepository.save(transaction);

             // Save or update the wallet
            
        	
        }
        else {
            // If the wallet does not exist, create a new one
            wallet = new WalletInfo();
            wallet.setId(Id);
            wallet.setBalance(amount);
            
            Transaction transaction = new Transaction();
            transaction.setDate(new Date()); // Set the date
            transaction.setDeposit(true); // Assuming it's a deposit
            transaction.setAmount(amount);
            
            // Set the wallet reference in the transaction
            transaction.setWalletInfo(wallet);
            
            // Save or update the wallet
            walletRepository.save(wallet);

            // Save the transaction
            transactionRepository.save(transaction);
        }

       
        
       
    }
    
    public List<Transaction> getTransactionsForWallet(int walletId) {
        // Fetch the wallet by ID
        WalletInfo wallet = walletRepository.findById(walletId);

        // If the wallet exists, return its transactions
        if (wallet != null) {
            return wallet.getTransactions();
        } else {
            // Wallet not found, return an empty list or handle as needed
            return List.of();
        }
    }
//
//    public boolean withdraw(User user, BigDecimal amount) {
//        Wallet wallet = getWalletByUser(user);
//        BigDecimal newBalance = wallet.getBalance().subtract(amount);
//
//        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
//            return false; // Insufficient balance
//        }
//
//        wallet.setBalance(newBalance);
//        walletRepository.save(wallet);
//        return true;
//    }
}

