package seung.java.kimchi;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.FileUtils;

import seung.java.kimchi.util.SKimchiException;

/**
 * <pre>
 * File 관련 함수 모음
 * </pre>
 * 
 * @author seung
 */
public class SFile {

	private SFile() {}
	
	public static int toFile(ZipInputStream zipInputStream, String path, int size) {
		
		int result = -1;
		try(
				FileOutputStream fileOutputStream = new FileOutputStream(path);
				BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
				) {
			byte[] b = new byte[size];
			int read;
			while((read = zipInputStream.read(b)) > 0) {
				bufferedOutputStream.write(b, 0, read);
			}
		} catch (FileNotFoundException e) {
			result = 0;
		} catch (IOException e) {
			result = 0;
		} finally {
			if(new File(path).exists()) {
				result = 1;
			}
		}
		
		return result;
	}
	
	public static byte[] unzipSingleTextFile(byte[] zip) throws SKimchiException {
		
		byte[] unzip = null;
		
		try(
			ZipInputStream zipInputStream = new ZipInputStream(new ByteArrayInputStream(zip));
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		) {
			ZipEntry zipEntry = zipInputStream.getNextEntry();
			if(zipEntry != null && !zipEntry.isDirectory()) {
				byte[] b = new byte[1024];
				int len;
				while((len = zipInputStream.read(b)) > 0) {
					byteArrayOutputStream.write(b, 0, len);
				}
				unzip = byteArrayOutputStream.toByteArray();
				byteArrayOutputStream.close();
			}
			zipInputStream.closeEntry();
			zipInputStream.close();
		} catch (IOException e) {
			throw new SKimchiException(e);
		}
		
		return unzip;
	}
	
	public static String getContentDisposition(String userAgent, String fileName) {
		String format = "attachment; filename=%s";
		StringBuffer encodedFileName = new StringBuffer();
		try {
			switch(getBrowser(userAgent)) {
			case "MSIE":
				encodedFileName.append("\"");
				encodedFileName.append(URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20"));
				encodedFileName.append("\"");
				break;
			case "Chrome":
				encodedFileName.append("\"");
				for(int i = 0; i < fileName.length(); i++) {
					char c = fileName.charAt(i);
					if(c > '~') {
						encodedFileName.append(URLEncoder.encode("" + c, "UTF-8"));
					} else {
						encodedFileName.append(c);
					}
				}
				encodedFileName.append("\"");
				break;
			case "Firefox":
			case "Opera":
				encodedFileName.append("\"");
				encodedFileName.append(new String(fileName.getBytes("UTF-8"), "8859_1"));
				encodedFileName.append("\"");
				break;
			default:
				break;
			}
		} catch (UnsupportedEncodingException e) {
		}
		return String.format(format, encodedFileName.toString());
	}
	
	public static String getContentType(String userAgent) {
		String contentType = "application/download; utf-8";
		switch(getBrowser(userAgent)) {
			case "MSIE":
			case "Chrome":
			case "Firefox":
				break;
			case "Opera":
				contentType = "application/octet-stream; charset=UTF-8";
				break;
			default:
				break;
		}
		return contentType;
	}
	
	public static String getBrowser(String userAgent) {
		if(userAgent.indexOf("MSIE") > -1) {
			return "MSIE";
		} else if(userAgent.indexOf("Trident") > -1) {
			return "MSIE";
		} else if(userAgent.indexOf("Chrome") > -1) {
			return "Chrome";
		} else if(userAgent.indexOf("Opera") > -1) {
			return "Opera";
		}
		return "Firefox";
	}
	
	public static String fileToString(
			String path
			, String encoding
			) throws IOException {
		File file = new File(path);
		if(file.exists()) {
			return FileUtils.readFileToString(file, encoding);
		}
		return null;
	}
	
	public static byte[] fileToByteArray(
			String path
			) throws IOException {
		File file = new File(path);
		if(file.exists()) {
			return FileUtils.readFileToByteArray(file);
		}
		return null;
	}
	
	public static int toFile(
			String path
			, byte[] data
			) {
		
		int exists = 0;
		
		File newFile = new File(path);
		try {
			FileUtils.writeByteArrayToFile(newFile, data);
			if(newFile.exists()) {
				exists = 1;
			}
		} catch (IOException e) {
			if(newFile.exists()) {
				exists = -1;
			}
		}
		
		return exists;
	}
	
}
