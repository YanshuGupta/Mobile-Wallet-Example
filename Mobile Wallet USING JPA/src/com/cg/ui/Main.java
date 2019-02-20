package com.cg.ui;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import com.cg.beans.Customer;
import com.cg.exception.DuplicateIdentityException;
import com.cg.exception.IdNotExistException;
import com.cg.exception.InsufficientWalletBalanceException;
import com.cg.repo.WalletRepoImplementation;
import com.cg.service.WalletService;
import com.cg.service.WalletServiceImplementation;

public class Main {

	private static Scanner scanner = new Scanner(System.in);
	private static WalletService service = new WalletServiceImplementation(new WalletRepoImplementation());
	
	public static void main(String[] args) {
		
		do {
			System.out.println("Choose Option :-)");
			System.out.println(""
					+ "		1. CreateAccount \n"
					+ "		2. Show Balance \n"
					+ "		3. Deposit Amount \n"
					+ "		4. Withdraw Amount \n"
					+ "		5. Fund Transfer \n"
					+ "		6. Exit \n	");
			
			int choice =  scanner.nextInt();
			scanner.nextLine();
			
			switch(choice) {
			
			case 1:	createAccount();	
			break;
			
			case 2: showBalance();		
			break;
			
			case 3: depositAmount();
			break;
			
			case 4:	withdrawAmount();
			break;
			
			case 5: fundTransfer();
			break;
			
			case 6:	exit();
			
			default: System.out.println("You have Entered wrong choice");
			
			}
			
		}while(true);
		
	}

	
	private static void exit() {
		service.closeConnection();
		System.exit(0);
	}


	private static void createAccount() {
		
		String mobileNumber;
		String name;
		BigDecimal initialBalance;
		
		do {
			
			System.out.println("Enter Mobile Number");
			mobileNumber = scanner.next().trim();
			
			if(isValidMobileNumber(mobileNumber)) {
				
				if(!(service.isExist(mobileNumber))) {
					scanner.nextLine();
					break;
				}
				else {
					
					System.err.println("Mobile Number already exist");
				}
			}
			else {
				
				System.out.println("Mobile Number is not valid");
			}

		}while(true);
			
		do {
			System.out.println("Enter full name");
			name = scanner.nextLine().toUpperCase().trim();
			
			if(isValidName(name)) {
				name = name.trim();
				break;
			}
			else {
				
				System.err.println("Enter Valid Name");
			}
			
		}while(true);
		
		do {
			System.out.println("Enter Amount");
			String amount = scanner.next().trim();
			
			if(isValidAmount(amount)) {
				initialBalance = new BigDecimal(amount);
				break;
			}
			else {
				System.err.println("Enter Valid Amount, Make sure amount contain two digit after decimal");
			}
		}while(true);
	
		try {
			Customer customer = service.createAccount(mobileNumber, name, initialBalance);
			
			System.out.println("Customer Added Successfully");
			System.out.println("Wallet Account Number :"+customer.getMobileNumber()+'\n'
					+"Name :"+customer.getName()+'\n'
					+"Balance :"+customer.getWallet().getBalance());
		} catch (DuplicateIdentityException e) {
			System.err.println("Id Already Exist");
		}
	}

	private static boolean isValidAmount(String amount) {
		return Pattern.matches("[\\d]+([.][\\d])?([.][\\d][\\d])?", amount);
	}

	private static boolean isValidName(String name) {
		if(name.length() < 3)	return false;
		return Pattern.matches("([\\s]?[A-Z]+)+", name);
	}

	private static boolean isValidMobileNumber(String mobileNumber) {
		return mobileNumber.matches("[6-9]{1}\\d{9}");
	}

	private static void showBalance() {
		
		Customer customer;
		
		do {
			System.out.println("Enter Mobile Number");
			String mobileNumber = scanner.next();
			
			if(isValidMobileNumber(mobileNumber)) {
				
				try {
					customer = service.showBalance(mobileNumber);
					System.out.println("Your Current Balance is : "+ customer.getWallet().getBalance());
					break;
					
				} catch (IdNotExistException e) {
					System.err.println("Mobile Number is not Exist");
				}
				
			}
			else {
				System.err.println("Mobile Number is not valid");
			}
			
		}while(true);
		
	}

