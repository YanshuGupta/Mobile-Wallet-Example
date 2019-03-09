package com.capgemini.service;

import java.math.BigDecimal;

import com.capgemini.bean.Customer;
import com.capgemini.exception.DuplicateIdentityException;
import com.capgemini.exception.IdNotExistException;
import com.capgemini.exception.InsufficientWalletBalanceException;

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