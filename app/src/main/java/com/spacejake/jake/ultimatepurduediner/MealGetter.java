package com.spacejake.jake.ultimatepurduediner;

import android.os.AsyncTask;
import android.widget.Toast;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by jake on 11/21/14.
 */
public class MealGetter extends AsyncTask<URL, Void, Meal[]> {
	@Override
	protected Menu doInBackground(URL... urls) {
			SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser = null;
		try {
			parser = factory.newSAXParser();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
		HFSHandler handler = new HFSHandler();
		try {
			InputSource is = new InputSource (urls[0].openStream());
			is.setEncoding("UTF-8");
			parser.parse(is, handler);
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return handler.getMeals();
	}
}
