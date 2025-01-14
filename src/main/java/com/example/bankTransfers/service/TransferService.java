package com.example.bankTransfers.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import org.springframework.stereotype.Service;

import com.example.bankTransfers.exception.InvalidSchedulingException;

@Service
public class TransferService {
	
	public BigDecimal calculateFee(BigDecimal amount, LocalDate scheduledDate) {
        LocalDate today = LocalDate.now();
        long daysDifference = ChronoUnit.DAYS.between(today, scheduledDate);

        if (amount.compareTo(new BigDecimal("1000")) <= 0 && daysDifference == 0) {
            return amount.multiply(new BigDecimal("0.03")).add(new BigDecimal("3"));   
        } else if (amount.compareTo(new BigDecimal("2000")) <= 0 && (daysDifference <= 10 && daysDifference > 0)) {
            return amount.multiply(new BigDecimal("0.09"));
        } else if (amount.compareTo(new BigDecimal("2000")) > 0) {
            if (daysDifference >= 11 && daysDifference <= 20) {
                return amount.multiply(new BigDecimal("0.082"));
            } else if (daysDifference >= 21 && daysDifference <= 30) {
                return amount.multiply(new BigDecimal("0.069"));
            } else if (daysDifference >= 31 && daysDifference <= 40) {
                return amount.multiply(new BigDecimal("0.047"));
            } else if (daysDifference > 40) {
                return amount.multiply(new BigDecimal("0.017"));
            }
        }
        throw new InvalidSchedulingException("Invalid scheduling parameters: amount=" 
                + amount + ", scheduledDate=" + scheduledDate);
    }
}
