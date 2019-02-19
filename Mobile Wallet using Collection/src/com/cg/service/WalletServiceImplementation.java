package com.cg.service;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

import com.cg.beans.Customer;
import com.cg.beans.Transaction;
import com.cg.beans.Transaction.TRANSACTIONTYPE;
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
			
			long id = (long)(Math.random()*100000000);
			String tId = Long.toString(id);
			transactionDetails(tId, mobileNumber, mobileNumber, initialBalance, null, null);
			return customer;
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
			long id = (long)(Math.random()*100000000);
			String tId = Long.toString(id);
			transactionDetails(tId, mobileNumber, mobileNumber, depositAmountValue, null, null);
			return customer;
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
			long id = (long)(Math.random()*100000000);
			String tId = Long.toString(id);
			transactionDetails(tId, mobileNumber, mobileNumber, null, withdrawAmountValue, null);
			return customer;
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
			
			long id = (long)(Math.random()*100000000);
			String tId = Long.toString(id);
			transactionDetails(tId, senderAccount, receiverAccount, null, null, amount);
			
			return new Customer[] {sender, receiver};
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
	

	/* (non-Javadoc)
	 * @see com.cg.service.WalletService#transactionDetails(java.lang.String, java.lang.String, java.lang.String, java.math.BigDecimal, java.math.BigDecimal, java.math.BigDecimal)
	 */
	@Override
	public boolean transactionDetails(String tId, String senderMobileNumber, String receiverMobileNumber, BigDecimal deposit, BigDecimal withdraw, BigDecimal fundtransfer) {
		
		String info="";
		String senderInfo = "";
		String receiverinfo = "";
		if(fundtransfer != null) {
			
			senderInfo = fundtransfer +" is Credited";
			Transaction senderTransaction = new Transaction();
			senderTransaction.settId(tId);
			senderTransaction.settMobileNumber(senderMobileNumber);
			senderTransaction.settAmount(fundtransfer);
			senderTransaction.setTransactionType(TRANSACTIONTYPE.FUNDTRANSFER);
			senderTransaction.setInfo(senderInfo);
			
			receiverinfo = fundtransfer +" is Debited";		
			Transaction receiverTransaction = new Transaction();
			receiverTransaction.settId(tId);
			receiverTransaction.settMobileNumber(receiverMobileNumber);
			receiverTransaction.settAmount(fundtransfer);
			receiverTransaction.setTransactionType(TRANSACTIONTYPE.FUNDTRANSFER);
			receiverTransaction.setInfo(receiverinfo);
		
			if(walletRepo.saveTransactionDetails(senderMobileNumber, receiverTransaction) &&
					walletRepo.saveTransactionDetails(receiverMobileNumber, senderTransaction)) {
					return true;
			}
		}
		else if(deposit != null) {
			info = deposit+" is Credited";
			Transaction transaction = new Transaction();
			transaction.settId(tId);
			transaction.settMobileNumber(senderMobileNumber);
			transaction.settAmount(deposit);
			transaction.setTransactionType(TRANSACTIONTYPE.DEPOSIT);
			transaction.setInfo(info);
			
			if(walletRepo.saveTransactionDetails(receiverMobileNumber, transaction)) {
				return true;
			}
		}
		else {
			info = withdraw+" is Debited";
			Transaction transaction = new Transaction();
			transaction.settId(tId);
			transaction.settMobileNumber(receiverMobileNumber);
			transaction.settAmount(withdraw);
			transaction.setTransactionType(TRANSACTIONTYPE.WITHDRAW);
			transaction.setInfo(info);
			
			if(walletRepo.saveTransactionDetails(receiverMobileNumber, transaction)) {
				return true;
			}
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see com.cg.service.WalletService#getlast10Transaction(java.lang.String)
	 */
	@Override
	public List<Transaction> getlast10Transaction(String mobileNumber) throws IdNotExistException{
		
		LinkedList<Transaction> list = (LinkedList<Transaction>) walletRepo.getLast10Transaction(mobileNumber);
		
		if(list == null) {
			throw new IdNotExistException();
		}
		return list;
	}
	
	
}



