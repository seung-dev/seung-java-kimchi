package seung.java.kimchi.util;

import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import seung.java.kimchi.exception.SCastException;
import seung.java.kimchi.exception.SKimchiException;

/**
 * <pre>
 * advanced custom map
 * </pre>
 * @author seung
 * @since 2020.05.11
 */
@SuppressWarnings("rawtypes")
public class SLinkedHashMap extends LinkedHashMap {

    private static final long serialVersionUID = -7750824452201675922L;
    
    public SLinkedHashMap() {
    }
    @SuppressWarnings("unchecked")
    public SLinkedHashMap(Map m) {
        this.putAll(m);
    }
    @SuppressWarnings("unchecked")
    public SLinkedHashMap(String jsonString) throws SKimchiException {
        try {
            this.putAll(new ObjectMapper().readValue(jsonString, Map.class));
        } catch (JsonParseException e) {
            throw new SKimchiException(e);
        } catch (JsonMappingException e) {
            throw new SKimchiException(e);
        } catch (IOException e) {
            throw new SKimchiException(e);
        }
    }
    @SuppressWarnings("unchecked")
    public SLinkedHashMap(Object o) {
        this.putAll(asSLinkedHashMap(o));
    }
    
    @SuppressWarnings("unchecked")
    public SLinkedHashMap asSLinkedHashMap(Object o) {
        Field[] fields = o.getClass().getDeclaredFields();
        try {
            for(Field field : fields) {
                field.setAccessible(true);
                this.put(field.getName(), field.get(o));
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
        return this;
    }
    
    public String toJsonString() throws SKimchiException {
        return toJsonString(false);
    }
    public String toJsonString(boolean isPretty) throws SKimchiException {
        try {
            if(isPretty) {
                return new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(this);
            }
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new SKimchiException(e);
        }
    }
    
    @SuppressWarnings("unchecked")
    public SLinkedHashMap putMap(Map m) {
        super.putAll(m);
        return this;
    }
    
    public SLinkedHashMap putJsonString(String jsonString) throws SKimchiException {
        try {
            return putMap(new ObjectMapper().readValue(jsonString, Map.class));
        } catch (JsonParseException e) {
            throw new SKimchiException(e);
        } catch (JsonMappingException e) {
            throw new SKimchiException(e);
        } catch (IOException e) {
            throw new SKimchiException(e);
        }
    }
    
    public SLinkedHashMap putObject(Object o) {
        return putMap(asSLinkedHashMap(o));
    }
    
    public List<String> keyList() {
        
        List<String> keyList = new ArrayList<>();
        
        for(Object key : this.keySet()) {
            keyList.add(key == null ? "" : "" + key);
        }
        
        return keyList;
    }
    
    public boolean isEqual(Object key, Object object) {
        return get(key) == object;
    }
    
    public boolean isNull(Object key) {
        return get(key) == null;
    }
    
    public boolean isBlank(Object key) {
        return "".equals(getString(key, ""));
    }
    
    public boolean isEmpty(Object key) {
        return isNull(key) || isBlank(key);
    }
    
    public Object get(Object key, Object defaultValue) {
        return get(key) == null ? defaultValue : get(key);
    }
    
    public String getString(Object key) {
        return getString(key, null);
    }
    public String getString(Object key, String defaultValue) {
        Object value = get(key);
        if(value == null) {
            return defaultValue;
        }
        if(value instanceof String) {
            return "" + value;
        }
        if(value instanceof String[]) {
            String[] values = (String[]) value;
            return values[0];
        }
        if(value instanceof List) {
            List values = (List) value;
            if(values.size() == 0) {
                return defaultValue;
            }
            return "" + values.get(0);
        }
        return "" + get(key);
    }
    
    @SuppressWarnings("unchecked")
    public void appendString(Object key, String value) {
        super.put(key, getString(key, "") + value);
    }
    
    @SuppressWarnings("unchecked")
    public String[] getStringArray(Object key) {
        Object value = get(key);
        if(value == null) {
            return null;
        }
        if(value instanceof String) {
            String[] stringArray = {
                    "" + value
            };
            return stringArray;
        }
        if(value instanceof String[]) {
            return (String[]) value;
        }
        if(value instanceof List) {
            List values = (List) value;
            return (String[]) values.toArray(new String[values.size()]);
        }
        return null;
    }
    
    @SuppressWarnings("unchecked")
    public List<String> getStringList(Object key) {
        Object value = get(key);
        if(value == null) {
            return null;
        }
        if(value instanceof String) {
            return Arrays.asList(getString(key, null));
        }
        if(value instanceof String[]) {
            return Arrays.asList(getStringArray(key));
        }
        if(value instanceof List) {
            return (List) value;
        }
        return null;
    }
    public List<String> addStringList(Object key, String value) {
        getStringList(key).add(value);
        return getStringList(key);
    }
    
    public int getInt(Object key) throws SKimchiException {
        Object value = get(key);
        if(value instanceof Integer) {
            return (int) value;
        }
        if(!Pattern.matches("[0-9]+", getString(key, ""))) {
            throw new SKimchiException(
                    new SCastException(String.format(
                            "#%s# cannot be cast to int."
                            , getString(key, null)
                            ))
                    );
        }
        return Integer.parseInt(getString(key));
    }
    public int getInt(Object key, int defaultValue) throws SKimchiException {
        if(isNull(key)) {
            return defaultValue;
        }
        return getInt(key);
    }
    
    public boolean getBoolean(Object key) throws SKimchiException {
        String value = getString(key, null);
        if("true".equals(value)) {
            return true;
        }
        if("false".equals(value)) {
            return false;
        }
        throw new SKimchiException(
                new SCastException(String.format(
                        "#%s# cannot be cast to boolean."
                        , getString(key, null)
                        ))
                );
    }
    public boolean getIntAsBoolen(Object key) throws SKimchiException {
        return getInt(key) > 0 ? true : false;
    }
    
    public double getDouble(Object key) throws SKimchiException {
        Object value = get(key);
        if(value instanceof Double) {
            return (double) value;
        }
        if(!Pattern.matches("[0-9.]+", getString(key, ""))) {
            throw new SKimchiException(
                    new SCastException(String.format(
                            "#%s# cannot be cast to double."
                            , getString(key, null)
                            ))
                    );
        }
        return Double.parseDouble(getString(key, null));
    }
    public double getDouble(Object key, double defaultValue) throws SKimchiException {
        if(isNull(key)) {
            return defaultValue;
        }
        return getDouble(key);
    }
    
    public long getLong(Object key) throws SKimchiException {
        Object value = get(key);
        if(value instanceof Long) {
            return (long) value;
        }
        if(!Pattern.matches("[0-9.]+", getString(key, ""))) {
            throw new SKimchiException(
                    new SCastException(String.format(
                            "#%s# cannot be cast to long."
                            , getString(key, null)
                            ))
                    );
        }
        return Long.parseLong(getString(key, null));
    }
    public long getLong(Object key, long defaultValue) throws SKimchiException {
        if(isNull(key)) {
            return defaultValue;
        }
        return getLong(key);
    }
    
    public float getFloat(Object key) throws SKimchiException {
        Object value = get(key);
        if(value instanceof Float) {
            return (float) value;
        }
        if(!Pattern.matches("[0-9.]+", getString(key, ""))) {
            throw new SKimchiException(
                    new SCastException(String.format(
                            "#%s# cannot be cast to float."
                            , getString(key, null)
                            ))
                    );
        }
        return Float.parseFloat(getString(key, null));
    }
    public float getFloat(Object key, float defaultValue) throws SKimchiException {
        if(isNull(key)) {
            return defaultValue;
        }
        return getFloat(key);
    }
    
    public BigInteger getBigInteger(Object key) throws SKimchiException {
        Object value = get(key);
        if(value instanceof BigInteger) {
            return (BigInteger) value;
        }
        return BigInteger.valueOf(getLong(key));
    }
    public BigInteger getBigInteger(Object key, BigInteger defaultValue) throws SKimchiException {
        if(isNull(key)) {
            return defaultValue;
        }
        return getBigInteger(key);
    }
    
    public BigDecimal getBigDecimal(Object key) throws SKimchiException {
        Object value = get(key);
        if(value instanceof BigDecimal) {
            return (BigDecimal) value;
        }
        return BigDecimal.valueOf(getLong(key));
    }
    public BigDecimal getBigDecimal(Object key, BigDecimal defaultValue) throws SKimchiException {
        if(isNull(key)) {
            return defaultValue;
        }
        return getBigDecimal(key);
    }
    
    public Map getMap(Object key) throws SKimchiException {
        Object value = get(key);
        if(value == null) {
            return null;
        }
        if(value instanceof Map) {
            return (Map) value;
        }
        throw new SKimchiException(
                new SCastException(String.format(
                        "#%s# cannot be cast to Map."
                        , get(key).getClass().getName()
                        ))
                );
    }
    
    public LinkedHashMap getLinkedHashMap(Object key) throws SKimchiException {
        Object value = get(key);
        if(value == null) {
            return null;
        }
        if(value instanceof LinkedHashMap) {
            return (LinkedHashMap) value;
        }
        throw new SKimchiException(
                new SCastException(String.format(
                        "#%s# cannot be cast to LinkedHashMap."
                        , get(key).getClass().getName()
                        ))
                );
    }
    
    @SuppressWarnings("unchecked")
    public List<LinkedHashMap> getListLinkedHashMap(Object key) throws SKimchiException {
        try {
            return (List<LinkedHashMap>) get(key);
        } catch (Exception e) {
            throw new SKimchiException(e);
        }
    }
    
    public SLinkedHashMap getSLinkedHashMap(Object key) throws SKimchiException {
        Object value = get(key);
        if(value == null) {
            return null;
        }
        if(value instanceof SLinkedHashMap) {
            return (SLinkedHashMap) value;
        }
        throw new SKimchiException(
                new SCastException(String.format(
                        "#%s# cannot be cast to SLinkedHashMap."
                        , get(key).getClass().getName()
                        ))
                );
    }
    
    @SuppressWarnings("unchecked")
    public List<SLinkedHashMap> getListSLinkedHashMap(Object key) throws SKimchiException {
        try {
            return (List<SLinkedHashMap>) get(key);
        } catch (Exception e) {
            throw new SKimchiException(e);
        }
    }
    
    public List getList(Object key) throws SKimchiException {
        try {
            return (List) get(key);
        } catch (Exception e) {
            throw new SKimchiException(e);
        }
    }
    
    
}
