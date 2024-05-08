package demo;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import thread.thread_createSocket;

public class server_Chat {
	static ArrayList<thread_createSocket> clients = new ArrayList<>();
	private static final Logger logger = Logger.getLogger(server_Chat.class.getName());
	int limit = 2;
	static int id = 0;
	ServerSocket sv = null;
	Socket socket = null;
	thread_createSocket thread_socket;
	DataOutputStream output = null;

	public void server() {

		try {
			ExecutorService ex = Executors.newFixedThreadPool(limit);
			sv = new ServerSocket(2000);
			System.out.println("Server is running ... ");
			while (true) {
				thread_socket = new thread_createSocket(sv.accept());
				output = new DataOutputStream(thread_socket.socket.getOutputStream());
				if (clients.size() == limit) {
					output.writeBoolean(true);
				}
				Thread thread = new Thread(thread_socket);
				ex.execute(thread);
			}
		} catch (IOException e) {
			logger.log(Level.SEVERE, "IOException create server socket : ", e);
		}

	}

	public static void broadcastMessage(String message, thread_createSocket client) {
		if (message.equals("END")) {
			client.sendMessage(message);
			clients.remove(client);
			message = client.name + " has split the chat group ";
			System.out.println(message);
			for (thread_createSocket cl : clients) {
				cl.sendMessage(message);
			}
		} else {
			message = client.name + " : " + message;
			for (thread_createSocket cl : clients) {
				if (cl != client) {
					cl.sendMessage(message);
				}
			}
		}

	}

	public static void main(String[] args) {
		server_Chat sv = new server_Chat();
		sv.server();
	}

	public static void addClient(thread_createSocket createSocket) {
		clients.add(createSocket);
		String message = createSocket.name + " joined the chat group";
		System.out.println(message);
		for (thread_createSocket cl : clients) {
			if (cl != createSocket) {
				cl.sendMessage(message);
			}
		}
	}

}
