package tp1.api.service.soap;

import jakarta.xml.ws.WebFault;

@WebFault
public class FilesException extends Exception {

	private static final long serialVersionUID = 1L;

	public FilesException() {
		super("");
	}

	public FilesException(String errorMessage ) {
		super(errorMessage);
	}
}
