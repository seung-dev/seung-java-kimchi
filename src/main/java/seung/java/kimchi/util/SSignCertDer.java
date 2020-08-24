package seung.java.kimchi.util;

import org.apache.commons.lang3.exception.ExceptionUtils;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class SSignCertDer {

	@Builder.Default
	private String errorMessage = "";
	
	public String toJsonString(boolean isPretty) {
		try {
			return new ObjectMapper()
					.setSerializationInclusion(Include.ALWAYS)
					.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
					.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true)
					.configure(SerializationFeature.INDENT_OUTPUT, isPretty)
					.writeValueAsString(this)
					;
		} catch (JsonProcessingException e) {
			return new SLinkedHashMap()
					.add("exception", ExceptionUtils.getStackTrace(e))
					.toJsonString(isPretty)
					;
		}
	}
	
	private String base64;
	private String hex;
	
	private String type;
	private int version;
	private String serialNumber;
	private String sigAlgOID;
	private String sigAlgName;
	private String issuerX500PrincipalName;
	private String subjectX500Principal;
	private String notBefore;
	private String notAfter;
	
}
