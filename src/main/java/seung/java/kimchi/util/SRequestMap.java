package seung.java.kimchi.util;

import java.io.IOException;
import java.io.Serializable;
import java.util.UUID;

import org.apache.commons.lang3.exception.ExceptionUtils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * <pre>
 * web service data mapping class
 * </pre>
 * @author seung
 * @since 2020.05.11
 */
public class SRequestMap implements Serializable {

	private static final long serialVersionUID = 5322631908237947372L;
	
	private String         uuid    = UUID.randomUUID().toString();
	
	private SLinkedHashMap network = new SLinkedHashMap();
	private SLinkedHashMap header  = new SLinkedHashMap();
	private SLinkedHashMap session = new SLinkedHashMap();
	private SLinkedHashMap data    = new SLinkedHashMap();
	
	public String getUUID() {
		return uuid;
	}
	
	public SLinkedHashMap getNetwork() {
		return network;
	}
	@SuppressWarnings("unchecked")
	public void putNetwork(Object key, Object value) {
		network.put(key, value);
	}
	
	public SLinkedHashMap getHeader() {
		return header;
	}
	@SuppressWarnings("unchecked")
	public void putHeader(Object key, Object value) {
		header.put(key, value);
	}
	
	public SLinkedHashMap getSession() {
		return session == null ? new SLinkedHashMap() : session;
	}
	@SuppressWarnings("unchecked")
	public void putSession(Object key, Object value) {
		if(session == null) session = new SLinkedHashMap();
		session.put(key, value);
	}
	
	public SLinkedHashMap getData() {
		return data;
	}
	@SuppressWarnings("unchecked")
	public void putData(Object key, Object value) {
		data.put(key, value);
	}
	@SuppressWarnings("unchecked")
	public SLinkedHashMap addData(Object key, Object value) {
		data.put(key, value);
		return data;
	}
	@SuppressWarnings("unchecked")
	public SLinkedHashMap addData(SLinkedHashMap sLinkedHashMap) {
		data.putAll(sLinkedHashMap);
		return data;
	}
	public SLinkedHashMap addData(String jsonString) throws JsonParseException, JsonMappingException, IOException {
		data.putJsonString(jsonString);
		return data;
	}
	public SLinkedHashMap putData(Object o) {
		data.putObject(o);
		return data;
	}
	
	/**
	 * @param isPretty
	 * @return
	 */
	public String toString(boolean isPretty) {
		try {
			if(isPretty) {
				return new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(this);
			} else {
				return new ObjectMapper().writeValueAsString(this);
			}
		} catch (JsonProcessingException e) {
			return ExceptionUtils.getStackTrace(e);
		}
	}
	
}
