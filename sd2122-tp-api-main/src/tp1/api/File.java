package tp1.api;

public class File {
	private String fileId;
	private byte[] data;
	private String token;
	public File(String fileId, byte[] data, String token) {
		this.fileId = fileId;
		this.data = data;
		this.token = token;
	}
	public byte[] getData() {
		return this.data;
	}
	public String getToken() {
		return token;
	}
	public String getFileId() {
		return fileId;
	}
}
