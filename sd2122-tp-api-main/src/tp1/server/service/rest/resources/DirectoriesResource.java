package tp1.server.service.rest.resources;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import jakarta.inject.Singleton;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import tp1.api.FileInfo;
import tp1.api.User;
import tp1.api.service.rest.RestDirectory;
import tp1.clients.service.rest.RestUsersClient;
import tp1.clients.service.rest.RestFilesClient;
import tp1.server.service.rest.DirectoriesServer;

@Singleton
public class DirectoriesResource implements RestDirectory {

	private final Map<String,List<FileInfo>> files = new HashMap<>(); 

	private static Logger Log = Logger.getLogger(DirectoriesResource.class.getName());


	public DirectoriesResource() {
	}


	@Override
	public FileInfo writeFile(String filename, byte[] data, String userId, String password) {
		// TODO Auto-generated method stub
		User user = null;
		for(URI uri: DirectoriesServer.foundURI("users")) {
			user = new RestUsersClient(uri).getUser(userId, password);
			if(user != null) {
				break;
			}
		}
		if(userId == null || password == null) {
			Log.info("Null exception.");
			throw new WebApplicationException( Status.BAD_REQUEST );
		}

		if(user == null) {
			for(URI uri: DirectoriesServer.foundURI("users")) {
				user = new RestUsersClient(uri).getUserWithoutPassword(userId);
				if(user != null) {
					break;
				}
			}
			if(user == null) { 
				Log.info("User does not exist.");
				throw new WebApplicationException( Status.NOT_FOUND);}
			else {
				Log.info("Wrong Password.");
				throw new WebApplicationException( Status.FORBIDDEN);
			}
		}
		List<FileInfo> list = files.get(userId);
		if(list == null) {
			list = new ArrayList<FileInfo>();
		}
		Set<String> canRead = new HashSet<>();
		canRead.add(userId);
		URI fileURI = null;
		for(URI uri : DirectoriesServer.foundURI("files")) {
			new RestFilesClient(uri).writeFile(userId+":"+filename,data,"");
			fileURI = uri;
			break;
		}
		FileInfo f = new FileInfo(userId, filename,fileURI.toString() + "/files/" + userId+":"+filename, canRead);
		list.add(f);
		files.put(userId, list);
		return f;


	}

	@Override
	public void deleteFile(String filename, String userId, String password) {
		for(FileInfo f : files.get(userId)) {
			if(f.getFilename().equals(filename)) {
				files.get(userId).remove(f);
				return;
			}
		}

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
		User user = null;
		for(URI uri: DirectoriesServer.foundURI("users")) {
			user = new RestUsersClient(uri).getUser(accUserId, password);
			if(user != null) {
				break;
			}
		}


		if(user == null) {
			Log.info("User does not exist or has invalid password.");
			for(URI uri: DirectoriesServer.foundURI("users")) {
				user = new RestUsersClient(uri).getUserWithoutPassword(accUserId);
				if(user != null) {
					break;
				}
			}
			if(user == null) throw new WebApplicationException( Status.NOT_FOUND);
		}
		
		if(!user.getPassword().equals(password)) {
			Log.info("Wrong Password.");
			throw new WebApplicationException( Status.FORBIDDEN);
		}
		if(userId == null || password == null || accUserId == null || filename == null) {
			Log.info("Null exception.");
			throw new WebApplicationException( Status.BAD_REQUEST );
		}
		String fileURL ="";
		List<FileInfo> infos = files.get(userId);
		for(FileInfo f: infos) {
			if(f.getFilename().equals(filename)) {
				fileURL = f.getFileURL();
				break;
			}
		}
		throw new WebApplicationException( 
				Response.temporaryRedirect(URI.create(fileURL +  "?token=")).build());

	}

	@Override
	public List<FileInfo> lsFile(String userId, String password) {
		// TODO Auto-generated method stub
		return null;
	}

}
