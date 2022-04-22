package tp1.server.service.rest.resources;

import java.net.InetAddress;
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
			Log.info("User does not exist.");
			throw new WebApplicationException( Status.NOT_FOUND );
		}
		else {
			if(!user.getPassword().equals(password)) {
				Log.info("Wrong Password.");
				throw new WebApplicationException( Status.FORBIDDEN);
			}
			List<FileInfo> list = files.get(userId);
			if(list == null) {
				list = new ArrayList<FileInfo>();
			}
			Set<String> canRead = new HashSet<>();
			canRead.add(userId);
			FileInfo f = new FileInfo(userId, filename, userId+"/"+filename, canRead);
			list.add(f);
			for(URI uri : DirectoriesServer.foundURI("files")) {
				new RestFilesClient(uri).writeFile(userId+":"+filename,data,"");
				break;
			}
			return f;
		}

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
		if(userId == null || password == null || accUserId == null || filename == null) {
			Log.info("Null exception.");
			throw new WebApplicationException( Status.BAD_REQUEST );
		}

		if(user == null) {
			Log.info("User does not exist.");
			throw new WebApplicationException( Status.NOT_FOUND );
		}
		if(!user.getPassword().equals(password)) {
			Log.info("Wrong Password.");
			throw new WebApplicationException( Status.FORBIDDEN);
		}
		/*URI filesUri = null;
		for(URI uri: DirectoriesServer.foundURI("files")) {
			filesUri = uri;
		}*/
		String uri = "http://172.18.0.5:8080/rest/files/" + userId + ":" + filename + "/?token=";
		Log.info(uri);
		System.out.println(uri);
		throw new WebApplicationException(
				Response.temporaryRedirect(URI.create(uri)).build());
		/*byte[] data = null;
		for(URI uri : DirectoriesServer.foundURI("files")) {
			data = new RestFilesClient(uri).getFile(userId+":"+filename, "");
			if(data != null) {
				break;
			}
		}
		if(data == null) {
			Log.info("File does not exist.");
			throw new WebApplicationException( Status.NOT_FOUND );
		}
		else {
			return data;
		}*/
	}

	@Override
	public List<FileInfo> lsFile(String userId, String password) {
		// TODO Auto-generated method stub
		return null;
	}

}
