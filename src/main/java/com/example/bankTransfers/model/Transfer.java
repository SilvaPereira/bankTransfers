package com.example.bankTransfers.model;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "TRANSFERS")
public class Transfer {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column(nullable = false)
    private Long fromAccount;

    @Column(nullable = false)
    private Long toAccount;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate scheduledDate;

    @Column(nullable = false)
    private BigDecimal fee;
	
    public Transfer() {
    	
    }
    
	public Transfer(Long id, Long fromAccount, Long toAccount, BigDecimal amount, LocalDate scheduledDate,
			BigDecimal fee) {
		this.id = id;
		this.fromAccount = fromAccount;
		this.toAccount = toAccount;
		this.amount = amount;
		this.scheduledDate = scheduledDate;
		this.fee = fee;
	}
	
	public Transfer(Long fromAccount, Long toAccount, BigDecimal amount, LocalDate scheduledDate,
			BigDecimal fee) {
		this.fromAccount = fromAccount;
		this.toAccount = toAccount;
		this.amount = amount;
		this.scheduledDate = scheduledDate;
		this.fee = fee;
	}

	public Long getId() {
		return id;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setFromAccount(Long fromAccount) {
		this.fromAccount = fromAccount;
	}

	public void setToAccount(Long toAccount) {
		this.toAccount = toAccount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public void setScheduledDate(LocalDate scheduledDate) {
		this.scheduledDate = scheduledDate;
	}

	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}

	public Long getFromAccount() {
		return fromAccount;
	}

	public Long getToAccount() {
		return toAccount;
	}

	public LocalDate getScheduledDate() {
		return scheduledDate;
	}

	public BigDecimal getFee() {
		return fee;
	}
	
}

