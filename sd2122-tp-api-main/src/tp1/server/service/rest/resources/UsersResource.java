package tp1.server.service.rest.resources;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import jakarta.inject.Singleton;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response.Status;
import tp1.api.User;
import tp1.api.service.rest.RestUsers;
import tp1.clients.service.rest.RestDirectoriesClient;
import tp1.server.service.rest.DirectoriesServer;
import tp1.server.service.rest.UsersServer;

@Singleton
public class UsersResource implements RestUsers {

	private final Map<String,User> users = new HashMap<>();

	private static Logger Log = Logger.getLogger(UsersResource.class.getName());

	public UsersResource() {
	}

	@Override
	public String createUser(User user) {
		Log.info("createUser : " + user);

		// Check if user data is valid
		if(user.getUserId() == null || user.getPassword() == null || user.getFullName() == null || 
				user.getEmail() == null) {
			Log.info("User object invalid.");
			throw new WebApplicationException( Status.BAD_REQUEST );
		}

		// Check if userId already exists
		if( users.containsKey(user.getUserId())) {
			Log.info("User already exists.");
			throw new WebApplicationException( Status.CONFLICT );
		}

		//Add the user to the map of users
		users.put(user.getUserId(), user);
		return user.getUserId();
	}


	@Override
	public User getUser(String userId, String password) {
		Log.info("getUser : user = " + userId + "; pwd = " + password);
		// Check if user is valid
		/*if(userId == null || password == null) {
			Log.info("UserId or passwrod null.");
			throw new WebApplicationException( Status.BAD_REQUEST );
		}*/

		User user = users.get(userId);

		// Check if user exists 
		if( user == null ) {
			Log.info("User does not exist.");
			throw new WebApplicationException( Status.NOT_FOUND);
		}

		//Check if the password is correct
		if( !user.getPassword().equals( password)) {
			Log.info("Password is incorrect.");
			throw new WebApplicationException( Status.FORBIDDEN );
		}

		return user;
	}


	@Override
	public User updateUser(String userId, String password, User user) {
		Log.info("updateUser : user = " + userId + "; pwd = " + password + " ; user = " + user);
		// TODO Complete method
		User u = users.get(userId);

		if(u == null) {
			Log.info("User does not exist.");
			throw new WebApplicationException( Status.NOT_FOUND );
		}
		if(!u.getPassword().equals( password)) {
			Log.info("Password is incorrect.");
			throw new WebApplicationException( Status.FORBIDDEN );
		}
		if(user.getEmail() != null) {
			u.setEmail(user.getEmail());
		}
		if(user.getFullName() != null) {
			u.setFullName(user.getFullName());
		}
		if(user.getPassword() != null) {
			u.setPassword(user.getPassword());
		}
		return u;
	}


	@Override
	public User deleteUser(String userId, String password) {
		Log.info("deleteUser : user = " + userId + "; pwd = " + password);
		User u = users.get(userId);

		if(u == null) {
			Log.info("User does not exist.");
			throw new WebApplicationException( Status.NOT_FOUND );
		}
		if(!u.getPassword().equals( password)) {
			Log.info("Password is incorrect.");
			throw new WebApplicationException( Status.FORBIDDEN );
		}
		for(URI uri: UsersServer.foundURI(DirectoriesServer.SERVICE)) {
			RestDirectoriesClient rdc = new RestDirectoriesClient(uri);
			rdc.deleteAllUserFiles(userId);
		}
		users.remove(userId);
		return u;
	}


	@Override
	public List<User> searchUsers(String pattern) {
		Log.info("searchUsers : pattern = " + pattern);
		List<User> toReturn = new ArrayList<>();
		for(String id : users.keySet()) {
			User u = users.get(id);
			String userFullName = u.getFullName().toUpperCase();
			if(userFullName.contains(pattern.toUpperCase())) {
				toReturn.add(u);
			}
		}
		return toReturn;
	}

	@Override
	public User getUserWithoutPassword(String userId) {
		User u = users.get(userId);
		if(u == null) {
			Log.info("User does not exist.");
			throw new WebApplicationException( Status.NOT_FOUND );
		}

		return u;
	}

}
