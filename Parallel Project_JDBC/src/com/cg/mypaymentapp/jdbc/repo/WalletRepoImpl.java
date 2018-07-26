package com.cg.mypaymentapp.jdbc.repo;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cg.mypaymentapp.jdbc.beans.Customer;
import com.cg.mypaymentapp.jdbc.beans.Transactions;
import com.cg.mypaymentapp.jdbc.beans.Wallet;
import com.cg.mypaymentapp.jdbc.exception.InvalidInputException;
import com.cg.mypaymentapp.jdbc.util.DBUtil;

public class WalletRepoImpl implements WalletRepo{

	private Map<String, Customer> data ; 
	List<Transactions> trans = new ArrayList<Transactions>();
	public WalletRepoImpl(Map<String, Customer> data) {
		super();
		this.data = data;
	}
	public WalletRepoImpl()
	{
		data = new HashMap<String, Customer>();
	}

	public boolean save(Customer customer) {
		//Customer c = findOne(customer.getMobileNo());
		//if(c== null)
		try(Connection con = DBUtil.getConnection())
		{
			PreparedStatement pstm = con.prepareStatement("insert into Customer values(?,?,?)");
			pstm.setString(1, customer.getMobileNo());
			pstm.setString(2, customer.getName());
			pstm.setBigDecimal(3,customer.getWallet().getBalance());
			pstm.execute();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
			return true;
		
	}

	public Customer findOne(String mobileNo) {
		
	Customer customer=null;
		try(Connection con = DBUtil.getConnection())
		{ PreparedStatement pstm1 = con.prepareStatement("select * from customer where mobileNo=?");
		   pstm1.setString(1, mobileNo);
			ResultSet rs = pstm1.executeQuery();
			
		if(rs.next()!=false)
		{
		customer = new Customer();
		customer.setMobileNo(rs.getString(1));
		customer.setName(rs.getString(2));
		customer.setWallet(new Wallet(rs.getBigDecimal(3)));
		}
		else
			return null;
		
	}
		catch(Exception e)
		{
			throw new InvalidInputException("invalid no");
		}
		return customer;
}
	public Customer updateBalance(Customer customer,String transactionType)
	{
		BigDecimal balance= customer.getWallet().getBalance();
		String mobileNo = customer.getMobileNo();
		try(Connection con = DBUtil.getConnection())
		{
			Statement stmt= con.createStatement();
			
		String str="update customer set balance="+balance+" where mobileno="+mobileNo;
		
		stmt.executeUpdate(str);
		
		PreparedStatement pstm = con.prepareStatement("insert into recentTransactions values(?,?,?)");
		pstm.setString(1, customer.getMobileNo());
		pstm.setString(2, transactionType);
		pstm.setBigDecimal(3,customer.getWallet().getBalance());
		try {
			pstm.execute();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();}
		return customer;		
	}
	@Override
	public List<Transactions> recentTransactions(String mobileNo) {
		try(Connection con = DBUtil.getConnection()){
			PreparedStatement pstm = con.prepareStatement("select * from recentTransactions where mobileNo = ?");
			pstm.setString(1, mobileNo);
			ResultSet res = pstm.executeQuery();
			if(res.next()==false) {
				throw new InvalidInputException("not found");
			}
			trans.add(new Transactions(res.getString(1), res.getString(2),res.getBigDecimal(3)));
		while(res.next())
		{
			trans.add(new Transactions(res.getString(1),res.getString(2),res.getBigDecimal(3)));
		}
		
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return trans;
	}

		
		
		
		
	







}
