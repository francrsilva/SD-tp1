package tp1.server.service.rest.resources;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import jakarta.inject.Singleton;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response.Status;
import tp1.api.File;
import tp1.api.service.rest.RestFiles;

@Singleton
public class FilesResource implements RestFiles {
	private final Map<String, File> files = new HashMap<>();
	private static Logger Log = Logger.getLogger(FilesResource.class.getName());
	public FilesResource() {
		
	}
	
	@Override
	public void writeFile(String fileId, byte[] data, String token) {
		Log.info("Writing file with id: " + fileId);
		if(token == null) {
			Log.info("Invalid token.");
			throw new WebApplicationException( Status.FORBIDDEN );
		}
		if(fileId == null || data == null || token == null) {
			Log.info("All null.");
			throw new WebApplicationException( Status.BAD_REQUEST );
		}
		File f = files.get(fileId);
		f = new File(fileId,data,token);
		files.put(fileId, f);
		throw new WebApplicationException( Status.NO_CONTENT );
	}

	@Override
	public void deleteFile(String fileId, String token) {
		if(token == null) {
			Log.info("Invalid token.");
			throw new WebApplicationException( Status.FORBIDDEN );
		}
		File f = files.get(fileId);
		if(f == null) {
			Log.info("File doesnt exist.");
			throw new WebApplicationException( Status.NOT_FOUND );
		}
		if(fileId == null || token == null) {
			Log.info("All null.");
			throw new WebApplicationException( Status.BAD_REQUEST );
		}
		files.remove(fileId);
	}

	@Override
	public byte[] getFile(String fileId, String token) {
		Log.info("Getting file: " + fileId);
		if(token == null) {
			Log.info("Invalid token.");
			throw new WebApplicationException( Status.FORBIDDEN + fileId + token );
		}
		if(fileId == null || token == null) {
			Log.info("All null.");
			throw new WebApplicationException( Status.BAD_REQUEST );
		}
		File f = files.get(fileId);
		if(f == null) {
			Log.info("File doesnt exist.");
			throw new WebApplicationException( Status.NOT_FOUND );
		}
		return f.getData();
	}

}
