package server;

public class MainServer {

	public static void main(String[] args) {
		IServer server = new Server();
		server.startServer();
	}

}
