package seung.java.kimchi;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.util.Set;
import java.util.UUID;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.Inflater;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;

import seung.java.kimchi.exception.SKimchiException;
import seung.java.kimchi.util.SCharset;

/**
 * <pre>
 * String 관련 함수 모음
 * </pre>
 * 
 * @author seung
 */
public class SString {

    private SString() {}
    
    public static byte[] inflate(byte[] data) throws SKimchiException {
        return inflate(data, true);
    }
    public static byte[] inflate(byte[] data, boolean nowrap) throws SKimchiException {
        
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
    
    public static byte[] deflate(byte[] data) throws SKimchiException {
        return deflate(data, Deflater.BEST_COMPRESSION, true);
    }
    public static byte[] deflate(byte[] data, int level, boolean nowrap) throws SKimchiException {
        
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
    
    /**
     * @param compressed byte array
     * @throws SKimchiException 
     */
    public static byte[] decompress(byte[] compressed) throws SKimchiException {
        
        byte[] decompressed = null;
        try (
                ByteArrayInputStream  byteArrayInputStream  = new ByteArrayInputStream(compressed);
                GZIPInputStream       gzipInputStream       = new GZIPInputStream(byteArrayInputStream);
                BufferedInputStream   bufferedInputStream   = new BufferedInputStream(gzipInputStream);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                ) {
            
            byte[] b = new byte[1024];
            int length;
            while((length = bufferedInputStream.read(b)) != -1) {
                byteArrayOutputStream.write(b, 0, length);
            }
            
            byteArrayOutputStream.close();
            bufferedInputStream.close();
            gzipInputStream.close();
            byteArrayInputStream.close();
            
            decompressed = byteArrayOutputStream.toByteArray();
            
        } catch (IOException e) {
            throw new SKimchiException(e);
        }
        
        return decompressed;
    }
    /**
     * @param data
     * @throws SKimchiException 
     */
    public static byte[] compress(byte[] data) throws SKimchiException {
        
        byte[] compressed = null;
        try (
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                GZIPOutputStream      gzipOutputStream      = new GZIPOutputStream(byteArrayOutputStream);
                BufferedOutputStream  bufferedOutputStream  = new BufferedOutputStream(gzipOutputStream);
                ) {
            
            bufferedOutputStream.write(data);
            bufferedOutputStream.close();
            gzipOutputStream.close();
            byteArrayOutputStream.close();
            
            compressed = byteArrayOutputStream.toByteArray();
            
        } catch (IOException e) {
            throw new SKimchiException(e);
        }
        
        return compressed;
    }
    
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
        int[]    decimalArray      = encodeDecimal(data, sCharset);
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
    
    /**
     * <pre>
     * default padChar is blank.
     * </pre>
     * 
     * @param data
     * @param maxLength
     */
    public static String padRight(String data, int maxLength) {
        return String.format("%" + maxLength + "s", data);
    }
    /**
     * @param data
     * @param maxLength
     * @param padChar
     */
    public static String padRight(String data, int maxLength, String padChar) {
        return String.format("%" + maxLength + "s", data).replace(" ", padChar);
    }
    /**
     * <pre>
     * default padChar is blank.
     * </pre>
     * 
     * @param data
     * @param maxLength
     */
    public static String padLeft(String data, int maxLength) {
        return String.format("%-" + maxLength + "s", data);
    }
    /**
     * @param data
     * @param maxLength
     * @param padChar
     */
    public static String padLeft(String data, int maxLength, String padChar) {
        return String.format("%-" + maxLength + "s", data).replace(" ", padChar);
    }
    
    /**
     * <pre>
     * default isPretty is false.
     * </pre>
     * 
     * @param data
     * @return {@link #toJson(Object, boolean)}
     * @throws SKimchiException 
     */
    public static String toJson(Object data) throws SKimchiException {
        return toJson(data, false);
    }
    /**
     * @see com.fasterxml.jackson.databind.ObjectMapper#writerWithDefaultPrettyPrinter()
     * @see com.fasterxml.jackson.databind.ObjectMapper#writeValueAsString(Object)
     * @param data
     * @param isPretty
     * @throws SKimchiException 
     */
    public static String toJson(Object data, boolean isPretty) throws SKimchiException {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.getSerializerProvider().setNullKeySerializer(new JsonSerializer<Object>() {
                @Override
                public void serialize(Object value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
                    jsonGenerator.writeFieldName("");
                }
            });
            if(isPretty) {
                return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(data);
            }
            return objectMapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            throw new SKimchiException(e);
        }
    }
    
    /**
     * @see org.apache.commons.lang3.StringUtils#repeat(String, int)
     * @param data
     * @param repeat
     */
    public static String repeat(String data, int repeat) {
        return StringUtils.repeat(data, repeat);
    }
    
    /**
     * @return {@link UUID#randomUUID()}
     */
    public static String getUUID() {
        return UUID.randomUUID().toString();
    }
    
}