	private static void depositAmount() {
		
		Customer customer;
		String mobileNumber;
		BigDecimal depositAmountValue;
		
		do {
			
			System.out.println("Enter Mobile Number");
			mobileNumber = scanner.next();
			
			if(isValidMobileNumber(mobileNumber)) {
				break;
			}
			else {
				System.err.println("Mobile Number is not valid");
			}
			
		}while(true);
		
		do {
			System.out.println("Enter Amount");
			String balance = scanner.next();
			
			if(isValidAmount(balance)) {
				
				depositAmountValue = new BigDecimal(balance);
				break;
			}
			else {
				
				System.err.println("Amount is not valid");
			}
			
		}while(true);
		
		try {
			customer = service.depositAmount(mobileNumber, depositAmountValue);
			
			System.out.println("Deposit Amount Successfully");
			System.out.println("Wallet Account Number :"+customer.getMobileNumber()+'\n'
					+"Name :"+customer.getName()+'\n'
					+"Balance :"+customer.getWallet().getBalance());
			
		} catch (IdNotExistException e) {
			System.err.println("Mobile Number is not Exist");
		}
	}

	private static void withdrawAmount(){
		
		Customer customer;
		String mobileNumber;
		BigDecimal withdrawAmountValue;
		
		do {
			System.out.println("Enter Mobile Number");
			mobileNumber = scanner.next();
			
			if(isValidMobileNumber(mobileNumber)) {
				break;
			}
			else {
				System.err.println("Mobile Number is not Valid");
			}
			
		}while(true);
		
		do {
			
			System.out.println("Enter Amount");
			String balance = scanner.next();
			
			if(isValidAmount(balance)) {
				withdrawAmountValue = new BigDecimal(balance);
				break;
			}
			else {
				System.err.println("Amount is not Valid");
			}
			
		}while(true);
		
		try {
			customer = service.withdrawAmount(mobileNumber, withdrawAmountValue);
			System.out.println("withdraw Amount Successfully");
			System.out.println("Wallet Account Number :"+customer.getMobileNumber()+'\n'
					+"Name :"+customer.getName()+'\n'
					+"Balance :"+customer.getWallet().getBalance());
			
		} catch (IdNotExistException e) {
			System.err.println("Mobile Number is not Exist");
		} catch (InsufficientWalletBalanceException e) {
			System.err.println("Insufficient Wallet Balance");
		}
		
	}

	private static void fundTransfer() {
		
		String senderAccount;
		String receiverAccount;
		BigDecimal amount;
		
		do {
			
			System.out.println("Enter Sender Mobile Number");
			senderAccount = scanner.next();
			System.out.println("Enter Receiver Mobile Number");
			receiverAccount = scanner.next();
		
			if(isValidMobileNumber(senderAccount) && isValidMobileNumber(receiverAccount)) {
				break;
			}
			else {
				System.err.println("Mobile Number is not valid");
			}
			
		}while(true);
		
		do {
			System.out.println("Enter Amount");
			String balance = scanner.next();
			
			if(isValidAmount(balance)) {
				amount = new BigDecimal(balance);
				break;
			}
			else {
				System.err.println("Amount is not valid");
			}
			
		}while(true);
		
		try {
			Customer customer[] = service.fundTransfer(senderAccount, receiverAccount, amount);
			
			System.out.println("Sender \n Account Number :"+customer[0].getMobileNumber()+'\n'
					+"Name :"+customer[0].getName()+'\n'
					+"Balance :"+customer[0].getWallet().getBalance());
			
			System.out.println("Receiver \n Account Number :"+customer[1].getMobileNumber()+'\n'
							+"Name :"+customer[1].getName()+'\n'
							+"Balance :"+customer[1].getWallet().getBalance());
			
		} catch (IdNotExistException e) {
			System.err.println("Mobile Number is not Exist");
		} catch (InsufficientWalletBalanceException e) {
			System.err.println("Insufficient Wallet Balance");
		}
		
	}
}
