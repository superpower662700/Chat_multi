package thread;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import demo.server_Chat;

public class thread_createSocket implements Runnable {
	public Socket socket = null;
	public String name = null;
	String message = "";
	DataInputStream input = null;
	DataOutputStream output = null;
	private static final Logger logger = Logger.getLogger(thread_createSocket.class.getName());

	public thread_createSocket(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		try {
			input = new DataInputStream(socket.getInputStream());
			output = new DataOutputStream(socket.getOutputStream());
			output.writeBoolean(false);
			name = input.readUTF();
			server_Chat.addClient(this);
			while ((message = input.readUTF()) != null) {
				if (message.equals("END")) {
					server_Chat.broadcastMessage(message, this);
					break;
				}
				server_Chat.broadcastMessage(message, this);
			}
		} catch (IOException e) {
			logger.log(Level.SEVERE, " IOException read : ", e);
		} finally {
			try {
				input.close();
			} catch (IOException e) {
				logger.log(Level.SEVERE, " IOException close input : ", e);
			}
			try {
				output.close();
			} catch (IOException e) {
				logger.log(Level.SEVERE, " IOException close output : ", e);
			}
			try {
				socket.close();
			} catch (IOException e) {
				logger.log(Level.SEVERE, " IOException close socket : ", e);
			}

		}

	}

	public void sendMessage(String message) {
		try {
			output.writeUTF(message);
		} catch (IOException e) {
			logger.log(Level.SEVERE, " IOException  writeUTF : ", e);
		}
	}
}
