package seung.java.kimchi.xml;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import seung.java.kimchi.exception.SSaxBreakException;
import seung.java.kimchi.util.SLinkedHashMap;

public class SSaxDefaultHandler extends DefaultHandler {

    private boolean isSchema = true;
    
    private boolean isText = true;
    
    private boolean isItem = true;
    
    private String target = "";
    
    private int start = -1;
    
    private int end = -1;
    
    private boolean isFirst = true;
    
    private String path;
    
    private int index = 0;
    
    private List<String> schema = new ArrayList<>();
    
    private List<String> texts = new ArrayList<>();
    
    private StringBuffer text = new StringBuffer();
    
    private List<SLinkedHashMap> items = new ArrayList<>();
    
    private SLinkedHashMap item;
    
    public void isSchema(boolean isSchema) {
        this.isSchema = isSchema;
    }
    
    public void isText(boolean isText) {
        this.isText = isText;
    }
    
    public void isItem(boolean isItem) {
        this.isItem = isItem;
    }
    
    public void target(String target) {
        this.target = target;
    }
    
    public void start(int start) {
        this.start = start;
    }
    
    public void end(int end) {
        this.end = end;
    }
    
    public List<String> schema() {
        return this.schema;
    }
    
    public List<String> texts() {
        return this.texts;
    }
    
    public List<SLinkedHashMap> items() {
        return this.items;
    }
    
    @Override
    public void startElement(
            String uri
            , String localName
            , String qName
            , Attributes attributes
            ) throws SAXException {
        
        if(isFirst) {
            path = qName;
            isFirst = false;
            return;
        }
        
        path = String.format("%s.%s", path, qName);
        
        if(isSchema && !schema.contains(path)) {
            schema.add(path);
        }
        
        if(isText && target.equals(path)) {
            text.setLength(0);
        }
        
        if(isItem) {
            if(target.equals(path)) {
                item = new SLinkedHashMap();
                text.setLength(0);
            }
        }
        
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public void endElement(
            String uri
            , String localName
            , String qName
            ) throws SAXException {
        
        if(isText) {
            if(target.equals(path)) {
                texts.add(text.length() > 0 ? text.toString() : "");
                index++;
            }
        }
        
        path = path.replace(String.format(".%s", qName), "");
        
        if(isItem && item != null) {
            if(target.equals(path) && text.length() > 0) {
                item.put(qName, text.toString());
                text.setLength(0);
            } else if(!target.equals(path) && index >= start) {
                items.add(item);
                index++;
            }
        }
        
        if(end == index) {
            throw new SAXException(new SSaxBreakException(String.format("index(%d) meets end(%d).", index, end)));
        }
        
    }
    
    @Override
    public void characters(
            char[] ch
            , int start
            , int length
            ) throws SAXException {
        
        if(isText || isItem) {
            String content = String.copyValueOf(ch, start, length).trim();
            if(isText && target.equals(path)) {
                if(content.length() > 0) {
                    text.append(content);
                }
                return;
            } else if(isItem && item != null) {
                if(content.length() > 0) {
                    text.append(content);
                }
            }
        }
        
    }
    
}
