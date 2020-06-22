package seung.java.kimchi;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
	
}
