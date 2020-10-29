package seung.java.kimchi;

import kong.unirest.HttpRequest;
import kong.unirest.HttpResponse;

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
			try {
				httpResponse = httpRequest.asBytes();
			} catch (Exception e) {
				if(millis > 0) {
					Thread.sleep(millis);
				}
			}
		}
		
		return httpResponse;
	}
	
}
