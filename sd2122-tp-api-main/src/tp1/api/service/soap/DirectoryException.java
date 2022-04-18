package tp1.api.service.soap;

import jakarta.xml.ws.WebFault;

@WebFault
public class DirectoryException extends Exception {

	private static final long serialVersionUID = 1L;

	public DirectoryException() {
		super("");
	}

	public DirectoryException(String errorMessage) {
		super(errorMessage);
	}
}
