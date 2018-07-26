package com.cg.mypaymentapp.jdbc.repo;

import java.util.List;

import com.cg.mypaymentapp.jdbc.beans.Customer;
import com.cg.mypaymentapp.jdbc.beans.Transactions;

public interface WalletRepo {

public boolean save(Customer customer);
	
	public Customer findOne(String mobileNo);
	public Customer updateBalance(Customer customer,String transactionType);
	public List<Transactions> recentTransactions(String mobileNo);
}
