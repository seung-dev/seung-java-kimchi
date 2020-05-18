package seung.java.kimchi.http;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.lang3.exception.ExceptionUtils;

import seung.java.kimchi.util.SCharset;

/**
 * <pre>
 * http 관련 함수 모음
 * </pre>
 * @author seung
 * @since 2020.05.11
 */
public class SHttpU {

	/**
	 * @see #http(SRequestV)
	 * @see #https(SRequestV)
	 * @param sRequestV {@link seung.java.kimchi.http.SRequestV}
	 * @return {@link seung.java.kimchi.http.SResponseV}
	 */
	public static SResponseV request(SRequestV sRequestV) {
		if(sRequestV.getUrl().startsWith("https")) {
			return https(sRequestV);
		}
		return http(sRequestV);
	}
	
//	public static SHttpV requestWithFile(SHttpV sHttpVO, String fileFieldName, String fileName, byte[] file) {
//		
//		String             boundary           = "----" + SStringU.getUUID();
//		String             delim              = "--";
//		String             newLine            = "\r\n";
//		
//		HttpsURLConnection httpsURLConnection = null;
//		DataOutputStream   dataOutputStream   = null;
//		InputStreamReader  inputStreamReader  = null;
//		
//		try {
//			
//			HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
//				@Override
//				public boolean verify(String hostname, SSLSession session) {
//					return true;
//				}
//			});
//			
//			SSLContext sslContext = SSLContext.getInstance("SSL");
//			sslContext.init(
//					null
//					, new TrustManager[] {
//							new X509TrustManager() {
//								@Override
//								public java.security.cert.X509Certificate[] getAcceptedIssuers() {
//									return null;
//								}
//								@Override
//								public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
//									
//								}
//								@Override
//								public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
//								}
//							}
//					}
//					, new SecureRandom()
//					);
//			HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
//			
//			URL url = new URL(sHttpVO.getRequestUrl());
//			httpsURLConnection = (HttpsURLConnection) url.openConnection();
//			
//			if(sHttpVO.getRequestHeaders() != null) {
//				for(String[] header : sHttpVO.getRequestHeaders()) {
//					httpsURLConnection.setRequestProperty(header[0], header[1]);
//				}
//			}
//			
//			httpsURLConnection.setRequestMethod(sHttpVO.getRequestMethod());
//			httpsURLConnection.setConnectTimeout(sHttpVO.getConnectionTimeout());
//			httpsURLConnection.setReadTimeout(sHttpVO.getReadTimeout());
//			httpsURLConnection.setUseCaches(false);
//			httpsURLConnection.setDoInput(true);
//			httpsURLConnection.setDoOutput(true);
//			httpsURLConnection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
//			
//			dataOutputStream = new DataOutputStream(httpsURLConnection.getOutputStream());
//			dataOutputStream.writeBytes(delim + boundary + newLine);
//			dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"" + fileFieldName + "\"; filename=\"" + fileName + "\"" + newLine);
//			dataOutputStream.writeBytes("Content-Type: application/octet-stream" + newLine);
//			dataOutputStream.writeBytes(newLine);
//			dataOutputStream.write(file);
//			dataOutputStream.writeBytes(newLine);
//			for(String[] requestParameters : sHttpVO.getRequestParameters()) {
//				dataOutputStream.writeBytes(delim + boundary + newLine);
//				dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"" + requestParameters[0] + "\"" + newLine);
//				dataOutputStream.writeBytes(newLine);
//				dataOutputStream.writeBytes(requestParameters[1]);
//				dataOutputStream.writeBytes(newLine);
//			}
//			dataOutputStream.writeBytes(delim + boundary + delim);
//			dataOutputStream.flush();
//			
//			sHttpVO.setResponseCode(httpsURLConnection.getResponseCode());
//			sHttpVO.setContentType(httpsURLConnection.getContentType());
//			sHttpVO.setContentLength(httpsURLConnection.getContentLength());
//			sHttpVO.setContentDisposition(httpsURLConnection.getHeaderField("Content-Disposition"));
////		sHttpVO.setResponse(IOUtils.toByteArray(httpsURLConnection.getInputStream(), sHttpVO.getResponseEncoding()));
//			
//		} catch (NoSuchAlgorithmException e) {
//			sHttpVO.setExceptionMessage("" + e);
//		} catch (KeyManagementException e) {
//			sHttpVO.setExceptionMessage("" + e);
//		} catch (IOException e) {
//			sHttpVO.setExceptionMessage("" + e);
//		} finally {
//			try {
//				if(inputStreamReader != null) {
//					inputStreamReader.close();
//				}
//				if(dataOutputStream != null) {
//					dataOutputStream.close();
//				}
//				if(httpsURLConnection != null) {
//					httpsURLConnection.disconnect();
//				}
//			} catch (IOException e) {
//				sHttpVO.setExceptionMessage("" + e);
//			}
//		}
//		
//		return sHttpVO;
//	}
	
