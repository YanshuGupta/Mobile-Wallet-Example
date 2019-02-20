package com.cg.repo;

import javax.persistence.EntityManager;

import com.cg.beans.Customer;
import com.cg.beans.Wallet;
import com.cg.util.Util;

public class WalletRepoImplementation implements WalletRepo {
	
	private EntityManager entitymanager;
	
	public WalletRepoImplementation() {
		
		entitymanager = Util.getEntityManager();
	}
	
	public boolean updateAccount(Customer updatedCustomer) {
		
		entitymanager.getTransaction().begin();
		
		Customer customer = entitymanager.find(Customer.class, updatedCustomer.getMobileNumber());
		
		customer.getWallet().setBalance(updatedCustomer.getWallet().getBalance());
	    
		entitymanager.getTransaction().commit();
		
		return true;
	}
	
	@Override
	public boolean updateAccount(Customer sender, Customer receiver) {
		
		entitymanager.getTransaction().begin();
		
		Customer customer1 = entitymanager.find(Customer.class, sender.getMobileNumber());
		customer1.getWallet().setBalance(sender.getWallet().getBalance());
		
		Customer customer2 = entitymanager.find(Customer.class, receiver.getMobileNumber());
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
				
				entitymanager.getTransaction( ).begin();
				entitymanager.persist(customer);
				entitymanager.getTransaction().commit();
				return true;
			}
			else { 
				return false;
			}
		}
		catch (Exception e) {
			
			System.out.println("Exception in creating Account Method");
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
		
		//entitymanager.getTransaction().begin();
		
		Customer customer = entitymanager.find(Customer.class, mobileNumber);
		
		if(customer != null) {
			return customer;
		}
		return null;
	}
	
	public boolean closeConnection() {
		Util.close();
		return true;
	}

	
}
