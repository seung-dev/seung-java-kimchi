package seung.java.kimchi.xml;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import lombok.Builder;
import lombok.Getter;
import seung.java.kimchi.util.SKimchiException;
import seung.java.kimchi.util.SLinkedHashMap;

@Builder
@Getter
public class SXml {

	private List<String> schema;
	
	private List<String> text;
	
	private List<SLinkedHashMap> item;
	
	public SLinkedHashMap item() {
		if(!item.isEmpty()) {
			return item.get(0);
		}
		return null;
	}
	
	public String toJsonString(boolean isPretty) throws SKimchiException {
		try {
			return new ObjectMapper()
					.setSerializationInclusion(Include.ALWAYS)
					.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
					.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true)
					.configure(SerializationFeature.INDENT_OUTPUT, isPretty)
					.writeValueAsString(this)
					;
		} catch (JsonProcessingException e) {
			throw new SKimchiException(e);
		}
	}
	
}
