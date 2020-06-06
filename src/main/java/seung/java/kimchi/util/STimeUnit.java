package seung.java.kimchi.util;

import java.util.Arrays;
import java.util.List;

/**
 * <pre>
 * time units enum
 * </pre>
 * @author seung
 * @since 2020.05.11
 */
public enum STimeUnit {

    MILLISECOND("MS", "SSS"),
    SECOND("S", "ss"),
    MINUTE("m", "mm"),
    HOUR("H", "HH"),
    WEEK("W", "w"),
    DAY("D", "dd"),
    MONTH("M", "MM"),
    YEAR("Y", "yyyy")
    ;
    
    private final List<String> aliases;
    
    private STimeUnit(String... aliases) {
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
        for(STimeUnit sTimeUnit : STimeUnit.values()) {
            for(String compare : sTimeUnit.aliases()) {
                if(compare.equals(alias)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public static STimeUnit forAlias(String alias) {
        for(STimeUnit sTimeUnit : STimeUnit.values()) {
            for(String compare : sTimeUnit.aliases()) {
                if(compare.equals(alias)) {
                    return sTimeUnit;
                }
            }
        }
        return null;
    }
    
}
