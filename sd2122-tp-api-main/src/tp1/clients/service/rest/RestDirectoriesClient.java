package tp1.clients.service.rest;

import java.net.URI;
import java.util.List;

import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import tp1.api.FileInfo;
import tp1.api.User;
import tp1.api.service.rest.RestDirectory;
import tp1.api.service.rest.RestUsers;

public class RestDirectoriesClient extends RestClient implements RestDirectory {
	final WebTarget target;
	RestDirectoriesClient(URI serverURI) {
		super(serverURI);
		target = client.target( serverURI ).path( RestDirectory.PATH );
	}

	@Override
	public FileInfo writeFile(String filename, byte[] data, String userId, String password) {
		return super.reTry(() -> {
			return clt_writeFile(filename,data,userId,password);
		});
	}

	private FileInfo clt_writeFile(String filename, byte[] data, String userId, String password) {
		// TODO Auto-generated method stub
		Response r = target.path(userId)
				.path(filename)
				.queryParam(RestUsers.PASSWORD, password).request()
				.accept(MediaType.APPLICATION_OCTET_STREAM)
				.post(Entity.entity(data, MediaType.APPLICATION_OCTET_STREAM));
		
		if( r.getStatus() == Status.OK.getStatusCode() && r.hasEntity() ) {
			System.out.println("Success:");
			FileInfo f = r.readEntity(FileInfo.class);
			System.out.println( "File " + f);
			return f;
		} else
			System.out.println("Error, HTTP error status: " + r.getStatus() );
		return null;
	}

	@Override
	public void deleteFile(String filename, String userId, String password) {
		//return super.reTry(() -> {
		//	return clt_deleteFile(filename,userId,password);
		//});

	}

	private void clt_deleteFile(String filename, String userId, String password) {
		// TODO Auto-generated method stub
		//return;
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
		return super.reTry(() -> {
			return clt_getFile(filename,userId,accUserId,password);
		});
		
		
	}

	private byte[] clt_getFile(String filename, String userId, String accUserId, String password) {
		Response r = target.path(userId)
				.path(filename)
				.queryParam(RestUsers.USER_ID, accUserId)
				.queryParam(RestUsers.PASSWORD, password).request()
				.accept(MediaType.APPLICATION_OCTET_STREAM)
				.get();
		
		if( r.getStatus() == Status.OK.getStatusCode() && r.hasEntity() ) {
			System.out.println("Success:");
			byte[] data = r.readEntity(byte[].class);
			System.out.println("Data: " + data);
			return data;
		} else
			System.out.println("Error, HTTP error status: " + r.getStatus() );
		return null;
	}

	@Override
	public List<FileInfo> lsFile(String userId, String password) {
		// TODO Auto-generated method stub
		return null;
	}

}
