package com.cg.testing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Test;

import com.cg.beans.Customer;
import com.cg.beans.Transaction;
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

	WalletService service = new WalletServiceImplementation(new WalletRepoImplementation());
	
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
		customer1.setMobileNumber("9999999999");
		customer1.setName("YANSHU GUPTA");
		customer1.setWallet(wallet);
	
		Customer customer = service.createAccount("9999999999", "YANSHU GUPTA", new BigDecimal("123.12"));
		customer1.setTransaction(customer.getTransaction());
		
		assertEquals(customer1, customer);
	}
	
	@Test(expected = DuplicateIdentityException.class)
	public void whenCustomerPassesPhoneNumberToCreateCustomerAccountButPhoneNumberAlreadyExist() throws DuplicateIdentityException {
		service.createAccount("9999999999", "YANSHU GUPTA", new BigDecimal("123.12"));
		service.createAccount("9999999999", "NILESH", new BigDecimal("123.12"));
	}
	
	/*
	 * 	Test Cases for show Balance
	 * 1. When the valid information is passed
	 * 2. when account is not present with that Id
	 */
	@Test
	public void whenCustomerWantsToSeeHisBalanceWithValidInformation() throws DuplicateIdentityException, IdNotExistException {
		
		assertEquals(service.createAccount("9999999999", "YANSHU GUPTA", new BigDecimal("123.12")), service.showBalance("9999999999"));
	}

	@Test(expected = IdNotExistException.class)
	public void whenCustomerWantsToSeeHisBalanceWhenIdIsNotExist() throws IdNotExistException, DuplicateIdentityException {
		
		assertEquals(service.createAccount("9999999999", "YANSHU GUPTA", new BigDecimal("123.12")), service.showBalance("9999999998"));
	}
	
	
	/*
	 * 	Test Cases for deposit Balance
	 * 1. when Valid Information is passed it should deposit balance
	 * 2. when Id is not Exist and try to deposit balance, it will throw IdNotExistException
	 */
	@Test
	public void whenCustomerWantsToDepositHisBalanceWithValidInformation() throws DuplicateIdentityException, IdNotExistException {
		
		assertEquals(service.createAccount("9999999999", "YANSHU GUPTA", new BigDecimal("123.12")),
				service.depositAmount("9999999999", new BigDecimal(100)));
	}

	@Test(expected = IdNotExistException.class)
	public void whenCustomerWantsToDepositHisBalanceWithInValidInformation() throws IdNotExistException, DuplicateIdentityException {
		
		assertEquals(service.createAccount("9999999999", "YANSHU GUPTA", new BigDecimal("123.12")),
				service.depositAmount("9999999998", new BigDecimal(100)));	
	}
	
	/*
	 * 	Test Cases for withdraw Balance
	 * 1. When Valid Information is passed it should withdraw balance
	 * 2. When Id is not Exist and try to withdraw balance, it will throw IdNotExistException
	 * 3. When Customer Account have Lower Balance then withdraw Amount it will throw InsufficientWalletBalanceException
	 */
	@Test
	public void whenCustomerWantsToWithdrawHisBalanceWithValidInformation() throws DuplicateIdentityException, IdNotExistException, InsufficientWalletBalanceException {
		
		assertEquals(service.createAccount("9999999999", "YANSHU GUPTA", new BigDecimal("123.12")),
				service.withdrawAmount("9999999999", new BigDecimal(100)));
	}

	@Test(expected = IdNotExistException.class)
	public void whenCustomerWantsToWithdrawHisBalanceWithInValidInformation() throws IdNotExistException, DuplicateIdentityException, InsufficientWalletBalanceException {
		
		assertEquals(service.createAccount("9999999999", "YANSHU GUPTA", new BigDecimal("123.12")),
				service.withdrawAmount("9999999998", new BigDecimal(100)));
	}
	
	@Test(expected = InsufficientWalletBalanceException.class)
	public void whenCustomerWantsToWithdrawHisBalanceWhenBalanceIsLessThanWithdrawBalance() throws IdNotExistException, DuplicateIdentityException, InsufficientWalletBalanceException {
		
		assertEquals(service.createAccount("9999999999", "YANSHU GUPTA", new BigDecimal("123.12")),
				service.withdrawAmount("9999999999", new BigDecimal(1001)));
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
		
		Customer sender = service.createAccount("9999999999", "YANSHU GUPTA", new BigDecimal("123.12"));
		Customer receiver = service.createAccount("8888888888", "NILESH GUPTA", new BigDecimal("50000"));
		
		Customer customer[] = service.fundTransfer("8888888888", "9999999999", new BigDecimal("10000"));
		
		assertEquals(receiver, customer[0]);
		assertEquals(sender, customer[1]);
	}
	
	@Test(expected = SenderIdNotExistException.class)
	public void whenCustomerWantsToTransferfundWithSenderInValidDetails() throws DuplicateIdentityException, IdNotExistException, InsufficientWalletBalanceException {
		
		Customer sender = service.createAccount("9999999999", "YANSHU GUPTA", new BigDecimal("123.12"));
		Customer receiver = service.createAccount("8888888888", "NILESH GUPTA", new BigDecimal("50000"));
		
		Customer customer[] = service.fundTransfer("8888888889", "9999999999", new BigDecimal("10000"));
		
	}
	
	@Test(expected = ReceiverIdNotExistException.class)
	public void whenCustomerWantsToTransferfundWithReceiverInValidDetails() throws DuplicateIdentityException, IdNotExistException, InsufficientWalletBalanceException {
		
		Customer sender = service.createAccount("9999999999", "YANSHU GUPTA", new BigDecimal("123.12"));
		Customer receiver = service.createAccount("8888888888", "NILESH GUPTA", new BigDecimal("50000"));
		
		Customer customer[] = service.fundTransfer("8888888888", "9999999998", new BigDecimal("10000"));
		
	}
	
	@Test(expected = IdNotExistException.class)
	public void whenCustomerWantsToTransferfundWithInValidDetails() throws DuplicateIdentityException, IdNotExistException, InsufficientWalletBalanceException {
		
		Customer sender = service.createAccount("9999999999", "YANSHU GUPTA", new BigDecimal("123.12"));
		Customer receiver = service.createAccount("8888888888", "NILESH GUPTA", new BigDecimal("50000"));
		
		Customer customer[] = service.fundTransfer("888888855588", "999977799998", new BigDecimal("10000"));
	}
	
	/*
	 * Transaction Test Case
	 */
	
	@Test
	public void whenTransactionIsSavedSuccessfully() throws DuplicateIdentityException {
		
		Customer sender = service.createAccount("9999999999", "YANSHU GUPTA", new BigDecimal("123.12"));
		List<Transaction> service = sender.getTransaction();
		
		if(service == null) {
			fail("Transaction is not saved");
		}
	}
	
	/*
	 * 	Test case for Last 10 Transaction
	 * 1. when Valid Information is Passed Transaction retrieved Successfully
	 * 2. when id is passed which is not Exist it will throw IdNotExistException
	 */
	
	@Test
	public void whenTransactionRetrivedSuccessfully() throws DuplicateIdentityException, IdNotExistException {
		
		service.createAccount("9999999999", "YANSHU GUPTA", new BigDecimal("123.12"));
		if(service.getlast10Transaction("9999999999").size()!=1) {
			fail("Transaction is not saved");
		};
	}
	

	@Test(expected = IdNotExistException.class)
	public void whenInvalidInformationIsPassedForTransactionRetrival() throws DuplicateIdentityException, IdNotExistException {
		
		service.createAccount("9999999999", "YANSHU GUPTA", new BigDecimal("123.12"));
		service.getlast10Transaction("8888888888").size();
	}
}




