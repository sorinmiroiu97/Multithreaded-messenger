package clients;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class MultithreadedClient extends Thread {

	private Socket clientSocket;
	private DataInputStream dis;
	private DataOutputStream dos;
	private boolean shouldRun = true;
	private ClientGUI gui;

	public MultithreadedClient(Socket clientSocket, Client client) {
		this.clientSocket = clientSocket;
	}

	@Override
	public void run() {
		try {
			dos = new DataOutputStream(clientSocket.getOutputStream());
			// ous.flush();
			dis = new DataInputStream(clientSocket.getInputStream());

			while (shouldRun) {
				try {
					while (dis.available() == 0) {
						try {
							Thread.sleep(1);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}

					String reply = dis.readUTF();
					
					System.out.println(reply);
					//for tests
					gui.showMessage(reply);
					
				} catch (IOException e) {
					e.printStackTrace();
					close();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			close();
		}
	}

	protected void sendStringToServer(String text) {
		try {
			dos.writeUTF(text);
			dos.flush();
		} catch (IOException e) {
			e.printStackTrace();
			close();
		}
	}

	protected void close() {
		try {
			dis.close();
			dos.close();
			clientSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
