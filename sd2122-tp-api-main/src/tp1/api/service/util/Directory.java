package tp1.api.service.util;

import java.util.*;

import tp1.api.FileInfo;

public interface Directory {

	/**
	 * Write a new version of a file. If the file exists, its contents are overwritten.
	 * Only the owner (userId) can write the file.
	 * 
	 * A file resource will has the full path "userId/filename".
	 * 
	 * @param filename - name of the file.
	 * @param data - contents of the file.
	 * @param userId - id of the user.
	 * @param password - the password of the user.
	 * 
	 * @return OK if success + FileInfo representing the file.
	 *	   NOT_FOUND if the userId does not exist.
	 *         FORBIDDEN if the password is incorrect.
	 * 	   BAD_REQUEST otherwise.
	 */
	Result<FileInfo> writeFile(String filename, byte []data, String userId, String password);

	/**
	 * Delete an existing file ("userId/filename"). 
	 * Only the owner (userId) can delete the file.
	 * 
	 * @param filename - name of the file.
	 * @param userId - id of the user.
	 * @param password - the password of the user.
	 * 
	 * @return OK if success; 
	 *	   NOT_FOUND if the userId or filename does not exist.
	 *         FORBIDDEN if the password is incorrect.
	 * 	   BAD_REQUEST otherwise.
	 */
	Result<Void> deleteFile(String filename, String userId, String password);

	/**
	 * Share the file "userId/filename" with another user. 
	 * Only the owner (userId) can share the file.
	 * 
	 * The operation succeeds if and only if the userId, userIdShare and userId/filename
	 * exist and password is correct (note: if userIdShare already has access to the file,
	 * the operation still succeeds).
	 * 
	 * @param filename - name of the file.
	 * @param userId - id of the user.
	 * @param userIdShare - id of the user to share the file with.
	 * @param password - the password of the user.
	 * 
	 * @return OK if success; 
	 *.        NOT_FOUND if the userId or userIdShare or filename does not exist.
	 *         FORBIDDEN if the password is incorrect.
	 * 	   BAD_REQUEST otherwise.
	 */
	Result<Void> shareFile(String filename, String userId, String userIdShare, String password);

	/**
	 * Unshare the file "userId/filename" with another user. 
	 * Only the owner (userId) can unshare the file.
	 * 
	 * The operation succeeds if and only if the userId, userIdShare and userId/filename
	 * exist and password is correct (note: if userIdShare does not have access to the file,
	 * the operation still succeeds).
	 * 
	 * @param filename - name of the file.
	 * @param userId - id of the user.
	 * @param userIdShare - id of the user to unshare the file with.
	 * @param password - the password of the user.
	 * 
	 * @return OK if success; 
	 *	   NOT_FOUND if the userId or userIdShare or filename does not exist.
	 *         FORBIDDEN if the password is incorrect.
	 *         BAD_REQUEST otherwise.
	 */
	Result<Void> unshareFile(String filename, String userId, String userIdShare, String password);

	/**
	 * Get the contents of the file "userId/filename". 
	 * Who can read a file: the owner and the users with whom the file has been shared.
	 * 
	 * This operation should be implemented using HTTP redirect on success.
	 * 
	 * @param filename - name of the file.
	 * @param userId - id of the user.
	 * @param accUserId - id of the user executing the operation.
	 * @param password - the password of accUserId.
	 * 
	 * @return OK if success + contents (through redirect to the File server); 
	 *.        NOT_FOUND if the userId or filename or accUserId does not exist.
	 *         FORBIDDEN if the password is incorrect or the user cannot access the file.
	 * 	   BAD_REQUEST otherwise.
	 */
	Result<byte[]> getFile(String filename, String userId, String accUserId, String password);

	/**
	 * List the files a given user ("userId") has access to - this includes both its own files
	 * and the files shared with her. 
	 *  
	 * @param userId - id of the user.
	 * @param password - the password of the user.
	 * 
	 * @return OK if success + list of FileInfo; 
	 *	   NOT_FOUND if the userId does not exist.
	 *         FORBIDDEN if the password is incorrect.
	 *         BAD_REQUEST otherwise.
	 */
	Result<List<FileInfo>> lsFile(String userId, String password);
}
