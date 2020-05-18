package seung.java.kimchi.util;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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

/**
 * <pre>
 * advanced custom map
 * </pre>
 * @author seung
 * @since 2020.05.11
 */
@SuppressWarnings("rawtypes")
public class SLinkedHashMap extends LinkedHashMap<String, Object> {

	private static final long serialVersionUID = -7750824452201675922L;
	
	private ObjectMapper objectMapper = new ObjectMapper();
	
	public SLinkedHashMap() {
	}
	@SuppressWarnings("unchecked")
	public SLinkedHashMap(Map m) {
		this.putAll(m);
	}
	@SuppressWarnings("unchecked")
	public SLinkedHashMap(String jsonString) throws JsonParseException, JsonMappingException, IOException {
		this.putAll(objectMapper.readValue(jsonString, Map.class));
	}
	public SLinkedHashMap(Object o) {
		this.putAll(asSLinkedHashMap(o));
	}
	
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
	public Object asObject(Object object) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		Method[] methods    = null;
		String   methodName = "";
		for(String key : this.keySet()) {
			
			methodName = "set" + key.substring(0, 1).toUpperCase() + (key.length() > 1 ? key.substring(1) : "");
			
			methods = object.getClass().getDeclaredMethods();
			for(Method method : methods) {
				if(methodName.equals(method.getName())) {
					if(this.get(key) != null) method.invoke(object, this.get(key));
					else method.invoke(object, "");
				}
			}
		}
		
