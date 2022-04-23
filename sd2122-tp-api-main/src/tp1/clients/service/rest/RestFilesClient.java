package tp1.clients.service.rest;

import java.net.URI;

import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import tp1.api.service.rest.RestFiles;
public class RestFilesClient extends RestClient implements RestFiles {

	final WebTarget target;

	public RestFilesClient(URI serverURI) {
		super(serverURI);
		target = client.target( serverURI ).path( RestFiles.PATH );
	}

	@Override
	public void writeFile(String fileId, byte[] data, String token) {
		super.reTry( () -> {
			return clt_writeFile(fileId,data,token);
		});
	}

	private String clt_writeFile(String fileId, byte[] data, String token) {
		Response r = target.path(fileId)
				.queryParam(RestFiles.TOKEN, token)
				.request()
				.accept(MediaType.APPLICATION_JSON)
				.post(Entity.entity(data, MediaType.APPLICATION_OCTET_STREAM));

		if( r.getStatus() == Status.NO_CONTENT.getStatusCode())
			return "";
		else 
			System.out.println("Error, HTTP error status: " + r.getStatus());

		return null;
	}
	@Override
	public void deleteFile(String fileId, String token) {
		super.reTry( () -> {
			return clt_deleteFile(fileId,token);
		});

	}

	private String clt_deleteFile(String fileId, String token) {
		Response r = target.path(fileId)
				.queryParam(RestFiles.TOKEN, token)
				.request()
				.delete();

		if( r.getStatus() == Status.NO_CONTENT.getStatusCode() )
			return "";
		else 
			System.out.println("Error, HTTP error status: " + r.getStatus() );

		return null;
	}

	@Override
	public byte[] getFile(String fileId, String token) {
		return super.reTry( () -> {
			return clt_getFile(fileId,token);
		});
	}

	private byte[] clt_getFile(String fileId, String token) {
		Response r = target.path(fileId)
				.queryParam(RestFiles.TOKEN, token)
				.request()
				.accept(MediaType.APPLICATION_OCTET_STREAM)
				.get();

		if( r.getStatus() == Status.OK.getStatusCode() && r.hasEntity() ) {
			System.out.println("Success:");
			byte[] data = r.readEntity(byte[].class);
			System.out.println( "Data " + data);
			return data;
		}
		else 
			System.out.println("Error, HTTP error status: " + r.getStatus() + " " + fileId);

		return null;
	}

}
