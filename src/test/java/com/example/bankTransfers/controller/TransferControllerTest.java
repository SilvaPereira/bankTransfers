package com.example.bankTransfers.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
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
import com.example.bankTransfers.dto.TransferRequest;
import com.example.bankTransfers.model.Account;
import com.example.bankTransfers.model.Transfer;
import com.example.bankTransfers.repository.AccountRepository;
import com.example.bankTransfers.repository.TransferRepository;
import com.example.bankTransfers.service.TransferService;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc 
@ActiveProfiles("test")
@Import(TestConfig.class)
public class TransferControllerTest {

	@Autowired
    private MockMvc mockMvc;

	@MockBean
    private TransferService transferService;

    @MockBean
    private TransferRepository transferRepository;

    @MockBean
    private AccountRepository accountRepository;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @BeforeEach
    void setUp() {
        accountRepository.deleteAll();

        Account account1 = new Account();
        account1.setId(1L);
        account1.setFirstName("First1");
        account1.setLastName("Last1");
        account1.setBalance(BigDecimal.valueOf(1000));
        accountRepository.save(account1);

        Account account2 = new Account();
        account2.setId(2L);
        account2.setFirstName("First2");
        account2.setLastName("Last2");
        account2.setBalance(BigDecimal.valueOf(2000));
        accountRepository.save(account2);
        
        Mockito.when(accountRepository.existsById(1L)).thenReturn(true);
        Mockito.when(accountRepository.existsById(2L)).thenReturn(true);
        Mockito.when(accountRepository.findById(1L)).thenReturn(Optional.of(account1));
        Mockito.when(accountRepository.findById(2L)).thenReturn(Optional.of(account2));
    }
    
