package com.cg.repo;

import com.cg.beans.Customer;

public interface WalletRepo {

	boolean save(Customer customer);

	Customer search(String mobileNumber);

	boolean closeConnection();

	boolean updateAccount(Customer customer);

	boolean updateAccount(Customer sender, Customer receiver);

}