package seung.java.kimchi.http;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 * http response value object
 * </pre>
 * @author seung
 * @since 2020.05.11
 */
public class SResponseV implements Serializable {

	private static final long serialVersionUID = 1505086923422336107L;
	
	private String protocol      = "";
	private String host          = "";
	private int    port          = -1;
	private String path          = "";
	private String query         = "";
	
	private int                       responseCode         = -1;
	private String                    responseMessage      = "";
	
	private Map<String, List<String>> responseHeaderFields = new LinkedHashMap<String, List<String>>();
	private String                    cookie               = "";
	
	private byte[] responseBody  = null;
	private byte[] responseError = null;
	
	private String exceptionMessage = "";
	
	public String getProtocol() {
		return protocol;
	}
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getQuery() {
		return query;
	}
	public void setQuery(String query) {
		this.query = query;
	}
	
	public int getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}
	public String getResponseMessage() {
		return responseMessage;
	}
	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}
	
	public Map<String, List<String>> getResponseHeaderFields() {
		return responseHeaderFields;
	}
	public void setResponseHeaderFields(Map<String, List<String>> responseHeaderFields) {
		this.responseHeaderFields = responseHeaderFields;
	}
	public String getCookie() {
		return cookie;
	}
	public void setCookie(String cookie) {
		this.cookie = cookie;
	}
	
	public byte[] getResponseBody() {
		return responseBody;
	}
	public void setResponseBody(byte[] responseBody) {
		this.responseBody = responseBody;
	}
	public byte[] getResponseError() {
		return responseError;
	}
	public void setResponseError(byte[] responseError) {
		this.responseError = responseError;
	}
	
	public String getExceptionMessage() {
		return exceptionMessage;
	}
	public void setExceptionMessage(String exceptionMessage) {
		this.exceptionMessage = exceptionMessage;
	}
	
}
