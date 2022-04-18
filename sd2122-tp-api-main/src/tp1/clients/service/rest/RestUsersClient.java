package tp1.clients.service.rest;

import java.net.URI;
import java.util.List;

import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import tp1.api.User;
import tp1.api.service.rest.RestUsers;

public class RestUsersClient extends RestClient implements RestUsers {

	final WebTarget target;
	
	RestUsersClient( URI serverURI ) {
		super( serverURI );
		target = client.target( serverURI ).path( RestUsers.PATH );
	}
	
	@Override
	public String createUser(User user) {
		return super.reTry( () -> {
			return clt_createUser( user );
		});
	}

	@Override
	public User getUser(String userId, String password) {
		return super.reTry(()->clt_getUser(userId,password));
	}

	@Override
	public User updateUser(String userId, String password, User user) {
		return super.reTry(()->clt_updateUser(userId,password,user));
	}

	@Override
	public User deleteUser(String userId, String password) {
		return reTry(()->clt_deleteUser(userId,password));
	}

	@Override
	public List<User> searchUsers(String pattern) {
		return super.reTry( () -> clt_searchUsers( pattern ));
	}


	private String clt_createUser( User user) {
		
		Response r = target.request()
				.accept(MediaType.APPLICATION_JSON)
				.post(Entity.entity(user, MediaType.APPLICATION_JSON));

		if( r.getStatus() == Status.OK.getStatusCode() && r.hasEntity() )
			return r.readEntity(String.class);
		else 
			System.out.println("Error, HTTP error status: " + r.getStatus() );
		
		return null;
	}
	private User clt_getUser(String userId, String password) {
		Response r = target.path( userId )
				.queryParam(RestUsers.PASSWORD, password).request()
				.accept(MediaType.APPLICATION_JSON)
				.get();

		if( r.getStatus() == Status.OK.getStatusCode() && r.hasEntity() ) {
			System.out.println("Success:");
			User u = r.readEntity(User.class);
			System.out.println( "User : " + u);
			return u;
		} else
			System.out.println("Error, HTTP error status: " + r.getStatus() );
		return null;
	}
	private List<User> clt_searchUsers(String pattern) {
		Response r = target
				.queryParam(QUERY, pattern)
				.request()
				.accept(MediaType.APPLICATION_JSON)
				.get();

		if( r.getStatus() == Status.OK.getStatusCode() && r.hasEntity() )
			return r.readEntity(new GenericType<List<User>>() {});
		else 
			System.out.println("Error, HTTP error status: " + r.getStatus() );
		
		return null;
	}
	
	private User clt_updateUser(String userId, String password, User user) {
		Response r = target.path( userId )
				.queryParam(RestUsers.PASSWORD, password).request()
				.accept(MediaType.APPLICATION_JSON)
				.put(Entity.entity(user, MediaType.APPLICATION_JSON));
		
		
		if( r.getStatus() == Status.OK.getStatusCode() && r.hasEntity() ) {
			System.out.println("Success, updated user with id: " + r.readEntity(String.class) );
			return user;
		}
		else
			System.out.println("Error, HTTP error status: " + r.getStatus() );
		return null;
	}
	
	private User clt_deleteUser(String userId, String password) {
		Response r = target.path( userId )
				.queryParam(RestUsers.PASSWORD, password).request()
				.accept(MediaType.APPLICATION_JSON)
				.delete();

		if( r.getStatus() == Status.OK.getStatusCode() && r.hasEntity() ) {
			System.out.println("Success:");
			User u = r.readEntity(User.class);
			System.out.println( "User : " + u);
			return u;
		} else
			System.out.println("Error, HTTP error status: " + r.getStatus() );
		return null;
	}
}
