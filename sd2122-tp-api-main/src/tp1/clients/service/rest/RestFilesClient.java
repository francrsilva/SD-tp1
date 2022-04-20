package tp1.clients.service.rest;

import java.net.URI;

import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import tp1.api.File;
import tp1.api.service.rest.RestFiles;
import tp1.api.service.rest.RestUsers;

public class RestFilesClient extends RestClient implements RestFiles {

	final WebTarget target;

	RestFilesClient(URI serverURI) {
		super(serverURI);
		target = client.target( serverURI ).path( RestUsers.PATH );
	}

	@Override
	public void writeFile(String fileId, byte[] data, String token) {
		super.reTry( () -> {
			return clt_writeFile(fileId,data,token);
		});
	}

	private File clt_writeFile(String fileId, byte[] data, String token) {
		Response r = target.path(fileId)
				.queryParam(RestFiles.TOKEN, token)
				.request()
				.accept(MediaType.APPLICATION_OCTET_STREAM)
				.post(Entity.entity(data, MediaType.APPLICATION_OCTET_STREAM));

		if( r.getStatus() == Status.OK.getStatusCode() && r.hasEntity() )
			return r.readEntity(File.class);
		else 
			System.out.println("Error, HTTP error status: " + r.getStatus() );
		
		return null;
	}
	@Override
	public void deleteFile(String fileId, String token) {
		// TODO Auto-generated method stub

	}

	@Override
	public byte[] getFile(String fileId, String token) {
		// TODO Auto-generated method stub
		return null;
	}

}
