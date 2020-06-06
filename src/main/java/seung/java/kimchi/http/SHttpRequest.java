package seung.java.kimchi.http;

import java.net.Proxy;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.Singular;
import lombok.ToString;
import seung.java.kimchi.util.SCharset;
import seung.java.kimchi.util.SLinkedHashMap;

/**
 * <pre>
 * http request value object
 * </pre>
 * 
 * @author seung
 * @since 2020.05.11
 */
@ToString
@Builder
@Setter
@Getter
public class SHttpRequest {

    @NonNull
    private String url;
    
    @Builder.Default
    private SRequestMethod requestMethod = SRequestMethod.GET;
    
    @Builder.Default
    private boolean useProxy = false;
    
    @Builder.Default
    private Proxy.Type proxyType = Proxy.Type.HTTP;
    
    private String proxyHostname;
    
    private int proxyPort;
    
    @Builder.Default
    private boolean followRedirects = true;
    
    @Builder.Default
    private boolean useCache = false;
    
    @Builder.Default
    private boolean doInput = true;
    
    @Builder.Default
    private boolean doOutput = false;
    
    @Builder.Default
    private int connectTimeout = 1000 * 3;
    
    @Builder.Default
    private int readTimeout = 1000 * 60;
    
    @Builder.Default
    @Setter(AccessLevel.NONE)
    private Map<String, List<String>> header = new LinkedHashMap<>();
    
    @Builder.Default
    private SCharset charset = SCharset.UTF_8;
    
//    @Builder.Default
//    @Setter(AccessLevel.NONE)
    @Singular("data")
    private List<Pair<String, String>> data;
    
    public void addHeader(String key, String value) {
        if(header.get(key) == null) {
            header.put(key, Arrays.asList(value));
        } else {
            header.get(key).add(value);
        }
    }
    
    public void addData(String key, String value) {
        data.add(Pair.of(key, value));
    }
    
    public SLinkedHashMap toSLinkedHashMap() throws JsonProcessingException {
        return new ObjectMapper()
                .setSerializationInclusion(Include.ALWAYS)
//                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
                .convertValue(this, SLinkedHashMap.class)
                ;
    }
    
    public String toJsonString(boolean isPretty) throws JsonProcessingException {
        return new ObjectMapper()
                .setSerializationInclusion(Include.ALWAYS)
//                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
                .configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true)
                .configure(SerializationFeature.INDENT_OUTPUT, isPretty)
                .writeValueAsString(this)
                ;
    }
    
}