		return object;
	}
	
	public String toJsonString() throws JsonProcessingException {
		return toJsonString(false);
	}
	public String toJsonString(boolean isPretty) throws JsonProcessingException {
		if(isPretty) {
			return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
		}
		return objectMapper.writeValueAsString(this);
	}
	
	@SuppressWarnings("unchecked")
	public SLinkedHashMap putMap(Map m) {
		super.putAll(m);
		return this;
	}
	
	public SLinkedHashMap putJsonString(String jsonString) throws JsonParseException, JsonMappingException, IOException {
		return putMap(objectMapper.readValue(jsonString, Map.class));
	}
	
	public SLinkedHashMap putObject(Object o) {
		return putMap(asSLinkedHashMap(o));
	}
	
	public List<String> keyList() {
		
		List<String> keyList = new ArrayList<String>();
		
		for(String key : this.keySet()) {
			keyList.add(key);
		}
		
		return keyList;
	}
	
	public boolean isEqual(String key, Object object) {
		return get(key) == object;
	}
	
	public boolean isNull(String key) {
		return get(key) == null;
	}
	
	public boolean isBlank(String key) {
		return "".equals(getString(key, ""));
	}
	
	public boolean isEmpty(String key) {
		return isNull(key) && isBlank(key);
	}
	
	public Object get(Object key, Object defaultValue) {
		return get(key) == null ? defaultValue : get(key);
	}
	
	public String getString(String key) {
		return getString(key, null);
	}
	public String getString(String key, String defaultValue) {
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
	
	public void appendString(String key, String value) {
		super.put(key, getString(key, "") + value);
	}
	
	@SuppressWarnings("unchecked")
	public String[] getStringArray(String key) {
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
	public List<String> getStringList(String key) {
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
	public List<String> addStringList(String key, String value) {
		getStringList(key).add(value);
		return getStringList(key);
	}
	
	public int getInt(String key) throws SCastException {
		Object value = get(key);
		if(value instanceof Integer) {
			return (int) value;
		}
		if(!Pattern.matches("[0-9]+", getString(key, ""))) {
			throw new SCastException(String.format(
					"#%s# cannot be cast to int."
					, getString(key, null)
					));
		}
		return Integer.parseInt(getString(key));
	}
	public int getInt(String key, int defaultValue) throws SCastException {
		if(isNull(key)) {
			return defaultValue;
		}
		return getInt(key);
	}
	
	public boolean getBoolean(String key) throws SCastException {
		String value = getString(key, null);
		if("true".equals(value)) {
			return true;
		}
		if("false".equals(value)) {
			return false;
		}
		throw new SCastException(String.format(
				"#%s# cannot be cast to boolean."
				, getString(key, null)
				));
	}
	
	public double getDouble(String key) throws SCastException {
		Object value = get(key);
		if(value instanceof Double) {
			return (double) value;
		}
		if(!Pattern.matches("[0-9.]+", getString(key, ""))) {
			throw new SCastException(String.format(
					"#%s# cannot be cast to double."
					, getString(key, null)
					));
		}
		return Double.parseDouble(getString(key, null));
	}
	public double getDouble(String key, double defaultValue) throws SCastException {
		if(isNull(key)) {
			return defaultValue;
		}
		return getDouble(key);
	}
	
	public long getLong(String key) throws SCastException {
		Object value = get(key);
		if(value instanceof Long) {
			return (long) value;
		}
		if(!Pattern.matches("[0-9.]+", getString(key, ""))) {
			throw new SCastException(String.format(
					"#%s# cannot be cast to long."
					, getString(key, null)
					));
		}
		return Long.parseLong(getString(key, null));
	}
	public long getLong(String key, long defaultValue) throws SCastException {
		if(isNull(key)) {
			return defaultValue;
		}
		return getLong(key);
	}
	
	public float getFloat(String key) throws SCastException {
		Object value = get(key);
		if(value instanceof Float) {
			return (float) value;
		}
		if(!Pattern.matches("[0-9.]+", getString(key, ""))) {
			throw new SCastException(String.format(
					"#%s# cannot be cast to float."
					, getString(key, null)
					));
		}
		return Float.parseFloat(getString(key, null));
	}
	public float getFloat(String key, float defaultValue) throws SCastException {
		if(isNull(key)) {
			return defaultValue;
		}
		return getFloat(key);
	}
	
	public BigInteger getBigInteger(String key) throws SCastException {
		Object value = get(key);
		if(value instanceof BigInteger) {
			return (BigInteger) value;
		}
		return BigInteger.valueOf(getLong(key));
	}
	public BigInteger getBigInteger(String key, BigInteger defaultValue) throws SCastException {
		if(isNull(key)) {
			return defaultValue;
		}
		return getBigInteger(key);
	}
	
	public BigDecimal getBigDecimal(String key) throws SCastException {
		Object value = get(key);
		if(value instanceof BigDecimal) {
			return (BigDecimal) value;
		}
		return BigDecimal.valueOf(getLong(key));
	}
	public BigDecimal getBigDecimal(String key, BigDecimal defaultValue) throws SCastException {
		if(isNull(key)) {
			return defaultValue;
		}
		return getBigDecimal(key);
	}
	
	public Map getMap(String key) throws SCastException {
		Object value = get(key);
		if(value == null) {
			return null;
		}
		if(value instanceof Map) {
			return (Map) value;
		}
		throw new SCastException(String.format(
				"#%s# cannot be cast to Map."
				, get(key).getClass().getName()
				));
	}
	
	public SLinkedHashMap getSMap(String key) throws SCastException {
		Object value = get(key);
		if(value == null) {
			return null;
		}
		if(value instanceof SLinkedHashMap) {
			return (SLinkedHashMap) value;
		}
		throw new SCastException(String.format(
				"#%s# cannot be cast to SMap."
				, get(key).getClass().getName()
				));
	}
	
	@SuppressWarnings("unchecked")
	public List<SLinkedHashMap> getListSMap(String key) throws SCastException {
		try {
			return (List<SLinkedHashMap>) get(key);
		} catch (Exception e) {
			throw new SCastException(String.format(
					"#%s# cannot be cast to List SMap."
					, get(key).getClass().getName()
					));
		}
	}
	
	public List getList(String key) throws SCastException {
		try {
			return (List) get(key);
		} catch (Exception e) {
			throw new SCastException(String.format(
					"#%s# cannot be cast to List."
					, get(key).getClass().getName()
					));
		}
	}
	
	
}
