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
import org.w3c.dom.Document;

import seung.java.kimchi.util.SCharset;

/**
 * <pre>
 * http 관련 함수 모음
 * </pre>
 * @author seung
 * @since 2020.05.11
 */
public class SHttp {

    private SHttp() {}
    
    /**
     * @param sRequestV {@link seung.java.kimchi.http.SHttpRequest}
     * @return {@link seung.java.kimchi.http.SHttpResponse}
     */
    public static SHttpResponse request(SHttpRequest sHttpRequest) {
        
        SHttpResponse sHttpResponse = SHttpResponse.builder().build();
        
        HttpURLConnection httpURLConnection = null;
        try {
            
            URL url = null;
            
            StringBuffer data = new StringBuffer();
            for(Pair<String, String> pair : sHttpRequest.getData()) {
                data.append("&");
                data.append(SRequestMethod.GET.equals(sHttpRequest.getRequestMethod()) ? encodeURIComponent(pair.getLeft(), sHttpRequest.getCharset()) : pair.getLeft());
                data.append("=");
                data.append(SRequestMethod.GET.equals(sHttpRequest.getRequestMethod()) ? encodeURIComponent(pair.getRight(), sHttpRequest.getCharset()) : pair.getRight());
            }// end of data
            
            if(SRequestMethod.GET.equals(sHttpRequest.getRequestMethod()) && !sHttpRequest.getData().isEmpty()) {
                String[] divided = sHttpRequest.getUrl().trim().split("\\?");
                if(divided.length > 1) {
                    url = new URL(String.format("%s?%s%s", divided[0], divided[1], data.toString()));
                } else {
                    url = new URL(String.format("%s?%s", divided[0], data.toString().substring(1)));
                }
            } else {
                url = new URL(sHttpRequest.getUrl().trim());
            }// end of url
            
            sHttpResponse.setProtocol(url.getProtocol());
            sHttpResponse.setHost(url.getHost());
            sHttpResponse.setPort(url.getPort() == -1 ? url.getDefaultPort() : url.getPort());
            sHttpResponse.setPath(url.getPath());
            if(SRequestMethod.GET.equals(sHttpRequest.getRequestMethod())) {
                sHttpResponse.setQuery(url.getQuery());
            } else {
                sHttpResponse.setQuery(data.toString().substring(1));
            }// end of query
            
            if("http".equals(url.getProtocol())) {
                
                if(sHttpRequest.isUseProxy()) {
                    Proxy proxy = new Proxy(sHttpRequest.getProxyType(), new InetSocketAddress(sHttpRequest.getProxyHostname(), sHttpRequest.getProxyPort()));
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
                
                if(sHttpRequest.isUseProxy()) {
                    Proxy proxy = new Proxy(sHttpRequest.getProxyType(), new InetSocketAddress(sHttpRequest.getProxyHostname(), sHttpRequest.getProxyPort()));
                    httpURLConnection = (HttpsURLConnection) url.openConnection(proxy);
                } else {
                    httpURLConnection = (HttpsURLConnection) url.openConnection();
                }
                
            }// end of httpURLConnection
            
//            httpURLConnection.setFollowRedirects(sHttpRequest.followRedirects());
            httpURLConnection.setUseCaches(sHttpRequest.isUseCache());
            httpURLConnection.setDoInput(sHttpRequest.isDoInput());
            httpURLConnection.setDoOutput(sHttpRequest.isDoOutput());
            httpURLConnection.setConnectTimeout(sHttpRequest.getConnectTimeout());
            httpURLConnection.setReadTimeout(sHttpRequest.getReadTimeout());
            
            for(String key : sHttpRequest.getHeader().keySet()) {
                httpURLConnection.setRequestProperty(key, sHttpRequest.getHeader().get(key).get(0));
            }// end of property
            
            if(SRequestMethod.POST.equals(sHttpRequest.getRequestMethod()) && data.length() > 0) {
                httpURLConnection.setDoOutput(true);
                httpURLConnection.getOutputStream().write(data.toString().substring(1).getBytes(sHttpRequest.getCharset().text()));
                httpURLConnection.getOutputStream().flush();
                httpURLConnection.getOutputStream().close();
            }
            
            sHttpResponse.setResponseCode(httpURLConnection.getResponseCode());
            sHttpResponse.setResponseMessage(httpURLConnection.getResponseMessage());
            if(httpURLConnection.getErrorStream() != null) {
                sHttpResponse.setResponseError(IOUtils.toByteArray(httpURLConnection.getErrorStream()));
            }
            
            Map<String, List<String>> headerFields = httpURLConnection.getHeaderFields();
            sHttpResponse.setResponseHeaderFields(headerFields);
            for(String key : headerFields.keySet()) {
                if("set-cookie".equals(key == null ? "" : key.toLowerCase())) {
                    sHttpResponse.setCookie(String.join(";", headerFields.get(key)));
                }
            }
            
            if(sHttpResponse.getResponseCode() == HttpURLConnection.HTTP_OK && sHttpRequest.isDoInput() && httpURLConnection.getInputStream() != null) {
                String contentEncoding = httpURLConnection.getContentEncoding();
                String contentType     = httpURLConnection.getContentType();
                sHttpResponse.setResponseLength(httpURLConnection.getContentLengthLong());
                if(contentEncoding != null) {
                    sHttpResponse.setResponseCharset(contentEncoding);
                } else if(contentType != null && contentType.contains("charset=")) {
                    sHttpResponse.setResponseCharset(contentType.split("charset=")[1].split(";")[0].trim());
                }
                sHttpResponse.setResponseBody(IOUtils.toByteArray(httpURLConnection.getInputStream()));
            }
            
        } catch (MalformedURLException e) {
            sHttpResponse.setResponseCode(0);
            sHttpResponse.setExceptionMessage(ExceptionUtils.getStackTrace(e));
        } catch (UnsupportedEncodingException e) {
            sHttpResponse.setResponseCode(0);
            sHttpResponse.setExceptionMessage(ExceptionUtils.getStackTrace(e));
        } catch (IOException e) {
            sHttpResponse.setResponseCode(0);
            sHttpResponse.setExceptionMessage(ExceptionUtils.getStackTrace(e));
        } catch (NoSuchAlgorithmException e) {
            sHttpResponse.setResponseCode(0);
            sHttpResponse.setExceptionMessage(ExceptionUtils.getStackTrace(e));
        } catch (KeyManagementException e) {
            sHttpResponse.setResponseCode(0);
            sHttpResponse.setExceptionMessage(ExceptionUtils.getStackTrace(e));
        } finally {
            if(httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
        
        return sHttpResponse;
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
    
    public static Document toDocument(String xml) {
        return null;
        
    }
    
}
