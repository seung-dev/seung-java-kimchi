package seung.java.kimchi;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.util.Set;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

import seung.java.kimchi.util.SCharset;
import seung.java.kimchi.util.SKimchiException;

/**
 * <pre>
 * Convert 관련 함수 모음
 * </pre>
 * 
 * @author stoas
 */
public class SConvert {

	private SConvert() {}
	
	public static byte[] decompress(byte[] data) throws SKimchiException {
		return decompress(data, true);
	}
	public static byte[] decompress(byte[] data, boolean nowrap) throws SKimchiException {
		
		byte[] inflated = null;
		
		Inflater inflater = new Inflater(nowrap);
		
		inflater.setInput(data);
		
		byte[] b = new byte[1024];
		int len;
		try (
				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				) {
			
			while(!inflater.finished()) {
				len = inflater.inflate(b);
				byteArrayOutputStream.write(b, 0, len);
			}
			
			inflated = byteArrayOutputStream.toByteArray();
			inflater.end();
			
		} catch (IOException e) {
			throw new SKimchiException(e);
		} catch (DataFormatException e) {
			throw new SKimchiException(e);
		}
		
		return inflated;
	}
	
	public static byte[] compress(byte[] data) throws SKimchiException {
		return compress(data, Deflater.BEST_COMPRESSION, true);
	}
	public static byte[] compress(byte[] data, int level, boolean nowrap) throws SKimchiException {
		
		byte[] deflated = null;
		
		Deflater deflater = new Deflater(level, nowrap);
		
		deflater.setInput(data);
		deflater.finish();
		
		byte[] b = new byte[1024];
		int len;
		try (
				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				) {
			
			while(!deflater.finished()) {
				len = deflater.deflate(b);
				byteArrayOutputStream.write(b, 0, len);
			}
			
			deflated = byteArrayOutputStream.toByteArray();
			deflater.end();
			
		} catch (IOException e) {
			throw new SKimchiException(e);
		}
		
		return deflated;
	}
	
//	/**
//	 * @param compressed byte array
//	 * @throws SKimchiException 
//	 */
//	public static byte[] decompress(byte[] compressed) throws SKimchiException {
//		
//		byte[] decompressed = null;
//		try (
//				ByteArrayInputStream  byteArrayInputStream  = new ByteArrayInputStream(compressed);
//				GZIPInputStream	   gzipInputStream	   = new GZIPInputStream(byteArrayInputStream);
//				BufferedInputStream   bufferedInputStream   = new BufferedInputStream(gzipInputStream);
//				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//				) {
//			
//			byte[] b = new byte[1024];
//			int length;
//			while((length = bufferedInputStream.read(b)) != -1) {
//				byteArrayOutputStream.write(b, 0, length);
//			}
//			
//			byteArrayOutputStream.close();
//			bufferedInputStream.close();
//			gzipInputStream.close();
//			byteArrayInputStream.close();
//			
//			decompressed = byteArrayOutputStream.toByteArray();
//			
//		} catch (IOException e) {
//			throw new SKimchiException(e);
//		}
//		
//		return decompressed;
//	}
//	/**
//	 * @param data
//	 * @throws SKimchiException 
//	 */
//	public static byte[] compress(byte[] data) throws SKimchiException {
//		
//		byte[] compressed = null;
//		try (
//				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//				GZIPOutputStream	  gzipOutputStream	  = new GZIPOutputStream(byteArrayOutputStream);
//				BufferedOutputStream  bufferedOutputStream  = new BufferedOutputStream(gzipOutputStream);
//				) {
//			
//			bufferedOutputStream.write(data);
//			bufferedOutputStream.close();
//			gzipOutputStream.close();
//			byteArrayOutputStream.close();
//			
//			compressed = byteArrayOutputStream.toByteArray();
//			
//		} catch (IOException e) {
//			throw new SKimchiException(e);
//		}
//		
//		return compressed;
//	}
	
