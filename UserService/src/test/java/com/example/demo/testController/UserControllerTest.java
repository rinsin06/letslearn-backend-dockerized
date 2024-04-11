package com.example.demo.testController;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.Controller.UserController;
import com.example.demo.entity.Transaction;
import com.example.demo.entity.WalletInfo;
import com.example.demo.feign.AuthInterface;
import com.example.demo.service.TwilioOtpService;
import com.example.demo.service.WalletService;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthInterface authInterface;

    @MockBean
    private WalletService walletService;
    
    
    @MockBean
    private RazorpayClient razorpayClient;
    
    
    @MockBean
    private TwilioOtpService twilioOtpService;


    @Test
    void testGetProfile() throws Exception {
    	
    	WalletInfo wallet = new WalletInfo();
    	
    	wallet.setId(1);
    	
    	wallet.setBalance(BigDecimal.TEN);
    	
    	wallet.setTransactions(List.of());
        // Mocking
    	 String username = "testUser";
         ResponseEntity<Map<String, String>> userResponse = ResponseEntity.ok(Map.of("id","1", "username", username));
         when(authInterface.getUser(username)).thenReturn(userResponse);


        when(walletService.getWalletByUserId(1))
                .thenReturn(wallet);

        // Test
        mockMvc.perform(get("/user/profile")
                .param("username", "testUser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Balance").value(10.0))
                .andExpect(jsonPath("$.user.body.username").value(username))
                .andExpect(jsonPath("$.transactions").isArray())
                .andExpect(jsonPath("$.transactions").isEmpty());
    }

    @Test
    void testViewWallet() throws Exception {
    	
    	WalletInfo wallet = new WalletInfo();
    	
    	wallet.setId(1);
    	
    	wallet.setBalance(BigDecimal.TEN);
    	
    	wallet.setTransactions(List.of());
    	
        // Mocking
        when(walletService.getWalletByUserId(1))
                .thenReturn(wallet);

        // Test
        mockMvc.perform(get("/user/wallet")
                .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Balance").value(10.0));
    }

    @Test
    void testDeposit() throws Exception {
    	
WalletInfo wallet = new WalletInfo();
    	
    	wallet.setId(1);
    	
    	wallet.setBalance(BigDecimal.TEN);
    	
    	wallet.setTransactions(List.of());
        // Mocking
        
        when(walletService.getWalletByUserId(1)).thenReturn(wallet);

        // Test
        mockMvc.perform(post("/user/wallet/add")
                .content("{\"id\":1,\"balance\":5}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Balance").value("added"));
    }

    

	@Test
    void testGetTransactions() throws Exception {
        // Mocking
    	Transaction transaction1 = new Transaction();
    	
    	transaction1.setId(1);
    	transaction1.setAmount(BigDecimal.TEN);
    	transaction1.setDeposit(true);
    	
Transaction transaction2 = new Transaction();
    	
transaction2.setId(2);
transaction2.setAmount(BigDecimal.ONE);
transaction2.setDeposit(false);
    	
        List<Transaction> transactions = Arrays.asList(
               transaction1,transaction2
        );
        when(walletService.getTransactionsForWallet(1)).thenReturn(transactions);

        // Test
        mockMvc.perform(get("/user/wallet/transactions/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].amount").value(10.0))
                .andExpect(jsonPath("$[0].deposit").value(true))
                .andExpect(jsonPath("$[1].amount").value(1.0))
                .andExpect(jsonPath("$[1].deposit").value(false));
    }

   
}
