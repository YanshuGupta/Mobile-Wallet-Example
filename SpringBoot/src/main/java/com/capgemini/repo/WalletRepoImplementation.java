package com.capgemini.repo;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.capgemini.bean.Customer;

@Repository
public class WalletRepoImplementation implements WalletRepo {
	
	@PersistenceContext
	private EntityManager entitymanager;
	
	@Transactional
	public boolean updateAccount(Customer updatedCustomer) {
		
		
		Customer customer = entitymanager.find(Customer.class, updatedCustomer.getMobileNo());
		
		customer.getWallet().setBalance(updatedCustomer.getWallet().getBalance());
	    		
		return true;
	}
	
	@Override
	@Transactional
	public boolean updateAccount(Customer sender, Customer receiver) {
		
		
		Customer customer1 = entitymanager.find(Customer.class, sender.getMobileNo());
		customer1.getWallet().setBalance(sender.getWallet().getBalance());
		
		Customer customer2 = entitymanager.find(Customer.class, receiver.getMobileNo());
		customer2.getWallet().setBalance(receiver.getWallet().getBalance());
	      
		
		return true;
		
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cg.repo.WalletRepo#save(com.cg.beans.Customer)
	 */
	@Override
	@Transactional
	public boolean save(Customer customer) {
		try {
			
			Customer c = search(customer.getMobileNo());
			
			if(c == null) {
				entitymanager.persist(customer);
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

	@Override
	public boolean closeConnection() {
		// TODO Auto-generated method stub
		return false;
	}
	
	

	
}
