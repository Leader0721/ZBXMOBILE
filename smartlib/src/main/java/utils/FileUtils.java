package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.text.TextUtils;

/*
 * FileUtils copied from org.apache.commons.io.FileUtils
 */
public class FileUtils {

	/**
	 * 清空指定的文件夹，而不删除该文件夹
	 * 
	 * @param directory
	 *            directory to clean
	 * @throws IOException
	 *             in case cleaning is unsuccessful
	 */
	public static void cleanDirectory(File directory) throws IOException {
		if (!directory.exists()) {
			String message = directory + " does not exist";
			throw new IllegalArgumentException(message);
		}

		if (!directory.isDirectory()) {
			String message = directory + " is not a directory";
			throw new IllegalArgumentException(message);
		}

		File[] files = directory.listFiles();
		if (files == null) { // null if security restricted
			throw new IOException("Failed to list contents of " + directory);
		}

		IOException exception = null;
		for (File file : files) {
			try {
				forceDelete(file);
			} catch (IOException ioe) {
				exception = ioe;
			}
		}

		if (null != exception) {
			throw exception;
		}
	}

	/**
	 * Deletes a directory recursively.
	 * 
	 * @param directory
	 *            directory to delete
	 * @throws IOException
	 *             in case deletion is unsuccessful
	 */
	public static void deleteDirectory(File directory) throws IOException {
		if (!directory.exists()) {
			return;
		}

		cleanDirectory(directory);

		if (!directory.delete()) {
			String message = "Unable to delete directory " + directory + ".";
			throw new IOException(message);
		}
	}

	/**
	 * Deletes a file. If file is a directory, delete it and all
	 * sub-directories.
	 * <p>
	 * The difference between File.delete() and this method are:
	 * <ul>
	 * <li>A directory to be deleted does not have to be empty.</li>
	 * <li>You get exceptions when a file or directory cannot be deleted.
	 * (java.io.File methods returns a boolean)</li>
	 * </ul>
	 * 
	 * @param file
	 *            file or directory to delete, must not be {@code null}
	 * @throws NullPointerException
	 *             if the directory is {@code null}
	 * @throws FileNotFoundException
	 *             if the file was not found
	 * @throws IOException
	 *             in case deletion is unsuccessful
	 */
	public static void forceDelete(File file) throws IOException {
		if (file.isDirectory()) {
			deleteDirectory(file);
		} else {
			boolean filePresent = file.exists();
			if (!file.delete()) {
				if (!filePresent) {
					throw new FileNotFoundException("File does not exist: "
							+ file);
				}
				String message = "Unable to delete file: " + file;
				throw new IOException(message);
			}
		}
	}

	/**
	 * Deletes a file, never throwing an exception. If file is a directory,
	 * delete it and all sub-directories.
	 * <p>
	 * The difference between File.delete() and this method are:
	 * <ul>
	 * <li>A directory to be deleted does not have to be empty.</li>
	 * <li>No exceptions are thrown when a file or directory cannot be deleted.</li>
	 * </ul>
	 * 
	 * @param file
	 *            file or directory to delete, can be {@code null}
	 * @return {@code true} if the file or directory was deleted, otherwise
	 *         {@code false}
	 * 
	 */
	public static boolean deleteQuietly(File file) {
		if (file == null) {
			return false;
		}
		try {
			if (file.isDirectory()) {
				cleanDirectory(file);
			}
		} catch (Exception ignored) {
		}

		try {
			return file.delete();
		} catch (Exception ignored) {
			return false;
		}
	}

	/**
	 * Makes a directory, including any necessary but nonexistent parent
	 * directories. If a file already exists with specified name but it is not a
	 * directory then an IOException is thrown. If the directory cannot be
	 * created (or does not already exist) then an IOException is thrown.
	 * 
	 * @param directory
	 *            directory to create, must not be {@code null}
	 * @throws NullPointerException
	 *             if the directory is {@code null}
	 * @throws IOException
	 *             if the directory cannot be created or the file already exists
	 *             but is not a directory
	 */
	public static void forceMkdir(File directory) throws IOException {
		if (directory.exists()) {
			if (!directory.isDirectory()) {
				String message = "File " + directory + " exists and is "
						+ "not a directory. Unable to create directory.";
				throw new IOException(message);
			}
		} else {
			if (!directory.mkdirs()) {
				// Double-check that some other thread or process hasn't made
				// the directory in the background
				if (!directory.isDirectory()) {
					String message = "Unable to create directory " + directory;
					throw new IOException(message);
				}
			}
		}
	}

