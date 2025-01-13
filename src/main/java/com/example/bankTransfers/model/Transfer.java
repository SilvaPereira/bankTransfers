package com.example.bankTransfers.model;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "transfers")
public class Transfer {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column(nullable = false)
    private String fromAccount;

    @Column(nullable = false)
    private String toAccount;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private LocalDate scheduledDate;

    @Column(nullable = false)
    private BigDecimal fee;
	
	public Transfer(Long id, String fromAccount, String toAccount, BigDecimal amount, LocalDate scheduledDate,
			BigDecimal fee) {
		super();
		this.id = id;
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

	public String getFromAccount() {
		return fromAccount;
	}

	public String getToAccount() {
		return toAccount;
	}

	public LocalDate getScheduledDate() {
		return scheduledDate;
	}

	public BigDecimal getFee() {
		return fee;
	}
	
}

