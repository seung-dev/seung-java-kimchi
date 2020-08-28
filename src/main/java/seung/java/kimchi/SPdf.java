package seung.java.kimchi;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;

public class SPdf {

	public SPdf() {}
	
	public static int toPDF(
			byte[] data
			, String path
			) throws IOException {
		return toPDF(data, path, null, null);
	}
	
	public static int toPDF(
			byte[] data
			, String path
			, String ownerPassword
			, String userPassword
			) throws IOException {
		
		int result = 0;
		
		try(
				PDDocument pdDocument = PDDocument.load(data);
				) {
			if(ownerPassword != null
					&& ownerPassword.length() > 0
					&& userPassword != null
					&& userPassword.length() > 0
					) {
				AccessPermission accessPermission = new AccessPermission();
				StandardProtectionPolicy standardProtectionPolicy = new StandardProtectionPolicy(ownerPassword, userPassword, accessPermission);
				standardProtectionPolicy.setEncryptionKeyLength(256);
				pdDocument.protect(standardProtectionPolicy);
			}
			pdDocument.save(path);
			result = 1;
		} catch (IOException e) {
			throw e;
		}
		
		return result;
	}
	
	public static byte[] toPDF(byte[] data) throws IOException {
		return toPDF(data, null, null);
	}
	
	public static byte[] toPDF(
			byte[] data
			, String ownerPassword
			, String userPassword
			) throws IOException {
		
		byte[] pdf = null;
		
		try(
				PDDocument pdDocument = PDDocument.load(data);
				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				) {
			if(ownerPassword != null
					&& ownerPassword.length() > 0
					&& userPassword != null
					&& userPassword.length() > 0
					) {
				AccessPermission accessPermission = new AccessPermission();
				StandardProtectionPolicy standardProtectionPolicy = new StandardProtectionPolicy(ownerPassword, userPassword, accessPermission);
				standardProtectionPolicy.setEncryptionKeyLength(256);
				pdDocument.protect(standardProtectionPolicy);
			}
			pdDocument.save(byteArrayOutputStream);
			pdf = byteArrayOutputStream.toByteArray();
		} catch (IOException e) {
			throw e;
		}
		
		return pdf;
	}
}
