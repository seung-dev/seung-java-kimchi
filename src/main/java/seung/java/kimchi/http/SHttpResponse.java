package seung.java.kimchi.http;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import seung.java.kimchi.util.SLinkedHashMap;

/**
 * <pre>
 * http response value object
 * </pre>
 * 
 * @author seung
 * @since 2020.05.11
 */
@ToString(exclude = {"responseBody", "responseError"})
@Builder
@Setter
@Getter
public class SHttpResponse {

    @Builder.Default
    private int responseCode = -1;
    
    @JsonIgnore
    private byte[] responseError;
    
    private String exceptionMessage;
    
    private String protocol;
    
    private String host;
    
    private int port;
    
    private String path;
    
    private String query;
    
    private String responseMessage;
    
    private Map<String, List<String>> responseHeaderFields;
    
    private String cookie;
    
    @Builder.Default
    private long responseLength = -1;
    
    private String responseCharset;
    
    @JsonIgnore
    private byte[] responseBody;
    
    public SLinkedHashMap toSLinkedHashMap() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.getSerializerProvider().setNullKeySerializer(new JsonSerializer<Object>() {
            @Override
            public void serialize(Object value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
                jsonGenerator.writeFieldName("");
            }
        });
        return objectMapper
                .setSerializationInclusion(Include.ALWAYS)
//                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
                .convertValue(this, SLinkedHashMap.class)
                ;
    }
    
    public String toJsonString(boolean isPretty) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.getSerializerProvider().setNullKeySerializer(new JsonSerializer<Object>() {
            @Override
            public void serialize(Object value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
                jsonGenerator.writeFieldName("");
            }
        });
        return objectMapper
                .setSerializationInclusion(Include.ALWAYS)
//                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
                .configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true)
                .configure(SerializationFeature.INDENT_OUTPUT, isPretty)
                .writeValueAsString(this)
                ;
    }
    
}