	/**
	 * <pre>
	 * default sCharset is {@link seung.java.kimchi.util.SCharset#UTF_8}.
	 * </pre>
	 * 
	 * @param algorithm {@link #availableDigestAlgorithm}
	 * @param data
	 * @return {@link #digestToBase64(String, String, SCharset)}
	 * @throws SKimchiException 
	 */
	public static String digestToBase64(String algorithm, String data) throws SKimchiException {
		return digestToBase64(algorithm, data, SCharset.UTF_8);
	}
	/**
	 * @param algorithm {@link #availableDigestAlgorithm}
	 * @param data
	 * @param sCharset {@link seung.java.kimchi.util.SCharset}
	 * @return {@link #encodeBase64String(byte[])} {@link #digest(String, String, SCharset)}
	 * @throws SKimchiException 
	 */
	public static String digestToBase64(String algorithm, String data, SCharset sCharset) throws SKimchiException {
		return encodeBase64String(digest(algorithm, data, sCharset));
	}
	
	/**
	 * <pre>
	 * default sCharset is {@link seung.java.kimchi.util.SCharset#UTF_8}.
	 * default toLowerCase is false.
	 * </pre>
	 * 
	 * @param algorithm {@link #availableDigestAlgorithm}
	 * @param data
	 * @return {@link #digestToHex(String, String, SCharset, boolean)}
	 * @throws SKimchiException 
	 */
	public static String digestToHex(String algorithm, String data) throws SKimchiException {
		return digestToHex(algorithm, data, SCharset.UTF_8, false);
	}
	/**
	 * @param algorithm {@link #availableDigestAlgorithm}
	 * @param data
	 * @param sCharset {@link seung.java.kimchi.util.SCharset}
	 * @param toLowerCase
	 * @return {@link #encodeHexString(byte[], boolean)} {@link #digest(String, String, SCharset)}
	 * @throws SKimchiException 
	 */
	public static String digestToHex(String algorithm, String data, SCharset sCharset, boolean toLowerCase) throws SKimchiException {
		return encodeHexString(digest(algorithm, data, sCharset), toLowerCase);
	}
	
	/**
	 * <pre>
	 * default sCharset is {@link seung.java.kimchi.util.SCharset#UTF_8}.
	 * </pre>
	 * 
	 * @param algorithm {@link #availableDigestAlgorithm}
	 * @param data
	 * @throws SKimchiException 
	 */
	public static byte[] digest(String algorithm, String data) throws SKimchiException {
		return digest(algorithm, data, SCharset.UTF_8);
	}
	/**
	 * @param algorithm {@link #availableDigestAlgorithm}
	 * @param data
	 * @param sCharset {@link seung.java.kimchi.util.SCharset}
	 * @return {@link #digest(String, byte[])}
	 * @throws SKimchiException 
	 */
	public static byte[] digest(String algorithm, String data, SCharset sCharset) throws SKimchiException {
		try {
			return digest(algorithm, data.getBytes(sCharset.text()));
		} catch (UnsupportedEncodingException e) {
			throw new SKimchiException(e);
		}
	}
	/**
	 * @param algorithm {@link #availableDigestAlgorithm}
	 * @param data
	 * @throws SKimchiException 
	 */
	public static byte[] digest(String algorithm, byte[] data) throws SKimchiException {
		try {
			MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
			return messageDigest.digest(data);
		} catch (NoSuchAlgorithmException e) {
			throw new SKimchiException(e);
		}
	}
	/**
	 * <pre>
	 * could add providers like "Security.addProvider(new BouncyCastleProvider())".
	 * </pre>
	 * 
	 * @return "SHA-384", "SHA-224", "SHA-256", "MD2", "SHA", "SHA-512", "MD5"
	 */
	public static Set<String> availableDigestAlgorithm() {
		return Security.getAlgorithms("MessageDigest");
	}
	
