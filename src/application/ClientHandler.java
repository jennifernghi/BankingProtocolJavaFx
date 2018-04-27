package application;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

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

	public ArrayList<String> stringHandle(String str){
		ArrayList<String> strArr= new ArrayList<>(Arrays.asList(str.split(" ")));

		return strArr;
	}

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

	private String depositProtocol(ArrayList<String> arr){
		bank.deposit(Integer.parseInt(arr.get(1)), Double.parseDouble(arr.get(2)));
		double balance = bank.getAccount(Integer.parseInt(arr.get(1))).getBalance();
		return "new balance of account # " + arr.get(1) +" is "+ balance +"\n";
	}

	private String withdrawProtocol(ArrayList<String> arr){
		bank.withdraw(Integer.parseInt(arr.get(1)), Double.parseDouble(arr.get(2)));
		double balance = bank.getAccount(Integer.parseInt(arr.get(1))).getBalance();
		return "new balance of account # " + arr.get(1) +" is "+ balance+"\n";
	}

	private String balanceProtocol(ArrayList<String> arr){
		BankAccount account = bank.getAccount(Integer.parseInt(arr.get(1)));
		double balance = account.getBalance();
		return "balance of account # " + arr.get(1) +" is "+ balance+"\n";
	}

	public String getResponse() {
		return response;
	}

	@Override
	public void run() {
		//get input data
		String line = "";

		try {
			while(!line.equals("QUIT")){
				line = in.readUTF();
				System.out.println(line);

				//split string and carry out routines
				this.response = routines(stringHandle(line));
				System.out.println(this.response);

				//SEND RESPONSE TO SOCKET
				this.out.writeBytes(this.response);
			}
			
			this.socket.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return;

	}


}
