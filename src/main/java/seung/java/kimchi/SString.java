package seung.java.kimchi;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;

import seung.java.kimchi.exception.SKimchiException;
import seung.java.kimchi.util.SLinkedHashMap;

/**
 * <pre>
 * String 관련 함수 모음
 * </pre>
 * 
 * @author seung
 */
public class SString {

    private SString() {}
    
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
