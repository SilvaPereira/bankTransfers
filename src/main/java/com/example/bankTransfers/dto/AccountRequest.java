package com.example.bankTransfers.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class AccountRequest {
	
	private BigDecimal balance;
	private String firstName;
	private String lastName;
	private LocalDate creationDate;
	private Boolean active;
	
	public AccountRequest() {}
	
	public AccountRequest(BigDecimal balance, String firstName, String lastName, LocalDate creationDate,
			Boolean active) {
		super();
		this.balance = balance;
		this.firstName = firstName;
		this.lastName = lastName;
		this.creationDate = creationDate;
		this.active = active;
	}
	public AccountRequest(BigDecimal balance, String firstName, String lastName) {
		super();
		this.balance = balance;
		this.firstName = firstName;
		this.lastName = lastName;
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
	public Boolean isActive() {
	    if (active == null) {
	        return false;
	    }
	    return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
	}
	public Boolean getIsActive() {
        return active;
    }
    public void setIsActive(Boolean isActive) {
        this.active = isActive;
    }
}
