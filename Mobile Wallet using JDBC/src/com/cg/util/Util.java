package com.cg.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Util {

	private static Connection con;
	private static Statement stmt;
	
	public static Connection getConnection(){
		try {
			//load the driver class  
			Class.forName("oracle.jdbc.driver.OracleDriver");  
			
			//create  the connection object  
			con=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","system","Capgemini123");  
			
			//create the statement object  
			stmt=con.createStatement();
			
			//Creating Customer,Wallet Table if not Exists
			createTable();
			
			return con;
		}
		catch(Exception e){
			System.out.println(e);
			System.exit(0);
		}
		return null;  
	}
	
	private static void createTable() {
		
		//Creating tables if not exists
		String customerTable = "CREATE TABLE Customer " +
		           "(MOBILE NUMBER NOT NULL, " +
		           " NAME VARCHAR2(50), " + 
                   " PRIMARY KEY ( MOBILE ))";
		
		String walletTable = "CREATE TABLE WALLET "
				+ " (MOBILE NUMBER NOT NULL, "
				+ " BALANCE NUMBER, "
				+ " PRIMARY KEY ( MOBILE ))";
		
		try {
			stmt.executeUpdate(customerTable);
		} 
		catch (SQLException e) {}
		
		try {
			stmt.executeUpdate(walletTable);
		} 
		catch (SQLException e) {}
	}
}
