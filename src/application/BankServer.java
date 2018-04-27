package application;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class BankServer {

	private ServerSocket server = null;

	// starts server and waits for a connection
	public void server(int port) throws IOException {
		server = new ServerSocket(port);
		while (true) {
			System.out.println("waiting");
			Socket socket = null;

			socket = server.accept();
			Bank bank = new Bank();

			System.out.println("A new client connected: " + socket);

			// obtaining input and out streams
			DataInputStream input = new DataInputStream(socket.getInputStream());
			DataOutputStream output = new DataOutputStream(socket.getOutputStream());

			System.out.println("Assigning new thread for this client");

			// create a new thread object
			Thread t = new ClientHandler(socket, input, output, bank);

			// Invoking the start() method
			t.start();

		}

	}

	public static void main(String[] args) {
		BankServer server = new BankServer();
		try {
			server.server(5000);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