	/**
	 * <pre>
	 * https request
	 * </pre>
	 * @param sRequestV {@link seung.java.kimchi.http.SRequestV}
	 * @return {@link seung.java.kimchi.http.SResponseV}
	 */
	public static SResponseV https(SRequestV sRequestV) {
		
		SResponseV sResponseV = new SResponseV();
		
		HttpsURLConnection httpsURLConnection = null;
		try {
			
			if(sRequestV.getFormData().size() > 0) {
				StringBuffer stringBuffer = new StringBuffer();
				for(String[] formData : sRequestV.getFormData()) {
					stringBuffer.append("&");
					stringBuffer.append(formData[0]);
					stringBuffer.append("=");
					if(formData[1] != null) {
						stringBuffer.append(encodeURIComponent(formData[1], sRequestV.getFormDataCharset()));
					}
				}
				sRequestV.setFormDataString(stringBuffer.length() > 0 ? stringBuffer.toString().substring(1) : "");
			}
			
			String urlString = sRequestV.getUrl();
			if(
					SRequestMethod.GET == sRequestV.getRequestMethod()
					&& sRequestV.getFormDataString() != null
					&& sRequestV.getFormDataString().length() > 0
					) {
				String[] urlSplit = sRequestV.getUrl().split("?");
				urlString = urlSplit.length > 1 ? urlSplit[0] : sRequestV.getUrl();
				urlString += "?";
				urlString += sRequestV.getFormDataString();
				if(urlSplit.length > 1) {
					urlString += "&";
					urlString += urlSplit[1];
				}
			}
			URL url = new URL(urlString);
			
			sRequestV.setProtocol(url.getProtocol());
			sRequestV.setHost(url.getHost());
			sRequestV.setPort(url.getPort() == -1 ? url.getDefaultPort() : url.getPort());
			sRequestV.setPath(url.getPath());
			sRequestV.setQuery(url.getQuery());
			
			HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
				@Override
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			});
			
