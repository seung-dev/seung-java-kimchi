package seung.java.kimchi.http;

import java.io.Serializable;
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
	
	private String resultCode;
	private String resultMessage;
	
	private int                       responseCode         = -1;
	private String                    responseMessage      = null;
	private Map<String, List<String>> responseHeaderFields = null;
	private byte[]                    responseBody         = null;
	
	public String getResultCode() {
		return resultCode;
	}
	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}
	public String getResultMessage() {
		return resultMessage;
	}
	public void setResultMessage(String resultMessage) {
		this.resultMessage = resultMessage;
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
	
	public byte[] getResponseBody() {
		return responseBody;
	}
	public void setResponseBody(byte[] responseBody) {
		this.responseBody = responseBody;
	}
	
}
