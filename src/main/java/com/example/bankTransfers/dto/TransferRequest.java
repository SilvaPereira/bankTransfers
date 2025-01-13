package com.example.bankTransfers.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TransferRequest {

	private Long fromAccount;
    private Long toAccount;
    private BigDecimal amount;
    private LocalDate scheduledDate;
    
    public TransferRequest() {}
    
	public TransferRequest(Long fromAccount, Long toAccount, BigDecimal amount, LocalDate scheduledDate) {
		super();
		this.fromAccount = fromAccount;
		this.toAccount = toAccount;
		this.amount = amount;
		this.scheduledDate = scheduledDate;
	}
	
	public Long getFromAccount() {
		return fromAccount;
	}
	public void setFromAccount(Long fromAccount) {
		this.fromAccount = fromAccount;
	}
	public Long getToAccount() {
		return toAccount;
	}
	public void setToAccount(Long toAccount) {
		this.toAccount = toAccount;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public LocalDate getScheduledDate() {
		return scheduledDate;
	}
	public void setScheduledDate(LocalDate scheduledDate) {
		this.scheduledDate = scheduledDate;
	}
 
}
