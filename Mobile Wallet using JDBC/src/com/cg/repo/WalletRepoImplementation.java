package com.cg.repo;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.cg.beans.Customer;
import com.cg.beans.Wallet;
import com.cg.util.Util;

public class WalletRepoImplementation implements WalletRepo {
	
	private Connection con;
	
	public WalletRepoImplementation() {
		
		con = Util.getConnection();
	}
	
	public boolean updateAccount(Customer customer) {
		
		try {
			con.setAutoCommit(false);
			
			PreparedStatement updateAccount = con.prepareStatement("UPDATE WALLET SET BALANCE = ? WHERE MOBILE = ? ");
			updateAccount.setBigDecimal(1, customer.getWallet().getBalance());
			updateAccount.setString(2, customer.getMobileNumber());
			updateAccount.executeUpdate();
			
			updateAccount.close();
			con.commit();
			con.setAutoCommit(true);
		}
		catch (Exception e) {
			System.out.println("error");
			System.out.println(e);
			return false;
		}
		return true;
	}
	
	@Override
	public boolean updateAccount(Customer sender, Customer receiver) {
		try {
			con.setAutoCommit(false);
			
			PreparedStatement updateAccount = con.prepareStatement("UPDATE WALLET SET BALANCE = ? WHERE MOBILE = ? ");
			
			updateAccount.setBigDecimal(1, sender.getWallet().getBalance());
			updateAccount.setString(2, sender.getMobileNumber());
			updateAccount.executeUpdate();
			
			updateAccount.setBigDecimal(1, receiver.getWallet().getBalance());
			updateAccount.setString(2, receiver.getMobileNumber());
			updateAccount.executeUpdate();
			
			updateAccount.close();
			con.commit();
			con.setAutoCommit(true);
		}
		catch (Exception e) {
			System.out.println(e);
			return false;
		}
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
			con.setAutoCommit(false);
			
			PreparedStatement saveCustomer = con.prepareStatement("INSERT INTO CUSTOMER(MOBILE, NAME) VALUES(?, ?)");
			saveCustomer.setString(1, customer.getMobileNumber());
			saveCustomer.setString(2, customer.getName());
			saveCustomer.executeQuery();
			
			PreparedStatement saveWallet = con.prepareStatement("INSERT INTO WALLET(MOBILE, BALANCE) VALUES(?, ?)");
			saveWallet.setString(1, customer.getMobileNumber());
			saveWallet.setBigDecimal(2, customer.getWallet().getBalance());
			saveWallet.executeQuery();
			
			saveCustomer.close();
			saveWallet.close();
			
			con.commit();
			con.setAutoCommit(true);
		}
		catch (SQLException e) {
			System.out.println(e);
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cg.repo.WalletRepo#search(java.lang.String)
	 */
	@Override
	public Customer search(String mobileNumber) {

		try {
			PreparedStatement selectCustomer = con.prepareStatement("SELECT NAME FROM CUSTOMER WHERE MOBILE = ?");
			PreparedStatement selectWallet = con.prepareStatement("SELECT BALANCE FROM WALLET WHERE MOBILE = ?");

			selectCustomer.setString(1, mobileNumber);
			selectWallet.setString(1, mobileNumber);
			
			ResultSet rs1 = selectCustomer.executeQuery();
			ResultSet rs2 = selectWallet.executeQuery();
			
			String name = null;
			BigDecimal balance = null;
			
			try {
				rs1.next();
				rs2.next();
				name = rs1.getString(1);
				balance = rs2.getBigDecimal(1);
			}
			catch (Exception e) {
				//System.out.println("ID is Unique");
			}
			
			if(name == null || balance == null) {
				return null;
			}
			
			Wallet wallet = new Wallet();
			wallet.setBalance(balance);
			
			Customer customer = new Customer();
			customer.setMobileNumber(mobileNumber);
			customer.setName(name);
			customer.setWallet(wallet);
			
			rs1.close();
			rs2.close();
			selectCustomer.close();
			selectWallet.close();
			
			return customer;
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean closeConnection() {
		//close the connection object  
		try {
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} 
		return true;
	}

	
}
