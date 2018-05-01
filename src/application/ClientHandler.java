package application;

import java.io.*;
import java.net.Socket;
import java.util.*;

public class ClientHandler extends Thread {
	private Socket socket;
	private DataOutputStream out;
	private DataInputStream in;
	private Bank bank;
	private String response;

	public  ClientHandler(Socket socket,DataInputStream in,  DataOutputStream out, Bank bank) {
		this.socket = socket;
		this.in=in;
		this.out = out;
		this.bank = bank;
		this.response="";
	}

	// split String data into list 
	public ArrayList<String> stringHandle(String str){
		ArrayList<String> strArr= new ArrayList<>(Arrays.asList(str.split(" ")));

		return strArr;
	}
	
	public String getResponse() {
		return response;
	}

	// server protocols
	// return string response for client
	private String routines(ArrayList<String> arr){
		switch(arr.get(0)){
		case "DEPOSIT":
			//length 3: DEPOSIT n amount
			System.out.println("deposit processing...");
			return depositProtocol(arr);

		case "WITHDRAW":
			//length 3: WITHDRAW n amount
			System.out.println("withdraw processing...");
			return withdrawProtocol(arr);

		case "BALANCE":
			//length 2: BALANCE n
			System.out.println("balance processing...");
			return balanceProtocol(arr);
		default:
			return "";
		}
	}

	//deposit protocol
	private String depositProtocol(ArrayList<String> arr){
		bank.deposit(Integer.parseInt(arr.get(1)), Double.parseDouble(arr.get(2)));
		double balance = bank.getAccount(Integer.parseInt(arr.get(1))).getBalance();
		return "new balance of account # " + arr.get(1) +" is "+ balance +"\n";
	}

	//withdraw protocol
	private String withdrawProtocol(ArrayList<String> arr){
		bank.withdraw(Integer.parseInt(arr.get(1)), Double.parseDouble(arr.get(2)));
		double balance = bank.getAccount(Integer.parseInt(arr.get(1))).getBalance();
		return "new balance of account # " + arr.get(1) +" is "+ balance+"\n";
	}

	//balance protocol
	private String balanceProtocol(ArrayList<String> arr){
		BankAccount account = bank.getAccount(Integer.parseInt(arr.get(1)));
		double balance = account.getBalance();
		return "balance of account # " + arr.get(1) +" is "+ balance+"\n";
	}

	

	@Override
	public void run() {
		//get input data
		String line = "";

		try {
			//keep getting client data 
			while(!line.equals("QUIT")){
				line = in.readUTF();
				System.out.println(line);

				//split string and carry out routines
				this.response = routines(stringHandle(line));
				System.out.println(this.response);

				//SEND RESPONSE TO CLIENT SOCKET
				this.out.writeBytes(this.response);
			}
			
			//if client send QUIT signal, close the socket
			this.socket.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return;
	}
}
