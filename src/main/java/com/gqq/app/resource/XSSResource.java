package com.gqq.app.resource;

import java.io.InputStream;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.springframework.stereotype.Component;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

@Path("/v1")
@Component
public class XSSResource {

    /**
     * Vuln Code. ReflectXSS http://localhost:8080/xss/reflect?xss=<script>alert(1)</script>
     *
     * @param xss unescape string
     */
    @GET
    @Path("/xss/reflect")
    @Produces(MediaType.APPLICATION_JSON)
    public Response reflectXss(@QueryParam("xss") String xss) {
        String res = String.format("{\"key\": \"%s\"}", xss);
        return Response.ok(res).build();
    }

    @POST
    @Path("/xss/reflect")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response reflectXssPost(Map<String, Object> requestPayload) {
        // Directly echo the input data (potential XSS vulnerability!)
        Map<String, Object> response = new HashMap<>();
        response.put("reflectedData", requestPayload);

        // Best practice: Always validate and sanitize input to avoid XSS vulnerabilities
        return Response.ok(response).build();
    }

    @GET
    @Path("/xss/readtest")
    public String testDocumentBuilderInstrumentation() {
        try {
            // DocumentBuilder的工厂类，专门用来生成DocumentBuilder
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            // 用来构造Document
            DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
            InputStream is = XSSResource.class.getClassLoader().getResourceAsStream("msg.xml");
            Document document = documentBuilder.parse(is);
            NodeList chapterList = document.getElementsByTagName("webSite");
            System.out.println("共获取到:" + chapterList.getLength());
            return "ok";
        } catch(Exception e) {
            e.printStackTrace();
        }
        return "fail";
    }

    @GET
    @Path("/xss/readnotworking")
    public String testDocumentBuilderInstrumentation2() {
        try {
            // DocumentBuilder的工厂类，专门用来生成DocumentBuilder
            String body = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<content>\n"
                + "    <webSite>https://www.roadjava.com</webSite>\n"
                + "</content>";
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new InputSource(new StringReader(body)));
            System.out.println("output" + doc.getDocumentElement().getTextContent());
            return "ok";
        } catch(Exception e) {
            e.printStackTrace();
        }
        return "fail";
    }
}