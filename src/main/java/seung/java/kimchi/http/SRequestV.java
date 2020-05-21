package seung.java.kimchi.http;

import java.io.Serializable;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import seung.java.kimchi.util.SCharset;
import seung.java.kimchi.util.SLinkedHashMap;

/**
 * <pre>
 * http request value object
 * </pre>
 * @author seung
 * @since 2020.05.11
 */
public class SRequestV implements Serializable {

	private static final long serialVersionUID = 4940150854342584532L;
	
	private String         url           = "";
	private SRequestMethod requestMethod = SRequestMethod.GET;
	
	private boolean    useProxy      = false;
	private Proxy.Type proxyType     = Proxy.Type.HTTP;
	private String     proxyHostname = "";
	private int        proxyPort     = -1;
	
//	private boolean followRedirects = true;
	private boolean useCache        = false;
	
	private boolean doInput  = true;
	private boolean doOutput = false;
	
	private int connectTimeout = 1000 * 3;
	private int readTimeout    = 1000 * 60;
	
	private List<Pair<String, String>> headers = new ArrayList<Pair<String, String>>();
	
	private SCharset                   dataCharset = SCharset.UTF_8;
	private List<Pair<String, String>> data        = new ArrayList<Pair<String, String>>();
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	public SRequestMethod getRequestMethod() {
		return requestMethod;
	}
	public void setRequestMethod(SRequestMethod requestMethod) {
		this.requestMethod = requestMethod;
	}
	
	public boolean useProxy() {
		return useProxy;
	}
	public void setUseProxy(boolean useProxy) {
		this.useProxy = useProxy;
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
	
//	public boolean followRedirects() {
//		return followRedirects;
//	}
//	public void setFollowRedirects(boolean followRedirects) {
//		this.followRedirects = followRedirects;
//	}
	public boolean useCache() {
		return useCache;
	}
	public void setUseCache(boolean useCache) {
		this.useCache = useCache;
	}
	
	public boolean doInput() {
		return doInput;
	}
	public void setDoInput(boolean doInput) {
		this.doInput = doInput;
	}
	public boolean doOutput() {
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
	
	public List<Pair<String, String>> getHeaders() {
		return headers;
	}
	public void setHeaders(List<Pair<String, String>> headers) {
		this.headers = headers;
	}
	public void addHeaders(String key, String value) {
		this.headers.add(Pair.of(key, value));
	}
	
	public SCharset getDataCharset() {
		return dataCharset;
	}
	public void setDataCharset(SCharset dataCharset) {
		this.dataCharset = dataCharset;
	}
	public List<Pair<String, String>> getData() {
		return data;
	}
	public void addData(String key, String value) {
		this.data.add(Pair.of(key, value));
	}
	public void addData(SLinkedHashMap sLinkedHashMap) {
		for(Object key : sLinkedHashMap.keySet()) {
			this.data.add(Pair.of("" + key, sLinkedHashMap.getString(key, "")));
		}
	}
	
}
