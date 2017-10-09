package clients;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client implements IClient{
	
	private Socket clientSocket;
	private String host;
	private int port;
	public MultithreadedClient clientThread;
	private ClientGUI gui;
	
	public Client(String host, int port) {
		this.host = host;
		this.port = port;
	}
	
	@Override
	public void startClient() {
		try {
			clientSocket = new Socket(host, port);
			clientThread = new MultithreadedClient(clientSocket, this);
			clientThread.start();
			
			listenForInput();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void listenForInput() {
		while(true) {
			String input = gui.getString();
			if(input.toLowerCase().equals("quit")) {
				break;
			}
			
			clientThread.sendStringToServer(input);
		}
		
		clientThread.close();
	}

	@Override
	public void sendToServer(String text) {
		clientThread.sendStringToServer(text);
	}
	
	public void closeStreams() {
		clientThread.close();
	}

}
