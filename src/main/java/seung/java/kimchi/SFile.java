package seung.java.kimchi;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

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
	
}
