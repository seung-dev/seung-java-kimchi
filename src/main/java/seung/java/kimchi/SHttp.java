package seung.java.kimchi;

import kong.unirest.HttpRequest;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import seung.java.kimchi.util.SKimchiException;
import seung.java.kimchi.util.SLinkedHashMap;

/**
 * @author seung20200706
 *
 */
public class SHttp {

	@SuppressWarnings("rawtypes")
	public static HttpResponse<byte[]> request(
			HttpRequest httpRequest
			) throws InterruptedException {
		return request(httpRequest, 0, 0);
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static HttpResponse<byte[]> request(
			HttpRequest httpRequest
			, int max
			, long millis
			) throws InterruptedException {
		
		HttpResponse<byte[]> httpResponse = null;
		
		int request = 0;
		while(request++ < max) {
//			System.out.println("request=" + request);
			try {
				httpResponse = httpRequest.asBytes();
				if(200 == httpResponse.getStatus() && httpResponse.getBody() != null) {
					break;
				} else {
					Thread.sleep(millis);
				}
			} catch (Exception e) {
				e.printStackTrace();
				if(millis > 0) {
					Thread.sleep(millis);
				}
			}
		}
		
		return httpResponse;
	}
	
	public static SLinkedHashMap network() throws SKimchiException {
		
		SLinkedHashMap network = null;
		
		HttpResponse<byte[]> httpResponse = Unirest
				.get("https://api.ip.pe.kr/json/")
				.proxy("btpnetp1.idc.bigtechplus.com", 8080)
				.connectTimeout(1000 * 3)
				.socketTimeout(1000 * 3)
				.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko")
				.asBytes()
				;
		
		if(200 == httpResponse.getStatus() && httpResponse.getBody() != null) {
			network = new SLinkedHashMap(new String(httpResponse.getBody()));
		}
		
		return network;
	}
	
}
