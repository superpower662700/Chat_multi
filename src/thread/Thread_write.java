package thread;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Thread_write implements Runnable {
	Scanner scanner = null;
	DataOutputStream output = null;
	private Socket socket = null;
	private static final Logger logger = Logger.getLogger(Thread_write.class.getName());

	public Thread_write(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		scanner = new Scanner(System.in);

		try {
			output = new DataOutputStream(socket.getOutputStream());
			while (true) {

				String chat = scanner.nextLine();
				output.writeUTF(chat);
				if (chat.equals("END")) {
					break;
				}
			}

		} catch (IOException e) {
			logger.log(Level.SEVERE, "IOException create data : ", e);
		} finally {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e2) {
				logger.log(Level.SEVERE, "InterruptedException : ", e2);
			}
			try {
				output.close();
			} catch (IOException e) {
				logger.log(Level.SEVERE, "IOException close output : ", e);
			}
			try {
				socket.close();
			} catch (IOException e1) {
				logger.log(Level.SEVERE, "IOException close socket : ", e1);
			}

			scanner.close();
		}

	}
}