	/**
	 * @param data
	 * @param sCharset {@link seung.java.kimchi.util.SCharset}
	 * @throws SKimchiException 
	 */
	public static String[] encodeBinary(String data, SCharset sCharset) throws SKimchiException {
		int[]	decimalArray	  = encodeDecimal(data, sCharset);
		String[] binaryStringArray = new String[decimalArray.length];
		for(int i = 0; i < binaryStringArray.length; i++) {
			binaryStringArray[i] = Integer.toBinaryString(decimalArray[i]);
		}
		return binaryStringArray;
	}
	
	/**
	 * @param data
	 * @param sCharset {@link seung.java.kimchi.util.SCharset}
	 * @throws SKimchiException 
	 */
	public static int[] encodeDecimal(String data, SCharset sCharset) throws SKimchiException {
		String hexString = encodeHexString(data, sCharset, false);
		int[]  decimalArray = new int[hexString.length() / 2];
		for(int i = 0; i < decimalArray.length; i++) {
			decimalArray[i] = Integer.parseInt(hexString.substring(i * 2, i * 2 + 2), 16);
		}
		return decimalArray;
	}
	
	/**
	 * @param base64String
	 * @param sCharset {@link seung.java.kimchi.util.SCharset}
	 * @return {@link #decodeBase64(String)}
	 * @throws SKimchiException 
	 * @throws UnsupportedEncodingException
	 */
	public static String decodeBase64(String base64String, SCharset sCharset) throws SKimchiException {
		try {
			return new String(decodeBase64(base64String), sCharset.text());
		} catch (UnsupportedEncodingException e) {
			throw new SKimchiException(e);
		}
	}
	/**
	 * @see org.apache.commons.codec.binary.Base64#decodeBase64(String)
	 * @param base64String
	 */
	public static byte[] decodeBase64(String base64String) {
		return Base64.decodeBase64(base64String);
	}
	
	/**
	 * @param data
	 * @param sCharset {@link seung.java.kimchi.util.SCharset}
	 * @return {@link #encodeBase64String(byte[])}
	 * @throws SKimchiException 
	 */
	public static String encodeBase64String(String data, SCharset sCharset) throws SKimchiException {
		try {
			return encodeBase64String(data.getBytes(sCharset.text()));
		} catch (UnsupportedEncodingException e) {
			throw new SKimchiException(e);
		}
	}
	/**
	 * @see org.apache.commons.codec.binary.Base64#encodeBase64String(byte[])
	 * @param binaryData
	 */
	public static String encodeBase64String(byte[] binaryData) {
		return Base64.encodeBase64String(binaryData);
	}
	
	/**
	 * @param data
	 * @param sCharset {@link seung.java.kimchi.util.SCharset}
	 * @return {@link #decodeHex(String)}
	 * @throws SKimchiException 
	 */
	public static String decodeHex(String data, SCharset sCharset) throws SKimchiException {
		try {
			return new String(decodeHex(data), sCharset.text());
		} catch (UnsupportedEncodingException e) {
			throw new SKimchiException(e);
		}
	}
	/**
	 * @see org.apache.commons.codec.binary.Hex#decodeHex(String)
	 * @param data
	 * @throws SKimchiException 
	 */
	public static byte[] decodeHex(String data) throws SKimchiException {
		try {
			return Hex.decodeHex(data);
		} catch (DecoderException e) {
			throw new SKimchiException(e);
		}
	}
	
	/**
	 * @param data
	 * @param sCharset {@link seung.java.kimchi.util.SCharset}
	 * @param toLowerCase
	 * @return {@link #encodeHexString(byte[], boolean)}
	 * @throws SKimchiException 
	 */
	public static String encodeHexString(String data, SCharset sCharset, boolean toLowerCase) throws SKimchiException {
		try {
			return encodeHexString(data.getBytes(sCharset.text()), toLowerCase);
		} catch (UnsupportedEncodingException e) {
			throw new SKimchiException(e);
		}
	}
	/**
	 * @see org.apache.commons.codec.binary.Hex#encodeHexString(byte[], boolean)
	 * @param data
	 * @param toLowerCase
	 */
	public static String encodeHexString(byte[] data, boolean toLowerCase) {
		return Hex.encodeHexString(data, toLowerCase);
	}
	
}
