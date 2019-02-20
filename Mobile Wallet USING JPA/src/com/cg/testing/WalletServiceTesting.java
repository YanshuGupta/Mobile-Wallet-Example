package com.cg.testing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.cg.beans.Customer;
import com.cg.beans.Wallet;
import com.cg.exception.DuplicateIdentityException;
import com.cg.exception.IdNotExistException;
import com.cg.exception.InsufficientWalletBalanceException;
import com.cg.exception.ReceiverIdNotExistException;
import com.cg.exception.SenderIdNotExistException;
import com.cg.repo.WalletRepoImplementation;
import com.cg.service.WalletService;
import com.cg.service.WalletServiceImplementation;

public class WalletServiceTesting {

	WalletService service;

	@Before
	public void setup() {
		service = new WalletServiceImplementation(new WalletRepoImplementation());
		
		
	}
	
	
	/*
	 *   Test Cases for Creating Customer Account
	 * 1. When Valid Information Passed Customer Account Will be Created
	 * 2. when an Account is Already exist it will throw DuplicateIdentityException
	 */
	
	@Test
	public void whenCustomerObjectCreatedSuccessfully() throws DuplicateIdentityException {
		
		Wallet wallet = new Wallet();
		wallet.setBalance(new BigDecimal("123.12"));
		
		Customer customer1 = new Customer();
		customer1.setMobileNumber("9999999980");
		customer1.setName("YANSHU GUPTA");
		customer1.setWallet(wallet);
	
		Customer customer = service.createAccount("9999999980", "YANSHU GUPTA", new BigDecimal("123.12"));
		
		assertEquals(customer1, customer);
	}
	
	@Test(expected = DuplicateIdentityException.class)
	public void whenCustomerPassesPhoneNumberToCreateCustomerAccountButPhoneNumberAlreadyExist() throws DuplicateIdentityException {
		service.createAccount("9999999981", "YANSHU GUPTA", new BigDecimal("123.12"));
		service.createAccount("9999999981", "NILESH", new BigDecimal("123.12"));
	}
	
	/*
	 * 	Test Cases for show Balance
	 * 1. When the valid information is passed
	 * 2. when account is not present with that Id
	 */
	@Test
	public void whenCustomerWantsToSeeHisBalanceWithValidInformation() throws DuplicateIdentityException, IdNotExistException {
		
		assertEquals(service.createAccount("9999999982", "YANSHU GUPTA", new BigDecimal("123.12")), service.showBalance("9999999982"));
	}

	@Test(expected = IdNotExistException.class)
	public void whenCustomerWantsToSeeHisBalanceWhenIdIsNotExist() throws IdNotExistException, DuplicateIdentityException {
		
		assertEquals(service.createAccount("9999999983", "YANSHU GUPTA", new BigDecimal("123.12")), service.showBalance("9999999984"));
	}
	
	
	/*
	 * 	Test Cases for deposit Balance
	 * 1. when Valid Information is passed it should deposit balance
	 * 2. when Id is not Exist and try to deposit balance, it will throw IdNotExistException
	 */
	@Test
	public void whenCustomerWantsToDepositHisBalanceWithValidInformation() throws DuplicateIdentityException, IdNotExistException {
		service.createAccount("9999999984", "YANSHU GUPTA", new BigDecimal("123.12"));
		
		Wallet wallet = new Wallet();
		wallet.setBalance(new BigDecimal("223.12"));
		
		Customer customer1 = new Customer();
		customer1.setMobileNumber("9999999984");
		customer1.setName("YANSHU GUPTA");
		customer1.setWallet(wallet);
		
		assertEquals(customer1, service.depositAmount("9999999984", new BigDecimal(100)));
	}

	@Test(expected = IdNotExistException.class)
	public void whenCustomerWantsToDepositHisBalanceWithInValidInformation() throws IdNotExistException, DuplicateIdentityException {
		
		assertEquals(service.createAccount("9999999985", "YANSHU GUPTA", new BigDecimal("123.12")),
				service.depositAmount("8888888888", new BigDecimal(100)));	
	}
	
	/*
	 * 	Test Cases for withdraw Balance
	 * 1. When Valid Information is passed it should withdraw balance
	 * 2. When Id is not Exist and try to withdraw balance, it will throw IdNotExistException
	 * 3. When Customer Account have Lower Balance then withdraw Amount it will throw InsufficientWalletBalanceException
	 */
	@Test
	public void whenCustomerWantsToWithdrawHisBalanceWithValidInformation() throws DuplicateIdentityException, IdNotExistException, InsufficientWalletBalanceException {
		service.createAccount("9999999986", "YANSHU GUPTA", new BigDecimal("123.12"));
		

		Wallet wallet = new Wallet();
		wallet.setBalance(new BigDecimal("23.12"));
		
		Customer customer1 = new Customer();
		customer1.setMobileNumber("9999999986");
		customer1.setName("YANSHU GUPTA");
		customer1.setWallet(wallet);
		
		assertEquals(customer1, service.withdrawAmount("9999999986", new BigDecimal(100)));
	}

