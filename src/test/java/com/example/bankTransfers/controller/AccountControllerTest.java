package com.example.bankTransfers.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.example.bankTransfers.dto.AccountRequest;
import com.example.bankTransfers.model.Account;
import com.example.bankTransfers.repository.AccountRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc 
@ActiveProfiles("test")
@Import(TestConfig.class)
public class AccountControllerTest {

	@Autowired
    private MockMvc mockMvc;
	
    @MockBean
    private AccountRepository accountRepository;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Test
    public void testCreateTransfer() throws Exception {
    	
    	AccountRequest request = new AccountRequest(BigDecimal.valueOf(5000), "André", "Pereira");
    	
    	Account mockAccount = new Account(1L, request.getBalance(), request.getFirstName(), request.getLastName(), LocalDate.now(), true);
    			
    	Mockito.when(accountRepository.save(Mockito.any(Account.class)))
 	   		.thenReturn(mockAccount);
    	
    	mockMvc.perform(post("/account")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        //.andDo(MockMvcResultHandlers.print())
        .andExpect(status().isOk())  
        .andExpect(jsonPath("$.status").value(200))  
        .andExpect(jsonPath("$.path").value("/account"))
        .andExpect(jsonPath("$.data.id").value(1)) 
        .andExpect(jsonPath("$.data.balance").value(5000)) 
        .andExpect(jsonPath("$.data.firstName").value("André"))  
        .andExpect(jsonPath("$.data.lastName").value("Pereira"))
        .andExpect(jsonPath("$.data.creationDate").value(LocalDate.now().toString()))
        .andExpect(jsonPath("$.data.active").value(true));
    }
    
    @Test
    public void testUpdateAccount() throws Exception {

        AccountRequest request = new AccountRequest(BigDecimal.valueOf(6000), "André", "Updated");

        Account existingAccount = new Account(1L, BigDecimal.valueOf(5000), "André", "Pereira", LocalDate.now().minusDays(5), true);
        Mockito.when(accountRepository.findById(1L)).thenReturn(Optional.of(existingAccount));

        Account updatedAccount = new Account(1L, BigDecimal.valueOf(6000), "André", "Updated", existingAccount.getCreationDate(), true);

        Mockito.when(accountRepository.save(Mockito.any(Account.class))).thenReturn(updatedAccount);

        mockMvc.perform(put("/account/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                //.andDo(MockMvcResultHandlers.print()) 
                .andExpect(status().isOk())  
                .andExpect(jsonPath("$.status").value(200))  
                .andExpect(jsonPath("$.path").value("/account/1")) 
                .andExpect(jsonPath("$.data.id").value(1)) 
                .andExpect(jsonPath("$.data.balance").value(6000))  
                .andExpect(jsonPath("$.data.firstName").value("André"))  
                .andExpect(jsonPath("$.data.lastName").value("Updated"))  
                .andExpect(jsonPath("$.data.creationDate").value(LocalDate.now().minusDays(5).toString()))
                .andExpect(jsonPath("$.data.active").value(true)); 
    }

    
    @Test
    public void testGetAllAccounts() throws Exception {

        Account account1 = new Account(1L, BigDecimal.valueOf(5000), "André", "Pereira", LocalDate.now(), true);
        Account account2 = new Account(2L, BigDecimal.valueOf(3000), "João", "Silva", LocalDate.now(), true);
        
        List<Account> accounts = Arrays.asList(account1, account2);

        Mockito.when(accountRepository.findAll()).thenReturn(accounts);

        mockMvc.perform(get("/account"))
                //.andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk()) 
                .andExpect(jsonPath("$.length()").value(2)) 
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].balance").value(5000))
                .andExpect(jsonPath("$[0].firstName").value("André"))
                .andExpect(jsonPath("$[0].lastName").value("Pereira"))
                .andExpect(jsonPath("$[0].creationDate").value(LocalDate.now().toString()))
                .andExpect(jsonPath("$[0].active").value(true))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].balance").value(3000))
                .andExpect(jsonPath("$[1].firstName").value("João"))
                .andExpect(jsonPath("$[1].lastName").value("Silva"))
                .andExpect(jsonPath("$[1].creationDate").value(LocalDate.now().toString()))
                .andExpect(jsonPath("$[1].active").value(true));
    }
    
    @Test
    public void testGetAccountById() throws Exception {

        Account account = new Account(1L, BigDecimal.valueOf(5000), "André", "Pereira", LocalDate.now(), true);

        Mockito.when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        mockMvc.perform(get("/account/{id}", 1L))
                //.andDo(MockMvcResultHandlers.print()) 
                .andExpect(status().isOk()) // 
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.balance").value(5000))
                .andExpect(jsonPath("$.firstName").value("André"))
                .andExpect(jsonPath("$.lastName").value("Pereira"))
                .andExpect(jsonPath("$.creationDate").value(LocalDate.now().toString()))
                .andExpect(jsonPath("$.active").value(true));
    }
    
    @Test
    public void testDeleteAccount_Success() throws Exception {

        Mockito.doNothing().when(accountRepository).deleteById(1L);

        mockMvc.perform(delete("/account/{id}", 1L))
                //.andDo(MockMvcResultHandlers.print()) 
                .andExpect(status().isOk()) 
                .andExpect(content().string("")); 
    }
    
}
