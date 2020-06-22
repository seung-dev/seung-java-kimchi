package seung.java.kimchi;

import java.io.IOException;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;

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
	 */
	public static String toJson(Object data) {
		return toJson(data, false);
	}
	/**
	 * @see com.fasterxml.jackson.databind.ObjectMapper#writerWithDefaultPrettyPrinter()
	 * @see com.fasterxml.jackson.databind.ObjectMapper#writeValueAsString(Object)
	 * @param data
	 * @param isPretty
	 */
	public static String toJson(Object data, boolean isPretty) {
		String json = "";
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.getSerializerProvider().setNullKeySerializer(new JsonSerializer<Object>() {
				@Override
				public void serialize(Object value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
					jsonGenerator.writeFieldName("");
				}
			});
			if(isPretty) {
				json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(data);
			}
			json = objectMapper.writeValueAsString(data);
		} catch (JsonProcessingException e) {
			json = String.format("{\"errorMessage\":\"%s\"}", ExceptionUtils.getStackTrace(e));
		}
		return json;
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
