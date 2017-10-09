package clients;

public interface IClient {
	
	public void startClient();

	public void sendToServer(String text);

	public void closeStreams();
	
}
