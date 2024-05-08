package demo;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import thread.Thread_read;
import thread.Thread_write;

public class client_Chat {
	private static final Logger logger = Logger.getLogger(client_Chat.class.getName());
	Socket socket = null;
	Scanner scanner = null;
	DataOutputStream output;
	DataInputStream input;
	boolean check = true;

	public void connect() {

		try {
			socket = new Socket("localhost", 2000);

		} catch (UnknownHostException e) {
			logger.log(Level.SEVERE, "Unknown host exception", e);
		}
//		catch (ConnectException e) {
//			logger.log(Level.SEVERE, "Connect Exception", e);
//		} 
		catch (IOException e) {

			System.out.println("Connect Fail, doing it again. Please wait !");
			while (true) {
				try {
					Thread.sleep(2000);
					socket = new Socket("localhost", 2000);
					break;
				} catch (IOException | InterruptedException ex) {
				}
			}
		} finally {
			System.out.println("Connected to the server.");
			System.out.println("What is your name ? ");
			scanner = new Scanner(System.in);
			try {
				output = new DataOutputStream(socket.getOutputStream());
				input = new DataInputStream(socket.getInputStream());
			} catch (IOException e) {
				logger.log(Level.SEVERE, "IOException create data output :", e);
			}
//			catch (NullPointerException e) {
//				logger.log(Level.SEVERE, "NullPointerException create data output :", e);
//			}

			try {
				String name = scanner.nextLine();
				output.writeUTF(name);

			} catch (IOException e) {
				logger.log(Level.SEVERE, "IOException write data name :", e);
			}

			while (true) {
				if (input != null) {
					try {
						check = input.readBoolean();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if (!check) {
					break;
				}
				System.out.println("FUll Group. pls wait !");
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			System.out.println("Start chat :");
			Runnable read = new Thread_read(socket);
			Runnable write = new Thread_write(socket);
			Thread tr = new Thread(read);
			Thread tw = new Thread(write);
			tr.start();
			tw.start();
		}
	}

	public static void main(String[] args) {
		client_Chat cl = new client_Chat();
		cl.connect();
	}
}
