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
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.tuple.Pair;

import seung.java.kimchi.util.SCharset;

/**
 * <pre>
 * http 관련 함수 모음
 * </pre>
 * @author seung
 * @since 2020.05.11
 */
public class SHttpU {

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
	 * @param sRequestV {@link seung.java.kimchi.http.SRequestV}
	 * @return {@link seung.java.kimchi.http.SResponseV}
	 */
	public static SResponseV request(SRequestV sRequestV) {
		
		SResponseV sResponseV = new SResponseV();
		
		HttpURLConnection httpURLConnection = null;
		try {
			
			URL   url = null;
			
			StringBuffer data = new StringBuffer();
			for(Pair<String, String> pair : sRequestV.getData()) {
				data.append("&");
				if(SRequestMethod.GET == sRequestV.getRequestMethod()) {
					data.append(encodeURIComponent(pair.getLeft(), sRequestV.getDataCharset()));
				} else {
					data.append(pair.getLeft());
				}
				data.append("=");
				if(SRequestMethod.GET == sRequestV.getRequestMethod()) {
					data.append(encodeURIComponent(pair.getRight(), sRequestV.getDataCharset()));
				} else {
					data.append(pair.getRight());
				}
			}// end of data
			
			if(SRequestMethod.GET == sRequestV.getRequestMethod() && sRequestV.getData().size() > 0) {
				String[] divided = sRequestV.getUrl().trim().split("\\?");
				if(divided.length > 1) {
					url = new URL(String.format("%s?%s%s", divided[0], divided[1], data.toString()));
				} else {
					url = new URL(String.format("%s?%s", divided[0], data.toString().substring(1)));
				}
			} else {
				url = new URL(sRequestV.getUrl().trim());
			}// end of url
			
			sResponseV.setProtocol(url.getProtocol());
			sResponseV.setHost(url.getHost());
			sResponseV.setPort(url.getPort() == -1 ? url.getDefaultPort() : url.getPort());
			sResponseV.setPath(url.getPath());
			if(SRequestMethod.GET == sRequestV.getRequestMethod()) {
				sResponseV.setQuery(url.getQuery());
			} else {
				sResponseV.setQuery(data.toString().substring(1));
			}// end of query
			
			if("http".equals(url.getProtocol())) {
				
				if(sRequestV.useProxy()) {
					Proxy proxy = new Proxy(sRequestV.getProxyType(), new InetSocketAddress(sRequestV.getProxyHostname(), sRequestV.getProxyPort()));
					httpURLConnection = (HttpURLConnection) url.openConnection(proxy);
				} else {
					httpURLConnection = (HttpURLConnection) url.openConnection();
				}
				
			} else if("https".equals(url.getProtocol())) {
				
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
				
				if(sRequestV.useProxy()) {
					Proxy proxy = new Proxy(sRequestV.getProxyType(), new InetSocketAddress(sRequestV.getProxyHostname(), sRequestV.getProxyPort()));
					httpURLConnection = (HttpsURLConnection) url.openConnection(proxy);
				} else {
					httpURLConnection = (HttpsURLConnection) url.openConnection();
				}
				
			}// end of httpURLConnection
			
//			httpURLConnection.setFollowRedirects(sRequestV.followRedirects());
			httpURLConnection.setUseCaches(sRequestV.useCache());
			httpURLConnection.setDoInput(sRequestV.doInput());
			httpURLConnection.setDoOutput(sRequestV.doOutput());
			httpURLConnection.setConnectTimeout(sRequestV.getConnectTimeout());
			httpURLConnection.setReadTimeout(sRequestV.getReadTimeout());
			
			for(Pair<String, String> property : sRequestV.getHeaders()) {
				httpURLConnection.setRequestProperty(property.getLeft(), property.getRight());
			}// end of property
			
			if(SRequestMethod.POST == sRequestV.getRequestMethod() && data.length() > 0) {
				httpURLConnection.setDoOutput(true);
				httpURLConnection.getOutputStream().write(data.toString().substring(1).getBytes(sRequestV.getDataCharset().text()));
				httpURLConnection.getOutputStream().flush();
				httpURLConnection.getOutputStream().close();
			}
			
			sResponseV.setResponseCode(httpURLConnection.getResponseCode());
			sResponseV.setResponseMessage(httpURLConnection.getResponseMessage());
			if(httpURLConnection.getErrorStream() != null) {
				sResponseV.setResponseError(IOUtils.toByteArray(httpURLConnection.getErrorStream()));
			}
			
			Map<String, List<String>> headerFields = httpURLConnection.getHeaderFields();
			sResponseV.setResponseHeaderFields(headerFields);
			for(String key : headerFields.keySet()) {
				if("set-cookie".equals(key == null ? "" : key.toLowerCase())) {
					sResponseV.setCookie(String.join(";", headerFields.get(key)));
				}
			}
			
			if(sResponseV.getResponseCode() == HttpURLConnection.HTTP_OK && sRequestV.doInput() && httpURLConnection.getInputStream() != null) {
				sResponseV.setResponseBody(IOUtils.toByteArray(httpURLConnection.getInputStream()));
			}
			
		} catch (MalformedURLException e) {
			sResponseV.setResponseCode(0);
			sResponseV.setExceptionMessage(ExceptionUtils.getStackTrace(e));
		} catch (UnsupportedEncodingException e) {
			sResponseV.setResponseCode(0);
			sResponseV.setExceptionMessage(ExceptionUtils.getStackTrace(e));
		} catch (IOException e) {
			sResponseV.setResponseCode(0);
			sResponseV.setExceptionMessage(ExceptionUtils.getStackTrace(e));
		} catch (NoSuchAlgorithmException e) {
			sResponseV.setResponseCode(0);
			sResponseV.setExceptionMessage(ExceptionUtils.getStackTrace(e));
		} catch (KeyManagementException e) {
			sResponseV.setResponseCode(0);
			sResponseV.setExceptionMessage(ExceptionUtils.getStackTrace(e));
		} finally {
			if(httpURLConnection != null) {
				httpURLConnection.disconnect();
			}
		}
		
//		if(sRequestV.getUrl().startsWith("https")) {
//			httpURLConnection = getHttpsURLConnection(sRequestV);
//		} else {
//			httpURLConnection = getHttpURLConnection(sRequestV);
//		}
//		return http(sRequestV);
		
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
