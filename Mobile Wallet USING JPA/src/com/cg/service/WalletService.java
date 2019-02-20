package com.cg.service;

import java.math.BigDecimal;
import java.util.List;

import com.cg.beans.Customer;
import com.cg.exception.DuplicateIdentityException;
import com.cg.exception.IdNotExistException;
import com.cg.exception.InsufficientWalletBalanceException;

public interface WalletService {

	Customer createAccount(String mobileNumber, String name, BigDecimal initialBalance)
			throws DuplicateIdentityException;

	Customer showBalance(String mobileNumber) throws IdNotExistException;

	Customer depositAmount(String mobileNumber, BigDecimal depositAmountValue) throws IdNotExistException;

	Customer withdrawAmount(String mobileNumber, BigDecimal withdrawAmountValue)
			throws IdNotExistException, InsufficientWalletBalanceException;

	Customer[] fundTransfer(String senderAccount, String receiverAccount, BigDecimal amount)
			throws IdNotExistException, InsufficientWalletBalanceException;

	boolean isExist(String mobileNumber);

	boolean closeConnection();

}