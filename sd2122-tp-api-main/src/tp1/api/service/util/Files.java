package tp1.api.service.util;


public interface Files {

	/**
	 * Write a file. If the file exists, overwrites the contents.
	 * 
	 * @param fileId - unique id of the file. 
	 * @param token - token for accessing the file server (in the first 
	 * project this will not be used).
     *
	 * @return OK if success.
	 *         FORBIDDEN if the token is incorrect.
	 * 	   BAD_REQUEST otherwise.
	 */
	Result<Void> writeFile(String fileId, byte[] data, String token);

	/**
	 * Delete an existing file.
	 * 
	 * @param fileId - unique id of the file. 
	 * @param token - token for accessing the file server (in the first 
	 * project this will not be used).
	 * 
	 * @return OK if success; 
	 *	   NOT_FOUND if the fileId does not exist.
	 *         FORBIDDEN if the token is incorrect.
	 * 	   BAD_REQUEST otherwise.
	 */
	Result<Void> deleteFile(String fileId, String token);

	/**
	 * Get the contents of the file. 
	 * 
	 * @param fileId - unique id of the file. 
	 * @param token - token for accessing the file server (in the first 
	 * project this will not be used).
	 * 
	 * @return OK if success + contents (through redirect to the File server); 
	 *	   NOT_FOUND if the uniqueId does not exist.
	 *         FORBIDDEN if the token is incorrect.
	 * 	   BAD_REQUEST otherwise.
	 */
	Result<byte[]> getFile(String fileId, String token);

}
