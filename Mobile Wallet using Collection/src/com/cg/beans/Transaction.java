package com.cg.beans;

import java.math.BigDecimal;

public class Transaction {
	private String tId;
	private String tMobileNumber;
	private BigDecimal tAmount;
	private String info;
	public enum TRANSACTIONTYPE{DEPOSIT, WITHDRAW, FUNDTRANSFER};
	TRANSACTIONTYPE transactionType;
	public String gettId() {
		return tId;
	}
	public void settId(String tId) {
		this.tId = tId;
	}
	public String gettMobileNumber() {
		return tMobileNumber;
	}
	public void settMobileNumber(String tMobileNumber) {
		this.tMobileNumber = tMobileNumber;
	}
	public BigDecimal gettAmount() {
		return tAmount;
	}
	public void settAmount(BigDecimal tAmount) {
		this.tAmount = tAmount;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public TRANSACTIONTYPE getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(TRANSACTIONTYPE transactionType) {
		this.transactionType = transactionType;
	}
	@Override
	public String toString() {
		return "Transaction [tId=" + tId + ", tMobileNumber=" + tMobileNumber + ", tAmount=" + tAmount + ", info="
				+ info + ", transactionType=" + transactionType + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((info == null) ? 0 : info.hashCode());
		result = prime * result + ((tAmount == null) ? 0 : tAmount.hashCode());
		result = prime * result + ((tId == null) ? 0 : tId.hashCode());
		result = prime * result + ((tMobileNumber == null) ? 0 : tMobileNumber.hashCode());
		result = prime * result + ((transactionType == null) ? 0 : transactionType.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Transaction other = (Transaction) obj;
		if (info == null) {
			if (other.info != null)
				return false;
		} else if (!info.equals(other.info))
			return false;
		if (tAmount == null) {
			if (other.tAmount != null)
				return false;
		} else if (!tAmount.equals(other.tAmount))
			return false;
		if (tId == null) {
			if (other.tId != null)
				return false;
		} else if (!tId.equals(other.tId))
			return false;
		if (tMobileNumber == null) {
			if (other.tMobileNumber != null)
				return false;
		} else if (!tMobileNumber.equals(other.tMobileNumber))
			return false;
		if (transactionType != other.transactionType)
			return false;
		return true;
	}
	
	
	
}
