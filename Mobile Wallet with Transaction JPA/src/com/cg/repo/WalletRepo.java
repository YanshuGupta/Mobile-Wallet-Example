package com.cg.repo;

import java.util.List;

import com.cg.beans.Customer;
import com.cg.beans.Transaction;

public interface WalletRepo {

	boolean save(Customer customer);

	Customer search(String mobileNumber);

	boolean closeEntityManager();

	boolean updateAccount(Customer customer);

	boolean updateAccount(Customer sender, Customer receiver);

	List<Transaction> getTransaction(String mobileNumber);

}