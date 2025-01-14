package com.example.bankTransfers.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.example.bankTransfers.dto.TransferRequest;
import com.example.bankTransfers.model.Transfer;
import com.example.bankTransfers.repository.TransferRepository;
import com.example.bankTransfers.service.TransferService;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Transactional
public class TransferControllerTest {

	@Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransferService transferService;

    @MockBean
    private TransferRepository transferRepository;

    @Autowired
    private ObjectMapper objectMapper;
    
    
    
    @Test
    public void testCreateTransfer() throws Exception {
    	//TODO
    	TransferRequest request = new TransferRequest();
        request.setFromAccount(1L);
        request.setToAccount(2L);
        request.setAmount(BigDecimal.valueOf(1200));
        request.setScheduledDate(LocalDate.now().plusDays(5));

        mockMvc.perform(post("/api/transfers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.amount").value(1200))
                .andExpect(jsonPath("$.fee").value(108.0)); // 9% fee calculation
    }
    

    
}