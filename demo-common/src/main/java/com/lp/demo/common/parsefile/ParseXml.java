package com.lp.demo.common.parsefile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lp.demo.common.exception.DisplayableException;
import com.lp.demo.common.util.StringUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author lp
 * @create 2021/5/6 23:56
 * @description
 **/
public class ParseXml {
    public ParseXml() {
    }

    public static JSONObject toJSONObject(InputStream in) {
        if (null == in) {
            return null;
        } else {
            try {
                return (JSONObject)inputStream2Map(in, (Map)null);
            } catch (IOException var2) {
                throw new DisplayableException(var2.getMessage());
            }
        }
    }

    public static JSONObject toJSONObject(String content) {
        return toJSONObject(content, Charset.defaultCharset());
    }

    public static JSONObject toJSONObject(String content, Charset charset) {
        return StringUtil.isEmpty(content) ? null : toJSONObject(content.getBytes(charset));
    }

    public static JSONObject toJSONObject(byte[] content) {
        if (null == content) {
            return null;
        } else {
            try {
                InputStream in = new ByteArrayInputStream(content);
                Throwable var2 = null;

                JSONObject var3;
                try {
                    var3 = (JSONObject)inputStream2Map(in, (Map)null);
                } catch (Throwable var13) {
                    var2 = var13;
                    throw var13;
                } finally {
                    if (in != null) {
                        if (var2 != null) {
                            try {
                                in.close();
                            } catch (Throwable var12) {
                                var2.addSuppressed(var12);
                            }
                        } else {
                            in.close();
                        }
                    }

                }

                return var3;
            } catch (IOException var15) {
                throw new DisplayableException(var15.getMessage());
            }
        }
    }

    public static <T> T toBean(String content, Class<T> clazz) {
        if (null != content && !"".equals(content)) {
            try {
                InputStream in = new ByteArrayInputStream(content.getBytes("UTF-8"));
                Throwable var3 = null;

                Object var4;
                try {
                    var4 = inputStream2Bean(in, clazz);
                } catch (Throwable var14) {
                    var3 = var14;
                    throw var14;
                } finally {
                    if (in != null) {
                        if (var3 != null) {
                            try {
                                in.close();
                            } catch (Throwable var13) {
                                var3.addSuppressed(var13);
                            }
                        } else {
                            in.close();
                        }
                    }

                }

                return (T) var4;
            } catch (IOException var16) {
                throw new DisplayableException(var16.getMessage());
            }
        } else {
            return null;
        }
    }

    public static JSON getChildren(NodeList children) {
        JSON json = null;

        for(int idx = 0; idx < children.getLength(); ++idx) {
            Node node = children.item(idx);
            NodeList nodeList = node.getChildNodes();
            int length = nodeList.getLength();
            if (node.getNodeType() == 1 && length >= 1 && nodeList.item(0).hasChildNodes()) {
                if (null == json) {
                    json = new JSONObject();
                }

                JSONObject c;
                if (json instanceof JSONObject) {
                    c = (JSONObject)json;
                    if (c.containsKey(node.getNodeName())) {
                        JSONArray array = new JSONArray();
                        array.add(json);
                        json = array;
                    } else {
                        c.put(node.getNodeName(), getChildren(nodeList));
                    }
                }

                if (json instanceof JSONArray) {
                    c = new JSONObject();
                    c.put(node.getNodeName(), getChildren(nodeList));
                    ((JSONArray)json).add(c);
                }
            } else if (node.getNodeType() == 1) {
                if (null == json) {
                    json = new JSONObject();
                }

                ((JSONObject)json).put(node.getNodeName(), node.getTextContent());
            }
        }

        return (JSON)json;
    }

    public static DocumentBuilder newDocumentBuilder() throws ParserConfigurationException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        documentBuilderFactory.setFeature("http://xml.org/sax/features/external-general-entities", false);
        documentBuilderFactory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
        documentBuilderFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        documentBuilderFactory.setFeature("http://javax.xml.XMLConstants/feature/secure-processing", true);
        documentBuilderFactory.setXIncludeAware(false);
        documentBuilderFactory.setExpandEntityReferences(false);
        return documentBuilderFactory.newDocumentBuilder();
    }

    public static Document newDocument() throws ParserConfigurationException {
        return newDocumentBuilder().newDocument();
    }

    public static <T> T inputStream2Bean(InputStream in, Class<T> clazz) throws IOException {
        JSON json = toJSONObject(in);
        return json.toJavaObject(clazz);
    }

    public static Map inputStream2Map(InputStream in, Map m) throws IOException {
        if (null == m) {
            m = new JSONObject();
        }

        try {
            DocumentBuilder documentBuilder = newDocumentBuilder();
            Document doc = documentBuilder.parse(in);
            doc.getDocumentElement().normalize();
            NodeList children = doc.getDocumentElement().getChildNodes();

            for(int idx = 0; idx < children.getLength(); ++idx) {
                Node node = children.item(idx);
                NodeList nodeList = node.getChildNodes();
                int length = nodeList.getLength();
                if (node.getNodeType() == 1 && (length > 1 || length == 1 && nodeList.item(0).hasChildNodes())) {
                    ((Map)m).put(node.getNodeName(), getChildren(nodeList));
                } else if (node.getNodeType() == 1) {
                    ((Map)m).put(node.getNodeName(), node.getTextContent());
                }
            }
        } catch (Exception var12) {
            throw new DisplayableException("XML解析失败\n" + var12.getMessage());
        } finally {
            in.close();
        }

        return (Map)m;
    }

    public static String getMap2Xml(Map<String, Object> data) {
        return getMap2Xml(data, "xml", "UTF-8");
    }

    public static String getMap2Xml(Map<String, Object> data, String rootElementName, String encoding) {
        Document document = null;

        try {
            document = newDocument();
        } catch (ParserConfigurationException var12) {
            throw new DisplayableException(var12.getLocalizedMessage());
        }

        Element root = document.createElement(rootElementName);
        document.appendChild(root);
        map2Xml(data, document, root);

        try {
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            DOMSource source = new DOMSource(document);
            transformer.setOutputProperty("encoding", encoding);
            transformer.setOutputProperty("indent", "yes");
            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);
            transformer.transform(source, result);
            String output = writer.getBuffer().toString();
            return output;
        } catch (TransformerException var11) {
            var11.printStackTrace();
            return "";
        }
    }

    public static void map2Xml(Map<String, Object> data, Document document, Element element) {
        Iterator var3 = data.entrySet().iterator();

        while(var3.hasNext()) {
            Map.Entry<String, Object> entry = (Map.Entry)var3.next();
            Object value = entry.getValue();
            if (value == null) {
                value = "";
            }

            Element filed = document.createElement((String)entry.getKey());
            object2Xml(value, document, filed);
            element.appendChild(filed);
        }

    }

    private static void object2Xml(Object value, Document document, Element element) {
        if (value instanceof Map) {
            map2Xml((Map)value, document, element);
        } else if (value instanceof List) {
            List vs = (List)value;
            Iterator var4 = vs.iterator();

            while(var4.hasNext()) {
                Object v = var4.next();
                object2Xml(v, document, element);
            }
        } else {
            value = value.toString().trim();
            element.appendChild(document.createTextNode(value.toString()));
        }

    }

    public static void main(String[] args) {
        String text = "<datas><data><code>0</code><users><user><id>1</id><name>张三</name></user><user><id>2</id><name>张4</name></user></users></data></datas>";
        System.out.println(getMap2Xml(toJSONObject(text), "datas", "utf-8"));
    }
}