	@Test(expected = IdNotExistException.class)
	public void whenCustomerWantsToWithdrawHisBalanceWithInValidInformation() throws IdNotExistException, DuplicateIdentityException, InsufficientWalletBalanceException {
		
		assertEquals(service.createAccount("9999999987", "YANSHU GUPTA", new BigDecimal("123.12")),
				service.withdrawAmount("9999999988", new BigDecimal(100)));
	}
	
	@Test(expected = InsufficientWalletBalanceException.class)
	public void whenCustomerWantsToWithdrawHisBalanceWhenBalanceIsLessThanWithdrawBalance() throws IdNotExistException, DuplicateIdentityException, InsufficientWalletBalanceException {
		
		assertEquals(service.createAccount("9999999989", "YANSHU GUPTA", new BigDecimal("123.12")),
				service.withdrawAmount("9999999989", new BigDecimal(1001)));
	}
	
	/*
	 * 	Test Cases for Fund Transfer
	 * 1. When Valid Information is Passed It will Transfer Fund Successfully
	 * 2. when Sender Invalid Id passed for fund Transfer it will throw SenderIdNotExistException
	 * 3. when receiver Invalid Id passed for fund Transfer it will throw ReceiverIdNotExistException
	 * 4. when either of the Invalid Id is passed it will throw IdNotExistException which is parent class of both SenderIdNotExistException, ReceiverIdNotExistException
	 */
	
	@Test
	public void whenCustomerWantsToTransferfundWithValidDetails() throws DuplicateIdentityException, IdNotExistException, InsufficientWalletBalanceException {
		
		Customer sender = service.createAccount("9999999990", "YANSHU GUPTA", new BigDecimal("123.12"));
		Customer receiver = service.createAccount("9999999991", "NILESH GUPTA", new BigDecimal("50000"));
		
		Wallet wallet = new Wallet();
		wallet.setBalance(new BigDecimal("10123.12"));
		
		Customer customer1 = new Customer();
		customer1.setMobileNumber("9999999990");
		customer1.setName("YANSHU GUPTA");
		customer1.setWallet(wallet);
		
		Wallet wallet2 = new Wallet();
		wallet2.setBalance(new BigDecimal("40000"));
		
		Customer customer2 = new Customer();
		customer2.setMobileNumber("9999999991");
		customer2.setName("NILESH GUPTA");
		customer2.setWallet(wallet2);
		
		Customer customer[] = service.fundTransfer("9999999991", "9999999990", new BigDecimal("10000"));
		
		assertEquals(customer1, customer[1]);
		assertEquals(customer2, customer[0]);
	}
	
	@Test(expected = SenderIdNotExistException.class)
	public void whenCustomerWantsToTransferfundWithSenderInValidDetails() throws DuplicateIdentityException, IdNotExistException, InsufficientWalletBalanceException {
		
		Customer sender = service.createAccount("9999999993", "YANSHU GUPTA", new BigDecimal("123.12"));
		Customer receiver = service.createAccount("9999999994", "NILESH GUPTA", new BigDecimal("50000"));
		
		Customer customer[] = service.fundTransfer("8888888889", "9999999994", new BigDecimal("10000"));
		
	}
	
	@Test(expected = ReceiverIdNotExistException.class)
	public void whenCustomerWantsToTransferfundWithReceiverInValidDetails() throws DuplicateIdentityException, IdNotExistException, InsufficientWalletBalanceException {
		
		Customer sender = service.createAccount("9999999995", "YANSHU GUPTA", new BigDecimal("123.12"));
		Customer receiver = service.createAccount("9999999996", "NILESH GUPTA", new BigDecimal("50000"));
		
		Customer customer[] = service.fundTransfer("9999999995", "8888888889", new BigDecimal("10000"));
		
	}
	
	@Test(expected = IdNotExistException.class)
	public void whenCustomerWantsToTransferfundWithInValidDetails() throws DuplicateIdentityException, IdNotExistException, InsufficientWalletBalanceException {
		
		Customer sender = service.createAccount("9999999997", "YANSHU GUPTA", new BigDecimal("123.12"));
		Customer receiver = service.createAccount("9999999998", "NILESH GUPTA", new BigDecimal("50000"));
		
		Customer customer[] = service.fundTransfer("8888888555", "9999777999", new BigDecimal("10000"));
	}
	
	/*
	 * Transaction Test Case
	 */
	
	@Test
	public void whenTransactionIsSavedSuccessfully() throws DuplicateIdentityException {
		
		Customer sender = service.createAccount("9999999999", "YANSHU GUPTA", new BigDecimal("123.12"));
		
		if(service == null) {
			fail("Transaction is not saved");
		}
	}

}