    @Test
    public void testCreateTransfer() throws Exception {

    	TransferRequest request = new TransferRequest();
        request.setFromAccount(1L);
        request.setToAccount(2L);
        request.setAmount(BigDecimal.valueOf(1200));
        request.setScheduledDate(LocalDate.now().plusDays(5));

        Transfer mockTransfer = new Transfer(request.getFromAccount(), request.getToAccount(), 
                request.getAmount(), request.getScheduledDate(), 
                BigDecimal.valueOf(108.0)); 

        Mockito.when(transferService.calculateFee(BigDecimal.valueOf(1200), LocalDate.now().plusDays(5)))
        .thenReturn(BigDecimal.valueOf(108.00));
        
        Mockito.when(transferRepository.save(Mockito.any(Transfer.class)))
        	   .thenReturn(mockTransfer);
        
        mockMvc.perform(post("/transfers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        //.andDo(MockMvcResultHandlers.print())
        .andExpect(status().isOk())  
        .andExpect(jsonPath("$.status").value(200))  
        .andExpect(jsonPath("$.path").value("/transfers"))
        .andExpect(jsonPath("$.data.fromAccount").value(1)) 
        .andExpect(jsonPath("$.data.toAccount").value(2))  
        .andExpect(jsonPath("$.data.amount").value(1200)) 
        .andExpect(jsonPath("$.data.scheduledDate").value(LocalDate.now().plusDays(5).toString())) 
        .andExpect(jsonPath("$.data.fee").value(108.00));
    }
    
    @Test
    public void testUpdateTransfer() throws Exception {

    	TransferRequest request = new TransferRequest();
        request.setFromAccount(1L);
        request.setToAccount(2L);
        request.setAmount(BigDecimal.valueOf(1500));
        request.setScheduledDate(LocalDate.now().plusDays(8));
        
        Transfer existingTransfer = new Transfer();
    	existingTransfer.setFromAccount(1L);
    	existingTransfer.setToAccount(2L);
    	existingTransfer.setAmount(BigDecimal.valueOf(1200));
    	existingTransfer.setScheduledDate(LocalDate.now().plusDays(5));
    	existingTransfer.setFee(BigDecimal.valueOf(30));
    	existingTransfer.setId(1L);
    	
    	Mockito.when(transferService.calculateFee(BigDecimal.valueOf(1500), LocalDate.now().plusDays(8)))
    	.thenReturn(BigDecimal.valueOf(135.00));
    	
    	Mockito.when(transferRepository.findById(anyLong())).thenReturn(Optional.of(existingTransfer));
    	Mockito.when(transferRepository.save(any(Transfer.class))).thenReturn(existingTransfer);
    	
    	mockMvc.perform(put("/transfers/1")
                 .contentType(MediaType.APPLICATION_JSON)
                 .content(objectMapper.writeValueAsString(request)))
    	//.andDo(MockMvcResultHandlers.print())
        .andExpect(status().isOk())  
        .andExpect(jsonPath("$.status").value(200))  
        .andExpect(jsonPath("$.path").value("/transfers/1"))
        .andExpect(jsonPath("$.data.id").value(1))
    	.andExpect(jsonPath("$.data.fromAccount").value(1)) 
        .andExpect(jsonPath("$.data.toAccount").value(2))  
        .andExpect(jsonPath("$.data.amount").value(1500)) 
        .andExpect(jsonPath("$.data.scheduledDate").value(LocalDate.now().plusDays(8).toString()))
        .andExpect(jsonPath("$.data.fee").value(135.00));
    }
    
    @Test
    public void testUpdateTransferWithoutDate() throws Exception {

    	TransferRequest request = new TransferRequest();
        request.setFromAccount(1L);
        request.setToAccount(2L);
        request.setAmount(BigDecimal.valueOf(1500));
        
        Transfer existingTransfer = new Transfer();
    	existingTransfer.setFromAccount(1L);
    	existingTransfer.setToAccount(2L);
    	existingTransfer.setAmount(BigDecimal.valueOf(1200));
    	existingTransfer.setScheduledDate(LocalDate.now().plusDays(5));
    	existingTransfer.setFee(BigDecimal.valueOf(30));
    	existingTransfer.setId(1L);
    	
    	Mockito.when(transferService.calculateFee(BigDecimal.valueOf(1500), LocalDate.now().plusDays(5)))
    	.thenReturn(BigDecimal.valueOf(135.00));
    	
    	Mockito.when(transferRepository.findById(anyLong())).thenReturn(Optional.of(existingTransfer));
    	Mockito.when(transferRepository.save(any(Transfer.class))).thenReturn(existingTransfer);
    	
    	mockMvc.perform(put("/transfers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
       //.andDo(MockMvcResultHandlers.print())
       .andExpect(status().isOk())  
       .andExpect(jsonPath("$.status").value(200))  
       .andExpect(jsonPath("$.path").value("/transfers/1"))
       .andExpect(jsonPath("$.data.id").value(1))
   	   .andExpect(jsonPath("$.data.fromAccount").value(1)) 
       .andExpect(jsonPath("$.data.toAccount").value(2))  
       .andExpect(jsonPath("$.data.amount").value(1500)) 
       .andExpect(jsonPath("$.data.scheduledDate").value(LocalDate.now().plusDays(5).toString()))
       .andExpect(jsonPath("$.data.fee").value(135.00));
    	 
    }
    
    @Test
    public void testUpdateTransferWithoutAmount() throws Exception {

    	TransferRequest request = new TransferRequest();
        request.setFromAccount(1L);
        request.setToAccount(2L);
        request.setScheduledDate(LocalDate.now().plusDays(9));
        
        Transfer existingTransfer = new Transfer();
    	existingTransfer.setFromAccount(1L);
    	existingTransfer.setToAccount(2L);
    	existingTransfer.setAmount(BigDecimal.valueOf(1200));
    	existingTransfer.setScheduledDate(LocalDate.now().plusDays(5));
    	existingTransfer.setFee(BigDecimal.valueOf(30));
    	existingTransfer.setId(1L);
    	
    	Mockito.when(transferService.calculateFee(BigDecimal.valueOf(1200), LocalDate.now().plusDays(9)))
    	.thenReturn(BigDecimal.valueOf(108.00));
    	
    	Mockito.when(transferRepository.findById(anyLong())).thenReturn(Optional.of(existingTransfer));
    	Mockito.when(transferRepository.save(any(Transfer.class))).thenReturn(existingTransfer);
    	
    	mockMvc.perform(put("/transfers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
       //.andDo(MockMvcResultHandlers.print())
       .andExpect(status().isOk())  
       .andExpect(jsonPath("$.status").value(200))  
       .andExpect(jsonPath("$.path").value("/transfers/1"))
       .andExpect(jsonPath("$.data.id").value(1))
   	   .andExpect(jsonPath("$.data.fromAccount").value(1)) 
       .andExpect(jsonPath("$.data.toAccount").value(2))  
       .andExpect(jsonPath("$.data.amount").value(1200)) 
       .andExpect(jsonPath("$.data.scheduledDate").value(LocalDate.now().plusDays(9).toString()))
       .andExpect(jsonPath("$.data.fee").value(108.00));
    	 
    }
    
    @Test
    public void testGetAllTransfers() throws Exception {

        Transfer transfer1 = new Transfer();
        transfer1.setId(1L);
        transfer1.setFromAccount(1L);
        transfer1.setToAccount(2L);
        transfer1.setAmount(BigDecimal.valueOf(1200));
        transfer1.setScheduledDate(LocalDate.now().plusDays(5));
        transfer1.setFee(BigDecimal.valueOf(108.00));

        Transfer transfer2 = new Transfer();
        transfer2.setId(2L);
        transfer2.setFromAccount(2L);
        transfer2.setToAccount(1L);
        transfer2.setAmount(BigDecimal.valueOf(1900));
        transfer2.setScheduledDate(LocalDate.now().plusDays(7));
        transfer2.setFee(BigDecimal.valueOf(171.00));

        List<Transfer> transfers = Arrays.asList(transfer1, transfer2);
        Mockito.when(transferRepository.findAll()).thenReturn(transfers);

        mockMvc.perform(get("/transfers")
                 .contentType(MediaType.APPLICATION_JSON))
        	//.andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk())  
            .andExpect(jsonPath("$.size()").value(2))  
            .andExpect(jsonPath("$[0].id").value(1))  
            .andExpect(jsonPath("$[0].fromAccount").value(1))  
            .andExpect(jsonPath("$[0].toAccount").value(2)) 
            .andExpect(jsonPath("$[0].amount").value(1200))  
            .andExpect(jsonPath("$[0].scheduledDate").value(LocalDate.now().plusDays(5).toString()))
            .andExpect(jsonPath("$[0].fee").value(108.00))  
            .andExpect(jsonPath("$[1].id").value(2)) 
            .andExpect(jsonPath("$[1].fromAccount").value(2))  
            .andExpect(jsonPath("$[1].scheduledDate").value(LocalDate.now().plusDays(7).toString()))
            .andExpect(jsonPath("$[1].toAccount").value(1))  
            .andExpect(jsonPath("$[1].amount").value(1900))  
            .andExpect(jsonPath("$[1].fee").value(171.00)); 
    }
    
    @Test
    public void testGetTransferById() throws Exception {
 
        Transfer transfer = new Transfer();
        transfer.setId(1L);
        transfer.setFromAccount(1L);
        transfer.setToAccount(2L);
        transfer.setAmount(BigDecimal.valueOf(1200));
        transfer.setScheduledDate(LocalDate.now().plusDays(5));
        transfer.setFee(BigDecimal.valueOf(108.00));

        Mockito.when(transferRepository.findById(1L)).thenReturn(Optional.of(transfer));

        mockMvc.perform(get("/transfers/1")
                 .contentType(MediaType.APPLICATION_JSON))
        	//.andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk())  
            .andExpect(jsonPath("$.id").value(1)) 
            .andExpect(jsonPath("$.fromAccount").value(1))  
            .andExpect(jsonPath("$.toAccount").value(2))  
            .andExpect(jsonPath("$.amount").value(1200)) 
            .andExpect(jsonPath("$.scheduledDate").value(LocalDate.now().plusDays(5).toString()))
            .andExpect(jsonPath("$.fee").value(108.00)); 
    }
    
    @Test
    public void testDeleteTransfer() throws Exception {

        Transfer existingTransfer = new Transfer();
        existingTransfer.setId(1L);

        Mockito.when(transferRepository.findById(1L)).thenReturn(Optional.of(existingTransfer));

        mockMvc.perform(delete("/transfers/1"))
               .andExpect(status().isOk())
               .andExpect(content().string(""));
    }
  
}