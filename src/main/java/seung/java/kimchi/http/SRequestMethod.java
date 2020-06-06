package seung.java.kimchi.http;

import java.util.Arrays;
import java.util.List;

/**
 * <pre>
 * http request methods enum
 * </pre>
 * @author seung
 * @since 2020.05.11
 */
public enum SRequestMethod {

    HEAD,
    OPTIONS,
    POST,
    GET,
    PUT,
    DELETE,
    CONNECT,
    TRACE,
    PATCH
    ;
    
    private final List<String> aliases;
    
    private SRequestMethod(String... aliases) {
        String[] clone = new String[aliases.length + 1];
        clone[0] = this.name();
        if(aliases.length > 0) {
            System.arraycopy(aliases, 0, clone, 1, aliases.length);
        }
        this.aliases = Arrays.asList(clone);
    }
    
    public String text() {
        return name();
    }
    
    public List<String> aliases() {
        return aliases;
    }
    
    public static boolean hasValue(String alias) {
        for(SRequestMethod sRequestMethod : SRequestMethod.values()) {
            for(String compare : sRequestMethod.aliases()) {
                if(compare.toLowerCase().equals(alias.toLowerCase())) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public static SRequestMethod forAlias(String alias) {
        for(SRequestMethod sRequestMethod : SRequestMethod.values()) {
            for(String compare : sRequestMethod.aliases()) {
                if(compare.toLowerCase().equals(alias.toLowerCase())) {
                    return sRequestMethod;
                }
            }
        }
        return null;
    }
    
}
