package com.example.bankTransfers.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class AccountRequest {
	
	private BigDecimal balance;
	private String firstName;
	private String lastName;
	private LocalDate creationDate;
	private boolean active;
	
	public AccountRequest() {}
	
	public AccountRequest(BigDecimal balance, String firstName, String lastName, LocalDate creationDate,
			boolean active) {
		super();
		this.balance = balance;
		this.firstName = firstName;
		this.lastName = lastName;
		this.creationDate = creationDate;
		this.active = active;
	}
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public LocalDate getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(LocalDate creationDate) {
		this.creationDate = creationDate;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	
	
}
