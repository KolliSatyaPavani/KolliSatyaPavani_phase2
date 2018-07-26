
package com.cg.mypaymentapp.jdbc.service;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cg.mypaymentapp.jdbc.beans.Customer;
import com.cg.mypaymentapp.jdbc.beans.Transactions;
import com.cg.mypaymentapp.jdbc.beans.Wallet;
import com.cg.mypaymentapp.jdbc.exception.InsufficientBalanceException;
import com.cg.mypaymentapp.jdbc.exception.InvalidInputException;
import com.cg.mypaymentapp.jdbc.repo.WalletRepo;
import com.cg.mypaymentapp.jdbc.repo.WalletRepoImpl;
import com.cg.mypaymentapp.jdbc.util.DBUtil;


public class WalletServiceImpl implements WalletService{

	long miilis=System.currentTimeMillis();
private WalletRepo repo=new WalletRepoImpl();
WalletRepo repo1 = new WalletRepoImpl();
	
	public WalletServiceImpl(Map<String, Customer> data){
		repo= new WalletRepoImpl(data);
	}

	public WalletServiceImpl(WalletRepo repo) 
	{
		this.repo = repo;
	}

	public WalletServiceImpl() {
		repo1 = new WalletRepoImpl();
		
	}

	public Customer createAccount(String name, String mobileNo, BigDecimal amount) {
		
		Customer customer1 = new Customer();
		customer1.setName(name);
		customer1.setMobileNo(mobileNo);
		Wallet wallet = new Wallet();
		wallet.setBalance(amount);
		customer1.setWallet(wallet);
		boolean b =repo.save(customer1);
		if(b)
		return customer1;
		else
			throw new InvalidInputException("Account is already created");
			
			

	}
	public Customer showBalance(String mobileNo) {
		Customer customer=repo.findOne(mobileNo);
		if(customer!=null)
			return customer;
		else
			throw new InvalidInputException("Invalid mobile no ");
	}

	public Customer fundTransfer(String sourceMobileNo, String targetMobileNo, BigDecimal amount) {
		Customer customer2=repo.findOne(sourceMobileNo);
		Customer customer3=repo.findOne(targetMobileNo);
        if(customer2!=null||customer3!= null) {
		BigDecimal total2;
		BigDecimal total3;
		BigDecimal x;
		total2 = customer2.getWallet().getBalance();
		total3 = customer3.getWallet().getBalance();
		if((total2.compareTo(amount)>0))
		{
			total2=total2.subtract(amount);
		total3=total3.add(amount);
		
		customer2.setWallet(new Wallet(total2));
		customer3.setWallet(new Wallet(total3));
Customer cust=		repo.updateBalance(customer2,"fund sent");
	Customer cust1=	repo.updateBalance(customer3,"fund received");
		}
		else
			System.out.println("Insufficient balance to transfer");
        }
        else
        	throw new InvalidInputException("Invalid mobile no ");
		return customer2;
		
	}

	public Customer depositAmount(String mobileNo, BigDecimal amount) {
		Customer customer=repo.findOne(mobileNo);
		BigDecimal total;
		if(customer!=null) {
		total = customer.getWallet().getBalance();
		total=total.add(amount);
		System.out.println(total);
		customer.setWallet(new Wallet(total));
Customer cust = repo.updateBalance(customer,"deposit");}
		else
			throw new InvalidInputException("Invalid mobile no ");
		return customer;
	}

	public Customer withdrawAmount(String mobileNo, BigDecimal amount) {
		Customer customer=repo.findOne(mobileNo);
		BigDecimal total;
		if(customer!=null) {
		total = customer.getWallet().getBalance();
		if((total.compareTo(amount)>0)) {
		total=total.subtract(amount);
		System.out.println(total);
		customer.setWallet(new Wallet(total));
		Customer cust = repo.updateBalance(customer,"withdraw");
		}
		else
			System.out.println("Insufficient balance to withdraw");
		}
		else
			throw new InvalidInputException("Invalid mobile no ");
		return customer;
	}

	@Override
	public List<Transactions> recentTransactions(String mobileNo) {
		List<Transactions> trans = repo.recentTransactions(mobileNo);
		if(trans!=null)
			return trans;
		else
			throw new InvalidInputException("no transactions to display");
		
	}
		
		

}
