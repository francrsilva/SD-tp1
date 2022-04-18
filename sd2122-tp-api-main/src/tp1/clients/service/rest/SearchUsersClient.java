package tp1.clients.service.rest;

import java.io.IOException;
import java.net.URI;
import java.util.logging.Level;

import util.Debug;

public class SearchUsersClient {
	
	static {
		System.setProperty("java.net.preferIPv4Stack", "true");
	}
	
	public static void main(String[] args) throws IOException {

		Debug.setLogLevel(Level.FINE, Debug.SD2122);
		
		if (args.length != 2) {
			System.err.println("Use: java sd2122.aula3.clients.SearchUsersClient url userId ");
			return;
		}

		String serverUrl = args[0];
		String userId = args[1];


		System.out.println("Sending request to server.");

		new RestUsersClient(URI.create(serverUrl)).searchUsers(userId);

	}

}