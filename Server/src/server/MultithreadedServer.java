package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;

public class MultithreadedServer extends Thread {

	private Socket clientSocket;
	private Server server;
	private List<MultithreadedServer> connections;
	private DataInputStream dis;
	private DataOutputStream dos;
	private boolean shouldRun = true;

	public MultithreadedServer(Socket clientSocket, Server server) {
		super("ServerConnectionThread");
		this.clientSocket = clientSocket;
		this.server = server;
		connections = server.getConnections();
	}

	public void sendStringToClient(String text) {
		try {
			dos.writeUTF(text);
			dos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendStringToAllClients(String text) {
		for(int index = 0; index < connections.size(); index++) {
			MultithreadedServer serverThread = connections.get(index);
			serverThread.sendStringToClient(text);
		}
	}

	@Override
	public void run() {
		try {
			dos = new DataOutputStream(clientSocket.getOutputStream());
			// ous.flush();
			dis = new DataInputStream(clientSocket.getInputStream());
			
			while(shouldRun) {
				while(dis.available() == 0) {
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
				String textIn = dis.readUTF();
				sendStringToAllClients(textIn);
			}
			
			dis.close();
			dos.close();
			clientSocket.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
