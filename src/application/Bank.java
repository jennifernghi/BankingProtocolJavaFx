package application;

import java.util.ArrayList;

public class Bank {
	ArrayList<BankAccount> acc = new ArrayList<>();
	
	public Bank(){
		acc.add(new BankAccount(0, 100));
		acc.add(new BankAccount(1, 200));
		acc.add(new BankAccount(2, 1000));
		acc.add(new BankAccount(3, 1100));
		acc.add(new BankAccount(4, 1040));
		acc.add(new BankAccount(5, 5000));
		acc.add(new BankAccount(6, 10000));
		
	}
	
	public BankAccount getAccount(int acctNum){
		for(int i =0; i< this.acc.size(); i++){
			if(this.acc.get(i).getAccountNumber()==acctNum){
				return acc.get(i);
			}
		}
		return null;
	}
	
	public void deposit(int acct, double amount){
		BankAccount account = getAccount(acct);
		setAmount(acct, account.getBalance() + amount);
	}
	
	public void withdraw(int acct, double amount){
		BankAccount account = getAccount(acct);
		double balance = account.getBalance() - amount;
		if(balance <0){
			setAmount(acct, 0);
		}else{
			setAmount(acct, balance);
		}
	}
	
	public double getBalance(int acct){
		return getAccount(acct).getBalance();
	}
	
	public void setAmount(int acctNum, double amount){
		for(int i =0; i< this.acc.size(); i++){
			if(this.acc.get(i).getAccountNumber()==acctNum){
				this.acc.set(i, new BankAccount(acctNum, amount));
			}
		}
	}
}
