package com.example.bankTransfers.service;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import com.example.bankTransfers.exception.InvalidSchedulingException;

public class TransferServiceTest {

	private final TransferService transferService = new TransferService();
	
	@Test
    void testCalculateFeeAmountLessThan1000AndSameDay() {
        BigDecimal amount = new BigDecimal("500");
        LocalDate scheduledDate = LocalDate.now();
        BigDecimal fee = transferService.calculateFee(amount, scheduledDate);
        BigDecimal expectedFee = amount.multiply(new BigDecimal("0.03")).add(new BigDecimal("3"));
        assertEquals(expectedFee, fee);
    }

    @Test
    void testCalculateFeeAmountBetween1000And2000AndLessThan10Days() {
        BigDecimal amount = new BigDecimal("1500");
        LocalDate scheduledDate = LocalDate.now().plusDays(7);
        BigDecimal fee = transferService.calculateFee(amount, scheduledDate);
        BigDecimal expectedFee = amount.multiply(new BigDecimal("0.09"));
        assertEquals(expectedFee, fee);
    }
    
    @Test
    void testCalculateFeeAmountGreaterThan2000And11To20Days() {
        BigDecimal amount = new BigDecimal("2500");
        LocalDate scheduledDate = LocalDate.now().plusDays(15);
        BigDecimal fee = transferService.calculateFee(amount, scheduledDate);
        BigDecimal expectedFee = amount.multiply(new BigDecimal("0.082"));
        assertEquals(expectedFee, fee);
    }

    @Test
    void testCalculateFeeAmountGreaterThan2000And21To30Days() {
        BigDecimal amount = new BigDecimal("3000");
        LocalDate scheduledDate = LocalDate.now().plusDays(25);
        BigDecimal fee = transferService.calculateFee(amount, scheduledDate);
        BigDecimal expectedFee = amount.multiply(new BigDecimal("0.069"));
        assertEquals(expectedFee, fee);
    }

    @Test
    void testCalculateFeeAmountGreaterThan2000And31To40Days() {
        BigDecimal amount = new BigDecimal("3500");
        LocalDate scheduledDate = LocalDate.now().plusDays(35);
        BigDecimal fee = transferService.calculateFee(amount, scheduledDate);
        BigDecimal expectedFee = amount.multiply(new BigDecimal("0.047"));
        assertEquals(expectedFee, fee);
    }

    @Test
    void testCalculateFeeAmountGreaterThan2000AndMoreThan40Days() {
        BigDecimal amount = new BigDecimal("4000");
        LocalDate scheduledDate = LocalDate.now().plusDays(50);
        BigDecimal fee = transferService.calculateFee(amount, scheduledDate);
        BigDecimal expectedFee = amount.multiply(new BigDecimal("0.017"));
        assertEquals(expectedFee, fee);
    }

    @Test
    void testCalculateFee_InvalidParameters() {
        BigDecimal amount = new BigDecimal("500");
        LocalDate scheduledDate = LocalDate.now().minusDays(1);
        Exception exception = assertThrows(RuntimeException.class, () -> {
            transferService.calculateFee(amount, scheduledDate);
        });
        assertEquals("Invalid scheduling parameters: amount=" + amount + ", scheduledDate=" + scheduledDate, exception.getMessage());
    }
}

