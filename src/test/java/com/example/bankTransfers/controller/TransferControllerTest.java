package com.example.bankTransfers.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.bankTransfers.dto.TransferRequest;
import com.example.bankTransfers.model.Transfer;
import com.example.bankTransfers.repository.TransferRepository;
import com.example.bankTransfers.service.TransferService;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransferController.class)
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
        TransferRequest request = new TransferRequest(1L, 2L, new BigDecimal("1000"), LocalDate.now().plusDays(5));
        Transfer transfer = new Transfer(1L, 2L, new BigDecimal("1000"), LocalDate.now().plusDays(5), new BigDecimal("30"));
        Mockito.when(transferService.calculateFee(any(BigDecimal.class), any(LocalDate.class))).thenReturn(new BigDecimal("30"));
        Mockito.when(transferRepository.save(any(Transfer.class))).thenReturn(transfer);

        mockMvc.perform(post("/transfers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fromAccount").value(1L))
                .andExpect(jsonPath("$.toAccount").value(2L))
                .andExpect(jsonPath("$.amount").value("1000"))
                .andExpect(jsonPath("$.fee").value("30"));
    }

    
}