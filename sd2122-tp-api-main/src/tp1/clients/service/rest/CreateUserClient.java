package tp1.clients.service.rest;

import java.io.IOException;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

import tp1.api.User;
import util.Debug;

public class CreateUserClient {
	
	private static Logger Log = Logger.getLogger(CreateUserClient.class.getName());

	static {
		System.setProperty("java.net.preferIPv4Stack", "true");
	}
	
	public static void main(String[] args) throws IOException {
		
		Debug.setLogLevel( Level.FINE, Debug.SD2122 );
		
		if (args.length != 5) {
			System.err.println("Use: java sd2122.aula3.clients.CreateUserClient url userId fullName email password");
			return;
		}

		String serverUrl = args[0];
		String userId = args[1];
		String fullName = args[2];
		String email = args[3];
		String password = args[4];

		User u = new User(userId, fullName, email, password);

		Log.info("Sending request to server.");

		var result = new RestUsersClient(URI.create(serverUrl)).createUser(u);
		System.out.println("Result: " + result);
	}

}
