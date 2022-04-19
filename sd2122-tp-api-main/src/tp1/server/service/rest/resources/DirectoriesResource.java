package tp1.server.service.rest.resources;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.inject.Singleton;
import jakarta.ws.rs.client.WebTarget;
import tp1.api.FileInfo;
import tp1.api.User;
import tp1.api.service.rest.RestDirectory;
import tp1.clients.service.rest.RestUsersClient;

@Singleton
public class DirectoriesResource implements RestDirectory {

	private final Map<String,FileInfo> files = new HashMap<>(); 
	
	
	public DirectoriesResource() {
	}
	@Override
	public FileInfo writeFile(String filename, byte[] data, String userId, String password) {
		// TODO Auto-generated method stub
		User u = RestUsersClient.getUser(filename,password);
		return null;
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
