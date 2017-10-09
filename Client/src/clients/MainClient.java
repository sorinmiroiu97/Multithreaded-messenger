package clients;

import javax.swing.JOptionPane;

public class MainClient {

	public static void main(String[] args) {

		String userName = JOptionPane.showInputDialog("Type in your guest-login user name: ");
		String IP = JOptionPane.showInputDialog("Type the ip to connect to: ");
		String hostString = JOptionPane.showInputDialog("Type the host to connect to: ");
		int host = Integer.valueOf(hostString);

		IClient client = new Client(IP, host);
		ClientGUI gui = new ClientGUI(client);
		gui.setUserName(userName);
		client.startClient();

	}

}
