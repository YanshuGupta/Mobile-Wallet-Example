package com.cg.beans;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Transaction")
public class Transaction {
	
	@Id
	@Column(name = "Transaction_Id", nullable=false)
	private long transactionId;
	
	@Column(name = "Credit_Amount")
	private BigDecimal creditAmount;
	
	@Column(name = "Debit_Amount")
	private BigDecimal debitAmount;
	
	@Column(name = "Total_Amount")
	private BigDecimal totalAmount;
	
	@Column(name = "Transaction_DATE")
	private LocalDate transactionDate;

	
	public Transaction(long transactionId, BigDecimal creditAmount, BigDecimal debitAmount, BigDecimal totalAmount, LocalDate transactionDate) {
		super();
		this.transactionId = transactionId;
		this.creditAmount = creditAmount;
		this.debitAmount = debitAmount;
		this.totalAmount = totalAmount;
		this.transactionDate = transactionDate;
	}

	public Transaction() {
		super();
		// TODO Auto-generated constructor stub
	}

	public long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(long transactionId) {
		this.transactionId = transactionId;
	}

	public BigDecimal getCreditAmount() {
		return creditAmount;
	}

	public void setCreditAmount(BigDecimal creditAmount) {
		this.creditAmount = creditAmount;
	}

	public BigDecimal getDebitAmount() {
		return debitAmount;
	}

	public void setDebitAmount(BigDecimal debitAmount) {
		this.debitAmount = debitAmount;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public LocalDate getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(LocalDate transactionDate) {
		this.transactionDate = transactionDate;
	}

	@Override
	public String toString() {
		return "Transaction [transactionId=" + transactionId + ", creditAmount=" + creditAmount + ", debitAmount="
				+ debitAmount + ", totalAmount=" + totalAmount + ", transactionDate=" + transactionDate + "]";
	}
	
	
	
}
