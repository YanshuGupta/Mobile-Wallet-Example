package com.capgemini.controller;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.capgemini.bean.Customer;
import com.capgemini.exception.DuplicateIdentityException;
import com.capgemini.exception.IdNotExistException;
import com.capgemini.exception.InsufficientWalletBalanceException;
import com.capgemini.service.WalletServiceImplementation;

@RestController
public class CustomerController {

	@Autowired
	WalletServiceImplementation walletServiceImpl;

	Customer customer;

	@RequestMapping(method = RequestMethod.POST, value = "/addCustomer")
	public Customer createAccount(@RequestBody Customer customer) throws DuplicateIdentityException {

		return walletServiceImpl.createAccount(customer.getMobileNo(),customer.getName(), 
				customer.getWallet().getBalance());

	}

	@RequestMapping(method = RequestMethod.GET, value = "/getBalance/{mobileNo}")
	public Customer showBalance(@PathVariable String mobileNo) throws IdNotExistException {
		return walletServiceImpl.showBalance(mobileNo);

	}

	@RequestMapping(method = RequestMethod.PUT, value = "/depositBalance/{mobileNo}/{amount}")

	public Customer depositBalance(@PathVariable String mobileNo, @PathVariable BigDecimal amount) throws IdNotExistException {
		return walletServiceImpl.depositAmount(mobileNo, amount);

	}

	@RequestMapping(method = RequestMethod.POST, value = "/withdrawBalance/{mobileNo}/{amount}")

	public Customer withdrawBalance(@PathVariable String mobileNo, @PathVariable BigDecimal amount) throws IdNotExistException, InsufficientWalletBalanceException {
		return walletServiceImpl.withdrawAmount(mobileNo, amount);

	}

	@RequestMapping(method = RequestMethod.POST, value = "/fundTransfer/{sourceMobileNo}/{targetMobileNo}/{amount}")

	public Customer[] fundTransfer(@PathVariable String sourceMobileNo, @PathVariable String targetMobileNo,
			@PathVariable BigDecimal amount) throws IdNotExistException, InsufficientWalletBalanceException  {

		return walletServiceImpl.fundTransfer(sourceMobileNo, targetMobileNo, amount);

	}

}
