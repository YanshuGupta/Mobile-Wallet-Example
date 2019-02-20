package com.cg.service;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

import com.cg.beans.Customer;
import com.cg.beans.Wallet;
import com.cg.exception.DuplicateIdentityException;
import com.cg.exception.IdNotExistException;
import com.cg.exception.InsufficientWalletBalanceException;
import com.cg.exception.ReceiverIdNotExistException;
import com.cg.exception.SenderIdNotExistException;
import com.cg.repo.WalletRepo;

public class WalletServiceImplementation implements WalletService {
	
	private WalletRepo walletRepo;
	
	public WalletServiceImplementation(WalletRepo walletRepo) {
		
		super();
		this.walletRepo = walletRepo;
	}

	
	/* (non-Javadoc)
	 * @see com.cg.service.WalletService#createAccount(java.lang.String, java.lang.String, java.math.BigDecimal)
	 */
	@Override
	public Customer createAccount(String mobileNumber, String name, BigDecimal initialBalance) throws DuplicateIdentityException {
		
		Wallet wallet = new Wallet();
		wallet.setBalance(initialBalance);
		
		Customer customer = new Customer();
		customer.setMobileNumber(mobileNumber);
		customer.setName(name);
		customer.setWallet(wallet);
		
		if(walletRepo.save(customer)) {
			return walletRepo.search(mobileNumber);
		}
		else {
			throw new DuplicateIdentityException();
		}
	}
	
	
	
	/* (non-Javadoc)
	 * @see com.cg.service.WalletService#showBalance(java.lang.String)
	 */
	@Override
	public Customer showBalance(String mobileNumber) throws IdNotExistException {
		
		Customer customer = walletRepo.search(mobileNumber);
		if(customer == null) {
			
			throw new IdNotExistException();
		}
		else {
			
			return customer;
		}
		
	}
	
	
	
	/* (non-Javadoc)
	 * @see com.cg.service.WalletService#depositAmount(java.lang.String, java.math.BigDecimal)
	 */
	@Override
	public Customer depositAmount(String mobileNumber, BigDecimal depositAmountValue) throws IdNotExistException {
		
		Customer customer = walletRepo.search(mobileNumber);
		
		if(customer != null) {
			
			customer.getWallet().setBalance(customer.getWallet().getBalance().add(depositAmountValue));
			
			walletRepo.updateAccount(customer);
			
			return walletRepo.search(mobileNumber);
		}
		else {
			
			throw new IdNotExistException();
		}
	}
	
	
	
	/* (non-Javadoc)
	 * @see com.cg.service.WalletService#withdrawAmount(java.lang.String, java.math.BigDecimal)
	 */
	@Override
	public Customer withdrawAmount(String mobileNumber, BigDecimal withdrawAmountValue) throws IdNotExistException, InsufficientWalletBalanceException {
		
		Customer customer = walletRepo.search(mobileNumber);
		if(customer == null) {
			throw new IdNotExistException();
		}
		
		if(customer != null && customer.getWallet().getBalance().compareTo(withdrawAmountValue) >= 0) {
			
			customer.getWallet().setBalance(customer.getWallet().getBalance().subtract(withdrawAmountValue));
			walletRepo.updateAccount(customer);
			return walletRepo.search(mobileNumber);
		}
		else {
			
			throw new InsufficientWalletBalanceException();
		}
		
	}
	
	
	
	/* (non-Javadoc)
	 * @see com.cg.service.WalletService#fundTransfer(java.lang.String, java.lang.String, java.math.BigDecimal)
	 */
	@Override
	public Customer[] fundTransfer(String senderAccount, String receiverAccount, BigDecimal amount) throws IdNotExistException, InsufficientWalletBalanceException {
		
		Customer sender = walletRepo.search(senderAccount);
		Customer receiver = walletRepo.search(receiverAccount);
		
		if(sender == null) {
			throw new SenderIdNotExistException();
		}
		
		if(receiver == null) {
			throw new ReceiverIdNotExistException();
		}
		
		else if(sender.getWallet().getBalance().compareTo(amount) >= 0) {
			
			sender.getWallet().setBalance(sender.getWallet().getBalance().subtract(amount));
			
			receiver.getWallet().setBalance(receiver.getWallet().getBalance().add(amount));
			
			walletRepo.updateAccount(sender, receiver);
			return new Customer[] {walletRepo.search(senderAccount), walletRepo.search(receiverAccount)};
		}
		
		else {
			
			throw new InsufficientWalletBalanceException();
		}
	}
	
	
	/* (non-Javadoc)
	 * @see com.cg.service.WalletService#isExist(java.lang.String)
	 */
	@Override
	public boolean isExist(String mobileNumber) {
		
		Customer customer = walletRepo.search(mobileNumber);
		
		if(customer == null) {
			return false;
		}
		else {
			return true;
		}
	}


	@Override
	public boolean closeConnection() {
		walletRepo.closeConnection();
		return true;
	}

	
}



