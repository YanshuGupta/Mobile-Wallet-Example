package com.capgemini.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capgemini.bean.Customer;
import com.capgemini.bean.Wallet;
import com.capgemini.exception.DuplicateIdentityException;
import com.capgemini.exception.IdNotExistException;
import com.capgemini.exception.InsufficientWalletBalanceException;
import com.capgemini.exception.ReceiverIdNotExistException;
import com.capgemini.exception.SenderIdNotExistException;
import com.capgemini.repo.WalletRepoImplementation;

@Service
public class WalletServiceImplementation implements WalletService {
	
	@Autowired
	private WalletRepoImplementation walletRepo;
	
	public WalletServiceImplementation(WalletRepoImplementation walletRepo) {
		
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
		customer.setMobileNo(mobileNumber);
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



