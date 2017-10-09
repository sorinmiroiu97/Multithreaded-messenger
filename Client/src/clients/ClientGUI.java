package clients;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class ClientGUI extends JFrame {

	private JTextField userText;
	private static JTextArea chatWindow;
	private JButton sendButton;
	private JButton disconnectButton;
	private JButton reconnectButton;
	private DataOutputStream dos;
	private DataInputStream dis;
	private String userName;
	private String message;
	private boolean disconnected = false;
	private IClient client;

	// default constructor
	public ClientGUI(IClient client) {
		super("JMessenger Client");
		this.client = client;
		initGUI();

	}

	protected String getString() {
		return userText.getText();
	}

	// setter for the client's user name
	public void setUserName(final String name) {
		this.userName = name;
		this.setTitle("Messenger " + userName);
	}

	// UI creation
	private void initGUI() {
		userText = new JTextField();
		userText.setEditable(true);
		userText.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String text = getString();
				if (text != null && text.trim().length() > 0) {
					client.sendToServer("\n" + userName + " > " + text);
				}
				userText.setText("");
			}
		});

		JPanel typingPanel = new JPanel();
		typingPanel.setLayout(new BoxLayout(typingPanel, BoxLayout.X_AXIS));
		typingPanel.add(Box.createHorizontalStrut(10));
		typingPanel.add(userText);
		typingPanel.add(Box.createHorizontalStrut(10));

		sendButton = new JButton("Send");
		sendButton.setEnabled(true);
		sendButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String text = getString();
				if (text != null && text.trim().length() > 0) {
					client.sendToServer("\n" + userName + " > " + text);
				}
				userText.setText("");
			}
		});

		disconnectButton = new JButton("Disconnect");
		disconnectButton.setEnabled(true);
		disconnectButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				disconnected = true;
				client.closeStreams();
				// TODO fix this
				// Process.this.destroyForcibly();
			}
		});

		reconnectButton = new JButton("Reconnect");
		reconnectButton.setEnabled(true);
		reconnectButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				disconnected = false;
				//TODO fix this
				// restarts the server's connection
			}
		});

		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
		buttonsPanel.add(Box.createVerticalStrut(10));
		buttonsPanel.add(sendButton);
		buttonsPanel.add(Box.createVerticalStrut(10));
		buttonsPanel.add(disconnectButton);
		buttonsPanel.add(Box.createVerticalStrut(10));
		buttonsPanel.add(reconnectButton);
		buttonsPanel.add(Box.createVerticalStrut(10));

		typingPanel.setSize(200, 50);
		typingPanel.add(buttonsPanel);

		chatWindow = new JTextArea();
		chatWindow.setEditable(false);
		JPanel chatPanel = new JPanel();
		chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.X_AXIS));
		chatPanel.add(Box.createHorizontalStrut(10));

		JScrollPane scrollChatPanel = new JScrollPane(chatWindow);
		scrollChatPanel.setPreferredSize(new Dimension(390, 150));
		chatPanel.add(scrollChatPanel);

		JPanel contentPane = new JPanel();
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		contentPane.add(Box.createVerticalStrut(10));
		contentPane.add(chatPanel);
		contentPane.add(Box.createVerticalStrut(10));
		contentPane.add(typingPanel);
		contentPane.add(Box.createVerticalStrut(10));
		
		buttonsEnabled(disconnected);
		
		add(contentPane);

		setSize(500, 350);
		setVisible(true);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}


	// updates chat windows
	protected static void showMessage(final String text) {
		if (SwingUtilities.isEventDispatchThread()) {
			chatWindow.append(text);
		} else {
			SwingUtilities.invokeLater(new Runnable() {

				@Override
				public void run() {
					showMessage(text);
				}
			});
		}
	}

	// buttons enabling depending the disconnected boolean
	// disconnected=true if disconnectButton is clicked
	private void buttonsEnabled(final boolean disconnected) {
		if (SwingUtilities.isEventDispatchThread()) {
			if (disconnected) {
				
				reconnectButton.setEnabled(true);
				disconnectButton.setEnabled(false);
				sendButton.setEnabled(false);
			} else {
				reconnectButton.setEnabled(false);
				disconnectButton.setEnabled(true);
			}
		} else {
			SwingUtilities.invokeLater(new Runnable() {

				@Override
				public void run() {
					buttonsEnabled(disconnected);
				}
			});
		}
	}

}
