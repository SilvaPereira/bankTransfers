package com.example.bankTransfers.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDate;

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
        .andDo(MockMvcResultHandlers.print())
        .andExpect(status().isOk())  
        .andExpect(jsonPath("$.status").value(200))  
        .andExpect(jsonPath("$.path").value("/account"))
        //.andExpect(jsonPath("$.data.id").value(1)) 
        .andExpect(jsonPath("$.data.balance").value(5000)) 
        .andExpect(jsonPath("$.data.firstName").value("André"))  
        .andExpect(jsonPath("$.data.lastName").value("Pereira"))
        .andExpect(jsonPath("$.data.creationDate").value(LocalDate.now().toString()))
        .andExpect(jsonPath("$.data.active").value(true));
    	
    	
    }
    
}
