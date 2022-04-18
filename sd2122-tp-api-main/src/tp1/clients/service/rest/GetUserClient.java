package tp1.clients.service.rest;

import java.io.IOException;
import java.net.URI;
import java.util.logging.Logger;

public class GetUserClient {
	private static Logger Log = Logger.getLogger(GetUserClient.class.getName());

	static {
		System.setProperty("java.net.preferIPv4Stack", "true");
	}
	
	public static void main(String[] args) throws IOException {

		if (args.length != 3) {
			System.err.println("Use: java sd2122.aula3.clients.GetUserClient url userId password");
			return;
		}
		
		String serverUrl = args[0];
		String userId = args[1];
		String password = args[2];
		
		Log.info("Sending Request to server.");
		
		var result = new RestUsersClient(URI.create(serverUrl)).getUser(userId, password);
		
		System.out.println("Result: " + result);
	}
}
