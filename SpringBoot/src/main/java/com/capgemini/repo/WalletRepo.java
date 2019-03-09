package com.capgemini.repo;

import com.capgemini.bean.Customer;

public interface WalletRepo {

	boolean save(Customer customer);

	Customer search(String mobileNumber);

	boolean closeConnection();

	boolean updateAccount(Customer customer);

	boolean updateAccount(Customer sender, Customer receiver);

}