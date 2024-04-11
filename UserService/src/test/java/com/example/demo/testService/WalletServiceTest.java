package com.example.demo.testService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.example.demo.entity.Transaction;
import com.example.demo.entity.WalletInfo;
import com.example.demo.repository.TransactionRepository;
import com.example.demo.repository.WalletRepository;
import com.example.demo.service.WalletService;



@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class WalletServiceTest {
	
	
	@Autowired
	WalletService service;
	
	
	@MockBean
	WalletRepository repository;
	
	@MockBean
	TransactionRepository transactionRepository;
	
	
	@Test
	 public void getWalletByUserId() {
		
		 int userId = 1;
	        WalletInfo expectedWallet = new WalletInfo();
	        expectedWallet.setId(userId);

	        when(repository.findById(userId)).thenReturn(expectedWallet);

	        WalletInfo actualWallet = service.getWalletByUserId(userId);

	        assertEquals(expectedWallet, actualWallet);
	        verify(repository).findById(userId);
	}
	
	
	 @Test
	    public void testGetWalletByUserId_NonExistingWallet() {
	        int userId = 2;

	        when(repository.findById(userId)).thenReturn(null);

	        WalletInfo actualWallet = service.getWalletByUserId(userId);

	        assertNull(actualWallet);
	        verify(repository).findById(userId);
	    }

	    @Test
	    public void testDeposit_ExistingWallet() {
	        int userId = 1;
	        BigDecimal amount = BigDecimal.valueOf(100);

	        WalletInfo existingWallet = new WalletInfo();
	        existingWallet.setId(userId);
	        existingWallet.setBalance(BigDecimal.ZERO);
	        

	        when(repository.findById(userId)).thenReturn(existingWallet);

	        service.deposit(userId, amount);

	        assertEquals(amount, existingWallet.getBalance());
	        verify(repository).save(existingWallet);
	        verify(transactionRepository).save(any(Transaction.class));
	    }

	    @Test
	    public void testDeposit_NewWallet() {
	        int userId = 2;
	        BigDecimal amount = BigDecimal.valueOf(50);

	        when(repository.findById(userId)).thenReturn(null);

	        service.deposit(userId, amount);

	        ArgumentCaptor<WalletInfo> walletCaptor = ArgumentCaptor.forClass(WalletInfo.class);
	        verify(repository).save(walletCaptor.capture());

	        WalletInfo newWallet = walletCaptor.getValue();
	        assertEquals(userId, newWallet.getId());
	        assertEquals(amount, newWallet.getBalance());
	        verify(transactionRepository).save(any(Transaction.class));
	    }

	    @Test
	    public void testGetTransactionsForWallet_ExistingWallet() {
	        int walletId = 1;
	        List<Transaction> expectedTransactions = List.of(new Transaction());

	        WalletInfo wallet = new WalletInfo();
	        wallet.setId(walletId);
	        wallet.setTransactions(expectedTransactions);

	        when(repository.findById(walletId)).thenReturn(wallet);

	        List<Transaction> actualTransactions = service.getTransactionsForWallet(walletId);

	        assertEquals(expectedTransactions, actualTransactions);
	        verify(repository).findById(walletId);
	    }

	    @Test
	    public void testGetTransactionsForWallet_NonExistingWallet() {
	        int walletId = 2;

	        when(repository.findById(walletId)).thenReturn(null);

	        List<Transaction> actualTransactions = service.getTransactionsForWallet(walletId);

	        assertTrue(actualTransactions.isEmpty());
	        verify(repository).findById(walletId);
	    }
	
	

}