			SSLContext sslContext = SSLContext.getInstance("SSL");
			sslContext.init(
					null
					, new TrustManager[] {
							new X509TrustManager() {
								@Override
								public java.security.cert.X509Certificate[] getAcceptedIssuers() {
									return null;
								}
								@Override
								public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
									
								}
								@Override
								public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
								}
							}
					}
					, new SecureRandom()
					);
			HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
			
			if(sRequestV.getProxyType() != null) {
				Proxy proxy = new Proxy(sRequestV.getProxyType(), new InetSocketAddress(sRequestV.getProxyHostname(), sRequestV.getProxyPort()));
				httpsURLConnection = (HttpsURLConnection) url.openConnection(proxy);
			} else {
				httpsURLConnection = (HttpsURLConnection) url.openConnection();
			}
			httpsURLConnection.setDefaultUseCaches(false);
			httpsURLConnection.setDoInput(sRequestV.getDoInput());
			httpsURLConnection.setDoOutput(sRequestV.getDoOutput());
			httpsURLConnection.setConnectTimeout(sRequestV.getConnectTimeout());
			httpsURLConnection.setReadTimeout(sRequestV.getReadTimeout());
			httpsURLConnection.setRequestMethod(sRequestV.getRequestMethod().text());
			for(String key : sRequestV.getRequestProperty().keySet()) {
				StringBuffer stringBuffer = new StringBuffer();
				for(String value : sRequestV.getRequestProperty().get(key)) {
					stringBuffer.append("; ");
					stringBuffer.append(value);
				}
				httpsURLConnection.setRequestProperty(key, stringBuffer.substring(2));
			}
			if(SRequestMethod.POST == sRequestV.getRequestMethod()) {
				httpsURLConnection.getOutputStream().write(sRequestV.getFormDataString().getBytes(sRequestV.getFormDataCharset().text()));
				httpsURLConnection.getOutputStream().flush();
			}
			
			sResponseV.setResponseCode(httpsURLConnection.getResponseCode());
			sResponseV.setResponseMessage(httpsURLConnection.getResponseMessage());
			sResponseV.setResponseHeaderFields(httpsURLConnection.getHeaderFields());
			
			sResponseV.setResultCode("0000");
			sResponseV.setResultMessage("");
			
		} catch (NoSuchAlgorithmException e) {
			sResponseV.setResultCode("E911");
			sResponseV.setResultMessage(ExceptionUtils.getStackTrace(e));
		} catch (KeyManagementException e) {
			sResponseV.setResultCode("E912");
			sResponseV.setResultMessage(ExceptionUtils.getStackTrace(e));
		} catch (MalformedURLException e) {
			sResponseV.setResultCode("E901");
			sResponseV.setResultMessage(ExceptionUtils.getStackTrace(e));
		} catch (IOException e) {
			sResponseV.setResultCode("E902");
			sResponseV.setResultMessage(ExceptionUtils.getStackTrace(e));
		} finally {
			httpsURLConnection.disconnect();
		}
		
		return sResponseV;
	}
	
	/**
	 * <pre>
	 * http request
	 * </pre>
	 * @param sRequestV {@link seung.java.kimchi.http.SRequestV}
	 * @return {@link seung.java.kimchi.http.SResponseV}
	 */
	public static SResponseV http(SRequestV sRequestV) {
		
		SResponseV sResponseV = new SResponseV();
		
		HttpURLConnection httpURLConnection = null;
		try {
			
			if(sRequestV.getFormData().size() > 0) {
				StringBuffer stringBuffer = new StringBuffer();
				for(String[] formData : sRequestV.getFormData()) {
					stringBuffer.append("&");
					stringBuffer.append(formData[0]);
					stringBuffer.append("=");
					if(formData[1] != null) {
						stringBuffer.append(encodeURIComponent(formData[1], sRequestV.getFormDataCharset()));
					}
				}
				sRequestV.setFormDataString(stringBuffer.length() > 0 ? stringBuffer.toString().substring(1) : "");
			}
			
			String urlString = sRequestV.getUrl();
			if(
					SRequestMethod.GET == sRequestV.getRequestMethod()
					&& sRequestV.getFormDataString() != null
					&& sRequestV.getFormDataString().length() > 0
					) {
				String[] urlSplit = sRequestV.getUrl().split("?");
				urlString = urlSplit.length > 1 ? urlSplit[0] : sRequestV.getUrl();
				urlString += "?";
				urlString += sRequestV.getFormDataString();
				if(urlSplit.length > 1) {
					urlString += "&";
					urlString += urlSplit[1];
				}
			}
			URL url = new URL(urlString);
			
			sRequestV.setProtocol(url.getProtocol());
			sRequestV.setHost(url.getHost());
			sRequestV.setPort(url.getPort() == -1 ? url.getDefaultPort() : url.getPort());
			sRequestV.setPath(url.getPath());
			sRequestV.setQuery(url.getQuery());
			
			if(sRequestV.getProxyType() != null) {
				Proxy proxy = new Proxy(sRequestV.getProxyType(), new InetSocketAddress(sRequestV.getProxyHostname(), sRequestV.getProxyPort()));
				httpURLConnection = (HttpURLConnection) url.openConnection(proxy);
			} else {
				httpURLConnection = (HttpURLConnection) url.openConnection();
			}
			httpURLConnection.setDefaultUseCaches(false);
			httpURLConnection.setDoInput(sRequestV.getDoInput());
			httpURLConnection.setDoOutput(sRequestV.getDoOutput());
			httpURLConnection.setConnectTimeout(sRequestV.getConnectTimeout());
			httpURLConnection.setReadTimeout(sRequestV.getReadTimeout());
			httpURLConnection.setRequestMethod(sRequestV.getRequestMethod().text());
			for(String key : sRequestV.getRequestProperty().keySet()) {
				StringBuffer stringBuffer = new StringBuffer();
				for(String value : sRequestV.getRequestProperty().get(key)) {
					stringBuffer.append("; ");
					stringBuffer.append(value);
				}
				httpURLConnection.setRequestProperty(key, stringBuffer.substring(2));
			}
			if(SRequestMethod.POST == sRequestV.getRequestMethod()) {
				httpURLConnection.getOutputStream().write(sRequestV.getFormDataString().getBytes(sRequestV.getFormDataCharset().text()));
				httpURLConnection.getOutputStream().flush();
			}
			
			sResponseV.setResponseCode(httpURLConnection.getResponseCode());
			sResponseV.setResponseMessage(httpURLConnection.getResponseMessage());
			sResponseV.setResponseHeaderFields(httpURLConnection.getHeaderFields());
			
			sResponseV.setResultCode("0000");
			sResponseV.setResultMessage("");
			
		} catch (MalformedURLException e) {
			sResponseV.setResultCode("E901");
			sResponseV.setResultMessage(ExceptionUtils.getStackTrace(e));
		} catch (IOException e) {
			sResponseV.setResultCode("E902");
			sResponseV.setResultMessage(ExceptionUtils.getStackTrace(e));
		} finally {
			httpURLConnection.disconnect();
		}
		
		return sResponseV;
	}
	
	/**
	 * <pre>
	 * default sCharset is {@link seung.java.kimchi.util.SCharset#UTF_8}.
	 * </pre>
	 * @param data
	 * @return {@link #encodeURIComponent(String, SCharset)}
	 * @throws UnsupportedEncodingException
	 */
	public static String encodeURIComponent(String data) throws UnsupportedEncodingException {
		return encodeURIComponent(data, SCharset.UTF_8);
	}
	/**
	 * @param data
	 * @param sCharset {@link seung.java.kimchi.util.SCharset}
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String encodeURIComponent(String data, SCharset sCharset) throws UnsupportedEncodingException {
		return URLEncoder.encode(data, sCharset.text())
				.replaceAll("\\+"  , "%20")
				.replaceAll("\\%21", "!")
				.replaceAll("\\%27", "'")
				.replaceAll("\\%28", "(")
				.replaceAll("\\%29", ")")
				.replaceAll("\\%7E", "~")
				;
	}
	
	/**
	 * <pre>
	 * default sCharset is {@link seung.java.kimchi.util.SCharset#UTF_8}.
	 * </pre>
	 * @param data
	 * @return {@link #decodeURI(String, SCharset)}
	 * @throws UnsupportedEncodingException
	 */
	public static String decodeURI(String data) throws UnsupportedEncodingException {
		return decodeURI(data, SCharset.UTF_8);
	}
	/**
	 * @param data
	 * @param sCharset {@link seung.java.kimchi.util.SCharset}
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String decodeURI(String data, SCharset sCharset) throws UnsupportedEncodingException {
		return URLDecoder.decode(data, sCharset.text());
	}
	
}
