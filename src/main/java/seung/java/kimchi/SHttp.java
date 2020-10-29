package seung.java.kimchi;

import kong.unirest.GetRequest;
import kong.unirest.HttpResponse;

public class SHttp {

	public static HttpResponse<byte[]> request(
			GetRequest getRequest
			) throws InterruptedException {
		return request(getRequest, 0, 0);
	}
	public static HttpResponse<byte[]> request(
			GetRequest getRequest
			, int max
			, long millis
			) throws InterruptedException {
		
		HttpResponse<byte[]> httpResponse = null;
		
		int request = 0;
		while(request++ < max) {
			try {
				httpResponse = getRequest.asBytes();
			} catch (Exception e) {
				if(millis > 0) {
					Thread.sleep(millis);
				}
			}
		}
		
		return httpResponse;
	}
	
}
