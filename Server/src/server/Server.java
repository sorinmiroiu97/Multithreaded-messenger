package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server implements IServer{

	private ServerSocket serverSocket;
	private Socket clientSocket;
	private boolean shouldRun = true;
	private final int PORT = 6969;
	private List<MultithreadedServer> connections = new ArrayList<MultithreadedServer>();

	@Override
	public void startServer() {
		try {
			serverSocket = new ServerSocket(PORT, 100);
			while (shouldRun) {
				clientSocket = serverSocket.accept();
				MultithreadedServer serverThread = new MultithreadedServer(clientSocket, this);
				serverThread.start();
				connections.add(serverThread);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public List<MultithreadedServer> getConnections() {
		return connections;
	}

}
