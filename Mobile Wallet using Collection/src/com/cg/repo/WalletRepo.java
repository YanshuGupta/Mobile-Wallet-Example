package com.cg.repo;

import java.util.List;

import com.cg.beans.Customer;
import com.cg.beans.Transaction;

public interface WalletRepo {

	boolean save(Customer customer);

	Customer search(String mobileNumber);

	boolean saveTransactionDetails(String MobileNumber, Transaction transaction);

	List<Transaction> getLast10Transaction(String mobileNumber);

}