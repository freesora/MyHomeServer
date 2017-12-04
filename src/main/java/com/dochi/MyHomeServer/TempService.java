package com.dochi.MyHomeServer;

import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.dochi.MyHomeServer.Domain.TempInfo;
import com.dochi.MyHomeServer.scheduler.BoilerRunner;
import com.dochi.MyHomeServer.scheduler.PropertyReader;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;

@Service
public class TempService {

	Logger logger = Logger.getLogger(TempService.class);

	@Value("${config_path}")
	private String configPath;
	
	

	public ArrayList<TempInfo> getTemperature() {

		ArrayList<TempInfo> tempArr = new ArrayList<TempInfo>();

		try {
			PropertyReader prop = new PropertyReader(configPath);

			for (int room = 1; room < 7; room++) {
				if (room == 5)
					continue;
				HttpResponse<InputStream> response = Unirest.get(prop.getResponseURL())
						.queryString("hkey", prop.getHkey()).queryString("hh_dong", prop.getHh_dong())
						.queryString("hh_ho", prop.getHh_ho()).queryString("no", room).asBinary();

				Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(response.getBody());
				XPath xpath = XPathFactory.newInstance().newXPath();
				String expression = "//boiler//boilerinfo";
				NodeList nodeList = (NodeList) xpath.compile(expression).evaluate(doc, XPathConstants.NODESET);
				String curTemp = null;
				String setTemp = null;
				if (nodeList != null) {
					for (int i = 0; nodeList != null && i < nodeList.getLength(); i++) {
						NodeList childNodes = nodeList.item(i).getChildNodes();
						for (int j = 0; j < childNodes.getLength(); j++) {
							Node tmpNode = childNodes.item(j);
							if (tmpNode.getNodeName().equals("curtemp")) {
								curTemp = tmpNode.getFirstChild().getNodeValue();
							} else if (tmpNode.getNodeName().equals("settemp")) {
								setTemp = tmpNode.getFirstChild().getNodeValue();
							}

						}

					}
					if (curTemp != null & setTemp != null) {
						TempInfo ti = new TempInfo(curTemp, setTemp);
						tempArr.add(ti);
					} else {
						TempInfo ti = new TempInfo("?", "?");
						tempArr.add(ti);
					}

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tempArr;
	}
}
