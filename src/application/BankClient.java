package application;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;

public class BankClient {

	// initialize socket and input output streams
	private Socket socket = null;
	private DataOutputStream output = null;
	private BufferedReader readerServer = null;
	private int acctNum;
	private double balance;
	private String serverResponse;
	private boolean connected = false;
	private Controller controller;

	public BankClient() {
		this.serverResponse = "";
	}

	public String getServerResponse() {
		return serverResponse;
	}

	public void setController(Controller controller) {
		this.controller = controller;
	}

	public int getAcctNum() {
		return acctNum;
	}

	public double getBalance() {
		return balance;
	}

	public boolean getConnected() {
		return connected;
	}

	public void start(String host, int port, Controller controller) {
		try {
			this.controller = controller;
			//open new socket
			socket = new Socket(host, port);

			this.connected = true;
			if (this.connected) {
				this.controller.getStatus().setText("Conneted");
			}

			if (socket != null) {
				//buffer get responses from server
				readerServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));

				// sends output to the socket
				output = new DataOutputStream(socket.getOutputStream());
			}

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void serverResponse() {
		// GET RESPONSE FROM SERVER
		try {
			this.serverResponse = readerServer.readLine();

			System.out.println(this.serverResponse);

			ArrayList<String> resArr = new ArrayList<>(Arrays.asList(this.serverResponse.split(" ")));

			for (int i = 0; i < resArr.size(); i++) {
				if (resArr.get(i).equals("#")) {
					this.acctNum = Integer.parseInt(resArr.get(i + 1));
					controller.getAccount_text().setText(String.valueOf(this.acctNum));
				}

				if (resArr.get(i).equals("is")) {
					this.balance = Double.parseDouble(resArr.get(i + 1));
					controller.getBalance_text().setText(String.valueOf(this.balance));
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void request(String protocol, String acctNum, String amount) {
		try {
			//send data to server
			switch (protocol) {
				case "DEPOSIT":
					// send: DEPOSIT accountNumber amount
					output.writeUTF("DEPOSIT " + this.controller.getAccount_text().getText().trim() + " " + this.controller.getAmount_text().getText().trim());
					//get server response
					serverResponse();
					break;
				case "WITHDRAW":
					// send: WITHDRAW accountNumber amount
					output.writeUTF("WITHDRAW " + this.controller.getAccount_text().getText().trim() + " " + this.controller.getAmount_text().getText().trim());
					//get server response
					serverResponse();
					break;
				case "BALANCE":
					// send: BALANCE accountNumber 
					output.writeUTF("BALANCE " + this.controller.getAccount_text().getText().trim());
					//get server response
					serverResponse();
					break;
				case "QUIT":
					// send: QUIT  
					output.writeUTF("QUIT");
					break;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	

}
