package seung.java.kimchi.xml;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import seung.java.kimchi.util.SKimchiException;

public class SSaxParser {

	private SSaxParser() {}
	
	public static SXml parseSchema(byte[] data) throws SKimchiException {
		return parseSchema(new ByteArrayInputStream(data));
	}
	public static SXml parseSchema(InputStream inputStream) throws SKimchiException {
		return parse(inputStream, true, false, false, "", -1, -1);
	}
	
	public static SXml parse(
			byte[] data
			, boolean isSchema
			, boolean isText
			, boolean isItem
			, String target
			, int start
			, int end
			) throws SKimchiException {
		return parse(new ByteArrayInputStream(data), isSchema, isText, isItem, target, start, end);
	}
	
	public static SXml parse(
			InputStream inputStream
			, boolean isSchema
			, boolean isText
			, boolean isItem
			, String target
			, int start
			, int end
			) throws SKimchiException {
		
		SXml sXml = null;
		
		SSaxDefaultHandler sSaxDefaultHandler = new SSaxDefaultHandler();
		sSaxDefaultHandler.isSchema(isSchema);
		sSaxDefaultHandler.isText(isText);
		sSaxDefaultHandler.isItem(isItem);
		sSaxDefaultHandler.target(target);
		sSaxDefaultHandler.start(start);
		sSaxDefaultHandler.end(end);
		
		try {
			
			SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
			SAXParser saxParser = saxParserFactory.newSAXParser();
			saxParser.parse(inputStream, sSaxDefaultHandler);
			
		} catch (IOException | SAXException | ParserConfigurationException e) {
			if(e.getCause() instanceof SKimchiException) {
				// no operation
			} else {
				throw new SKimchiException(e);
			}
		} finally {
			sXml = SXml.builder()
					.schema(sSaxDefaultHandler.schema())
					.text(sSaxDefaultHandler.texts())
					.item(sSaxDefaultHandler.items())
					.build()
					;
		}
		
		return sXml;
	}
	
	public static SXml parse2(
			byte[] data
			, boolean isSchema
			, boolean isText
			, boolean isItem
			, String target
			, int start
			, int end
			) throws SKimchiException {
		
		SXml sXml = null;
		
		SSaxContentHandler sSaxContentHandler = new SSaxContentHandler();
		sSaxContentHandler.isSchema(isSchema);
		sSaxContentHandler.isText(isText);
		sSaxContentHandler.isItem(isItem);
		sSaxContentHandler.target(target);
		sSaxContentHandler.start(start);
		sSaxContentHandler.end(end);
		
		try {
			
			SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
			SAXParser saxParser = saxParserFactory.newSAXParser();
			XMLReader xmlReader = saxParser.getXMLReader();
			xmlReader.setContentHandler(sSaxContentHandler);
			xmlReader.parse(new InputSource(new ByteArrayInputStream(data)));
			
		} catch (IOException | SAXException | ParserConfigurationException e) {
			if(e.getCause() instanceof SKimchiException) {
				// no operation
			} else {
				throw new SKimchiException(e);
			}
		} finally {
			sXml = SXml.builder()
					.schema(sSaxContentHandler.schema())
					.text(sSaxContentHandler.texts())
					.item(sSaxContentHandler.items())
					.build()
					;
		}
		
		return sXml;
	}
	
}
