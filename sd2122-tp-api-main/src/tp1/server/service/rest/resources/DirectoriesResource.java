package tp1.server.service.rest.resources;

import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import jakarta.inject.Singleton;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import tp1.api.FileInfo;
import tp1.api.User;
import tp1.api.service.rest.RestDirectory;
import tp1.clients.service.rest.RestUsersClient;
import tp1.server.service.rest.UsersServer;

@Singleton
public class DirectoriesResource implements RestDirectory {

	private final Map<String,List<FileInfo>> files = new HashMap<>(); 
	
	private static Logger Log = Logger.getLogger(DirectoriesResource.class.getName());
	
	
	public DirectoriesResource() {
	}
	
	private URI getURI(){
		String ip = "";
		try {
			ip = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		URI uri = URI.create(String.format("http://%s:%s/rest", ip, 8080));
		return uri;
	}
	@Override
	public FileInfo writeFile(String filename, byte[] data, String userId, String password) {
		// TODO Auto-generated method stub
		User user = new RestUsersClient(getURI()).getUser(userId, password);
		if(userId == null || password == null) {
			Log.info("Null exception.");
			throw new WebApplicationException( Status.BAD_REQUEST );
		}
			
		if(user == null) {
			Log.info("User does not exist.");
			throw new WebApplicationException( Status.NOT_FOUND );
		}
		if(!user.getPassword().equals(password)) {
			Log.info("Wrong Password.");
			throw new WebApplicationException( Status.FORBIDDEN + " " + getURI());
		}
		List<FileInfo> list = files.get(userId);
		if(list == null) {
			list = new ArrayList<FileInfo>();
		}
		FileInfo f = new FileInfo(userId, filename, null, null);
		list.add(f);
		return f;
	}

	@Override
	public void deleteFile(String filename, String userId, String password) {
		// TODO Auto-generated method stub

	}

	@Override
	public void shareFile(String filename, String userId, String userIdShare, String password) {
		// TODO Auto-generated method stub

	}

	@Override
	public void unshareFile(String filename, String userId, String userIdShare, String password) {
		// TODO Auto-generated method stub

	}

	@Override
	public byte[] getFile(String filename, String userId, String accUserId, String password) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<FileInfo> lsFile(String userId, String password) {
		// TODO Auto-generated method stub
		return null;
	}

}
