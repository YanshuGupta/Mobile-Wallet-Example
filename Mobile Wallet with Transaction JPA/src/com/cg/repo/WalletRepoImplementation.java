package com.cg.repo;

import java.util.List;

import javax.persistence.EntityManager;

import com.cg.beans.Customer;
import com.cg.beans.Transaction;
import com.cg.util.Util;

public class WalletRepoImplementation implements WalletRepo {
	
	private EntityManager entitymanager;
	
	public WalletRepoImplementation() {
		
		entitymanager = Util.getEntityManager();
	}
	
	public boolean updateAccount(Customer updatedCustomer) {
		
		entitymanager.getTransaction().begin();
		
		Customer customer = entitymanager.find(Customer.class, updatedCustomer.getMobileNumber());
		
		entitymanager.persist(updatedCustomer.getTransactionList().get(updatedCustomer.getTransactionList().size()-1));
		
		customer.getWallet().setBalance(updatedCustomer.getWallet().getBalance());
	    
		entitymanager.getTransaction().commit();
		
		return true;
	}
	
	@Override
	public boolean updateAccount(Customer sender, Customer receiver) {
		
		entitymanager.getTransaction().begin();
		
		Customer customer1 = entitymanager.find(Customer.class, sender.getMobileNumber());
		entitymanager.persist(sender.getTransactionList().get(sender.getTransactionList().size()-1));
		customer1.getWallet().setBalance(sender.getWallet().getBalance());
		
		Customer customer2 = entitymanager.find(Customer.class, receiver.getMobileNumber());
		entitymanager.persist(receiver.getTransactionList().get(receiver.getTransactionList().size()-1));
		customer2.getWallet().setBalance(receiver.getWallet().getBalance());
	      
		entitymanager.getTransaction().commit();
		
		return true;
		
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cg.repo.WalletRepo#save(com.cg.beans.Customer)
	 */
	@Override
	public boolean save(Customer customer) {
		try {
			Customer c = search(customer.getMobileNumber());
			
			if(c == null) {
				entitymanager.getTransaction().begin();
				
				for(Transaction transaction: customer.getTransactionList()) {
					entitymanager.persist(transaction);
				}
				
				entitymanager.persist(customer);
				
				entitymanager.getTransaction().commit();
				return true;
			}
			else { 
				return false;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cg.repo.WalletRepo#search(java.lang.String)
	 */
	@Override
	public Customer search(String mobileNumber) {
				
		Customer customer = entitymanager.find(Customer.class, mobileNumber);
		
		if(customer != null) {
			return customer;
		}
		return null;
	}
	
	public boolean closeEntityManager() {
		Util.closeEntityManager();
		return true;
	}
	
	@Override
	public List<Transaction> getTransaction(String mobileNumber) {
		
		Customer customer = entitymanager.find(Customer.class, mobileNumber);
		
		if(customer != null) {
			return customer.getTransactionList();
		}
		return null;
		
		
	}
	
}
