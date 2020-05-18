package seung.java.kimchi.http;

import java.io.Serializable;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import seung.java.kimchi.util.SCharset;

/**
 * <pre>
 * http request value object
 * </pre>
 * @author seung
 * @since 2020.05.11
 */
public class SRequestV implements Serializable {

	private static final long serialVersionUID = 4940150854342584532L;
	
	private String                    url           = "";
	private String                    protocol      = "";
	private String                    host          = "";
	private int                       port          = -1;
	private String                    path          = "";
	private String                    query         = "";
	private SRequestMethod               requestMethod = SRequestMethod.GET;
	
	private Proxy.Type                proxyType     = null;
	private String                    proxyHostname = null;
	private int                       proxyPort     = -1;
	
	private boolean                   doInput  = true;
	private boolean                   doOutput = false;
	
	private int                       connectTimeout = 1000 * 3;
	private int                       readTimeout    = 1000 * 60;
	
	private Map<String, List<String>> requestProperty = new LinkedHashMap<String, List<String>>();
	
	private String                    formDataString  = "";
	private SCharset                  formDataCharset = SCharset.UTF_8;
	private List<String[]>            formData        = new ArrayList<String[]>();
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
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
	public SRequestMethod getRequestMethod() {
		return requestMethod;
	}
	public void setRequestMethod(SRequestMethod requestMethod) {
		this.requestMethod = requestMethod;
	}
	
	public Proxy.Type getProxyType() {
		return proxyType;
	}
	public void setProxyType(Proxy.Type proxyType) {
		this.proxyType = proxyType;
	}
	public String getProxyHostname() {
		return proxyHostname;
	}
	public void setProxyHostname(String proxyHostname) {
		this.proxyHostname = proxyHostname;
	}
	public int getProxyPort() {
		return proxyPort;
	}
	public void setProxyPort(int proxyPort) {
		this.proxyPort = proxyPort;
	}
	
	public boolean getDoInput() {
		return doInput;
	}
	public void setDoInput(boolean doInput) {
		this.doInput = doInput;
	}
	public boolean getDoOutput() {
		return doOutput;
	}
	public void setDoOutput(boolean doOutput) {
		this.doOutput = doOutput;
	}
	
	public int getConnectTimeout() {
		return connectTimeout;
	}
	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}
	public int getReadTimeout() {
		return readTimeout;
	}
	public void setReadTimeout(int readTimeout) {
		this.readTimeout = readTimeout;
	}
	
	public Map<String, List<String>> getRequestProperty() {
		return requestProperty;
	}
	public void setRequestProperty(Map<String, List<String>> requestProperty) {
		this.requestProperty = requestProperty;
	}
	public void addRequestProperty(String key, List<String> values) {
		requestProperty.put(key, values);
	}
	
	public String getFormDataString() {
		return formDataString;
	}
	public void setFormDataString(String formDataString) {
		this.formDataString = formDataString;
	}
	public SCharset getFormDataCharset() {
		return formDataCharset;
	}
	public void setFormDataCharset(SCharset formDataCharset) {
		this.formDataCharset = formDataCharset;
	}
	public List<String[]> getFormData() {
		return formData;
	}
	public void setFormData(List<String[]> formData) {
		this.formData = formData;
	}
	public void addFormData(String key, String value) {
		formData.add(new String[] {key, value});
	}
	
}
