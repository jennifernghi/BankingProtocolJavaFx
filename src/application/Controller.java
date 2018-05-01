package application;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class Controller implements Initializable {

	private BankClient client = null;
	@FXML
	private TextField account_text;

	@FXML
	private TextField balance_text;

	@FXML
	private TextField amount_text;

	@FXML
	private Button balance_btn;

	@FXML
	private Button deposit_btn;

	@FXML
	private Button withdraw_btn;

	@FXML
	private Button quit;

	@FXML
	private Label status;

	@FXML
	protected void balanceProtocol(ActionEvent event) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				client.request("BALANCE", account_text.getText().trim(), amount_text.getText().trim());

				Platform.runLater(new Runnable() {

					@Override
					public void run() {
						status.setText(client.getServerResponse());

					}
				});
			}
		}).start();
	}

	@FXML
	protected void depositProtocol(ActionEvent event) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				client.request("DEPOSIT", account_text.getText().trim(), amount_text.getText().trim());

				Platform.runLater(new Runnable() {

					@Override
					public void run() {
						status.setText(client.getServerResponse());

					}
				});
			}
		}).start();
	}

	@FXML
	protected void withdrawProtocol(ActionEvent event) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				client.request("WITHDRAW", account_text.getText().trim(), amount_text.getText().trim());

				Platform.runLater(new Runnable() {

					@Override
					public void run() {
						status.setText(client.getServerResponse());

					}
				});
			}
		}).start();

	}
	
	@FXML
	protected void quitProtocol(ActionEvent event) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				client.request("QUIT", account_text.getText().trim(), amount_text.getText().trim());

				Platform.exit();
			}
		}).start();

	}

	public BankClient getClient() {
		return client;
	}

	public TextField getAccount_text() {
		return account_text;
	}

	public TextField getBalance_text() {
		return balance_text;
	}

	public TextField getAmount_text() {
		return amount_text;
	}

	public Label getStatus() {
		return status;
	}

	

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		client = new BankClient();
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				client.start("127.0.0.1", 5000, Controller.this);

				System.out.println("status: " + client.getConnected());
				Platform.runLater(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						if (client.getConnected()) {
							status.setText("Conneted to server");
						} else {
							status.setText("Connecting...");
						}

					}
				});
			}
		}).start();

	}

}
