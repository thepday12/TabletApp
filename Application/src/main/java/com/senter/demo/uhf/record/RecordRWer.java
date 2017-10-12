package com.senter.demo.uhf.record;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class RecordRWer {
    private static final String path;

    static {
        String pathRoot1 = "/data/customer";
        String pathRoot2 = "/storage/emulated/legacy";
        if (new File(pathRoot1).exists()) {
            path = pathRoot1 + "/bcrds/";
        } else {
            path = pathRoot2 + "/bcrds/";
        }
    }

    public static final String path() {
        File pathFile = new File(path);
        if (pathFile.exists() == false) {
            pathFile.mkdirs();
            pathFile.mkdir();
        }
        return path;
    }

    public static final String suffixRecord = ".rfid";

    public static class XmlOper {
        public static final String xmlRecords = "RfidRecords";
        public static final String xmlRecord = "RfidRecord";
        public static final String xmlRecordAbtrTimes = "RfidTime";
        public static final String mapKey2Rfid = "0";
        public static final String mapKey2Times = "1";

        public static ArrayList<HashMap<String, String>> parseFile(String fullPathName) {
            ArrayList<HashMap<String, String>> rst = new ArrayList<HashMap<String, String>>();
            File f = new File(fullPathName);
            FileInputStream fis;
            try {
                fis = new FileInputStream(f);
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();

                org.w3c.dom.Document document = builder.parse(fis);
                org.w3c.dom.Element root = document.getDocumentElement();
                NodeList nl = root.getElementsByTagName(xmlRecord);
                for (int i = 0; i < nl.getLength(); i++) {
                    Node n = nl.item(i);
                    HashMap<String, String> map = new HashMap<String, String>();
                    try {
                        map.put(mapKey2Rfid, n.getFirstChild().getNodeValue());
                        try {
                            map.put(mapKey2Times, n.getAttributes().getNamedItem(xmlRecordAbtrTimes).getNodeValue());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        rst.add(map);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return rst;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            }
            return rst;
        }

        public static void saveRecode2File(String fullPathName, ArrayList<HashMap<String, String>> lm) throws IOException {
            Element records = new Element(xmlRecords);
            for (int i = 0; i < lm.size(); i++) {
                Map<String, String> map = lm.get(i);
                if (map != null) {
                    String value = map.get(mapKey2Rfid);
                    String times = map.get(mapKey2Times);
                    value = ((value != null) ? value : "");
                    times = ((times != null) ? times : "");
                    Element record = new Element(xmlRecord).setAttribute(xmlRecordAbtrTimes, times).setText(value);
                    records.addContent(record);
                }
            }
            Document document = new Document(records);

            XMLOutputter XMLOut = new XMLOutputter(org.jdom.output.Format.getCompactFormat().setEncoding("UTF-8").setIndent("    "));
            FileOutputStream fops = new FileOutputStream(fullPathName);
            XMLOut.output(document, fops);

            fops.close();
        }

    }
}