	/**
	 * Returns the size of the specified file or directory. If the provided
	 * {@link File} is a regular file, then the file's length is returned. If
	 * the argument is a directory, then the size of the directory is calculated
	 * recursively. If a directory or subdirectory is security restricted, its
	 * size will not be included.
	 * 
	 * @param file
	 *            the regular file or directory to return the size of (must not
	 *            be {@code null}).
	 * 
	 * @return the length of the file, or recursive size of the directory,
	 *         provided (in bytes).
	 * 
	 * @throws NullPointerException
	 *             if the file is {@code null}
	 * @throws IllegalArgumentException
	 *             if the file does not exist.
	 * 
	 */
	public static long sizeOf(File file) throws IllegalArgumentException {

		if (!file.exists()) {
			String message = file + " does not exist";
			throw new IllegalArgumentException(message);
		}

		if (file.isDirectory()) {
			return sizeOfDirectory(file);
		} else {
			return file.length();
		}

	}

	/**
	 * Counts the size of a directory recursively (sum of the length of all
	 * files).
	 * 
	 * @param directory
	 *            directory to inspect, must not be {@code null}
	 * @return size of directory in bytes, 0 if directory is security
	 *         restricted, a negative number when the real total is greater than
	 *         {@link Long#MAX_VALUE}.
	 * @throws NullPointerException
	 *             if the directory is {@code null}
	 */
	public static long sizeOfDirectory(File directory) {
		if (!directory.exists() || !directory.isDirectory())
			return 0L;

		final File[] files = directory.listFiles();
		if (files == null) { // null if security restricted
			return 0L;
		}
		long size = 0;

		for (final File file : files) {
			size += sizeOf(file);
			if (size < 0) {
				break;
			}
		}

		return size;
	}

	/**
	 * 删除当前文件，若当前文件问文件夹，则删除该文件夹和文件夹下的文件
	 * 
	 * @param folder
	 * @throws Throwable
	 * @author GISirFive
	 */
	public static void deleteFileAndFolder(File folder) throws Throwable {
		if ((folder == null) || (!(folder.exists())))
			return;
		if (folder.isFile()) {
			folder.delete();
			return;
		}
		String[] names = folder.list();
		if ((names == null) || (names.length <= 0)) {
			folder.delete();
			return;
		}
		for (String name : names) {
			File f = new File(folder, name);
			if (f.isDirectory())
				deleteFileAndFolder(f);
			else
				f.delete();
		}
		folder.delete();
	}

	/**
	 * 复制文件
	 * 
	 * @param oldPath
	 *            String 原文件路径 如：c:/fqf.txt
	 * @param newPath
	 *            String 复制后路径 如：f:/fqf.txt
	 * @return boolean
	 */
	public static boolean copyFile(String fromFilePath, String toFilePath) {
		if ((TextUtils.isEmpty(fromFilePath))
				|| (TextUtils.isEmpty(toFilePath)))
			return false;
		if (!(new File(fromFilePath).exists()))
			return false;
		try {
			File target = new File(toFilePath);
			if (!target.getParentFile().exists())
				target.getParentFile().mkdirs();
			if (target.exists())
				target.delete();
			target.createNewFile();
			FileInputStream fisfrom = new FileInputStream(fromFilePath);
			FileOutputStream fosto = new FileOutputStream(toFilePath);
			copyFile(fisfrom, fosto);
		} catch (Throwable e) {
			return false;
		}
		return true;
	}

	/**
	 * 复制文件
	 * 
	 * @param src
	 * @param dst
	 * @throws Throwable
	 * @author GISirFive
	 */
	public static void copyFile(FileInputStream src, FileOutputStream dst)
			throws Throwable {
		byte[] buf = new byte[65536];
		for (int len = src.read(buf); len > 0; len = src.read(buf))
			dst.write(buf, 0, len);
		src.close();
		dst.close();
	}

}