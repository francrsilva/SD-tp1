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
		//canRead.add(userId);
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
		User user = null;
		for(URI uri : DirectoriesServer.foundURI("users")) {
			user = new RestUsersClient(uri).getUserWithoutPassword(userId);
		}
		if(user == null) {
			Log.info("User does not exist.");
			throw new WebApplicationException( Status.NOT_FOUND);
		}
		List<FileInfo> fList = files.get(userId);
		if(fList == null) {
			Log.info("File does not exist.");
			throw new WebApplicationException( Status.NOT_FOUND);
		}
		FileInfo fileInfo = null;
		for(FileInfo f : fList) {
			if(f.getFilename().equals(filename)) {
				fileInfo = f;
			}
		}
		if(fileInfo == null) {
			Log.info("File does not exist.");
			throw new WebApplicationException( Status.NOT_FOUND);
		}
		if(!user.getPassword().equals(password)) {
			Log.info("Wrong Password.");
			throw new WebApplicationException( Status.FORBIDDEN);
		}
		for(URI uri : DirectoriesServer.foundURI("files")) {
			RestFilesClient rfc = new RestFilesClient(uri);
			byte[] data = rfc.getFile(userId+":"+filename, "");
			if(data != null) {
				files.get(userId).remove(fileInfo);
				rfc.deleteFile(userId+":"+filename,"");
				throw new WebApplicationException(Status.NO_CONTENT);
			}
		}
		throw new WebApplicationException(Status.BAD_REQUEST);

	}

	@Override
	public void shareFile(String filename, String userId, String userIdShare, String password) {
		User user1 = null;
		User user2 = null;
		for(URI uri : DirectoriesServer.foundURI("users")) {
			if(user1 == null) {
				user1 = new RestUsersClient(uri).getUserWithoutPassword(userId);
			}
			if(user2 == null) {
				user2 = new RestUsersClient(uri).getUserWithoutPassword(userIdShare);
			}
		}
		if(user1 == null || user2 == null) {
			Log.info("User does not exist.");
			throw new WebApplicationException( Status.NOT_FOUND);
		}

		List<FileInfo> fList = files.get(userId);
		if(fList == null) {
			Log.info("File does not exist.");
			throw new WebApplicationException( Status.NOT_FOUND);
		}
		FileInfo fileInfo = null;
		for(FileInfo f : fList) {
			if(f.getFilename().equals(filename)) {
				fileInfo = f;
			}
		}
		if(fileInfo == null) {
			Log.info("File does not exist.");
			throw new WebApplicationException( Status.NOT_FOUND);
		}
		if(!user1.getPassword().equals(password)) {
			Log.info("Wrong Password.");
			throw new WebApplicationException( Status.FORBIDDEN);
		}
		Set<String> newSet = fileInfo.getSharedWith();
		newSet.add(userIdShare);
		fileInfo.setSharedWith(newSet);

	}

	@Override
	public void unshareFile(String filename, String userId, String userIdShare, String password) {
		User user1 = null;
		User user2 = null;
		for(URI uri : DirectoriesServer.foundURI("users")) {
			if(user1 == null) {
				user1 = new RestUsersClient(uri).getUserWithoutPassword(userId);
			}
			if(user2 == null) {
				user2 = new RestUsersClient(uri).getUserWithoutPassword(userIdShare);
			}
		}
		if(user1 == null || user2 == null) {
			Log.info("User does not exist.");
			throw new WebApplicationException( Status.NOT_FOUND);
		}

		List<FileInfo> fList = files.get(userId);
		if(fList == null) {
			Log.info("File does not exist.");
			throw new WebApplicationException( Status.NOT_FOUND);
		}
		FileInfo fileInfo = null;
		for(FileInfo f : fList) {
			if(f.getFilename().equals(filename)) {
				fileInfo = f;
			}
		}
		if(fileInfo == null) {
			Log.info("File does not exist.");
			throw new WebApplicationException( Status.NOT_FOUND);
		}
		if(!user1.getPassword().equals(password)) {
			Log.info("Wrong Password.");
			throw new WebApplicationException( Status.FORBIDDEN);
		}
		Set<String> newSet = fileInfo.getSharedWith();
		boolean canRemove = false;
		for(String s : newSet) {
			if(s.equals(userIdShare)) {
				canRemove = true;
				//newSet.remove(s);
				//fileInfo.setSharedWith(newSet);
			}
		}
		if(canRemove) {
			newSet.remove(userIdShare);
			fileInfo.setSharedWith(newSet);
			Log.info("Success.");
			throw new WebApplicationException( Status.NO_CONTENT);
		}
		else {
			Log.info("Bad Request.");
			throw new WebApplicationException( Status.BAD_REQUEST);
		}
	}

	@Override
	public byte[] getFile(String filename, String userId, String accUserId, String password) {
		User accUser = null;
		User user = null;
		for(URI uri: DirectoriesServer.foundURI("users")) {
			RestUsersClient ruc = new RestUsersClient(uri);
			accUser = ruc.getUser(accUserId, password);
			user = ruc.getUserWithoutPassword(userId);
			if(accUser != null && user != null) {
				break;
			}
		}
		if(user == null) {
			throw new WebApplicationException( Status.NOT_FOUND);
		}
		if(accUser == null) {
			Log.info("User does not exist or has invalid password.");
			for(URI uri: DirectoriesServer.foundURI("users")) {
				accUser = new RestUsersClient(uri).getUserWithoutPassword(accUserId);
				if(accUser != null) {
					break;
				}
			}
			if(accUser == null) throw new WebApplicationException( Status.NOT_FOUND);
		}
		if(!accUser.getPassword().equals(password)) {
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
				if(!userId.equals(accUserId) && !f.getSharedWith().contains(accUserId)) {
					Log.info("Cannot Access File.");
					throw new WebApplicationException( Status.FORBIDDEN);
				}
				break;
			}
		}
		throw new WebApplicationException( 
				Response.temporaryRedirect(URI.create(fileURL +  "?token=")).build());
	}

	@Override
	public List<FileInfo> lsFile(String userId, String password) {
		User user = null;
		for(URI uri : DirectoriesServer.foundURI("users")) {
			user = new RestUsersClient(uri).getUserWithoutPassword(userId);
		}
		if(user == null) {
			Log.info("User does not exist.");
			throw new WebApplicationException( Status.NOT_FOUND);
		}
		if(!user.getPassword().equals(password)) {
			Log.info("Wrong Password.");
			throw new WebApplicationException( Status.FORBIDDEN);
		}
		List<FileInfo> toReturn = new ArrayList<>();
		Set<String> users = files.keySet();
		for(String u : users) {
			List<FileInfo> userFiles = files.get(u);
			for(FileInfo f : userFiles) {
				Set<String> canReadFile = f.getSharedWith();
				if(canReadFile.contains(userId) || userId.equals(u)) {
					toReturn.add(f);
				}
			}
		}
		return toReturn;
	}


	@Override
	public void deleteAllUserFiles(String userId) {
		List<FileInfo> list = files.get(userId);
		if(list == null) {
			throw new WebApplicationException( Status.NO_CONTENT);
		}
		for(URI uri: DirectoriesServer.foundURI("files")) {
			for(FileInfo fInfo : list) {
				RestFilesClient rfc = new RestFilesClient(uri);
				rfc.deleteFile(userId + ":" + fInfo.getFilename(), "");
			}
		}
		files.remove(userId);
		throw new WebApplicationException( Status.NO_CONTENT);

	}

}
