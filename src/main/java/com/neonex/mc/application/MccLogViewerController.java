package com.neonex.mc.application;

import com.neonex.mc.model.SearchCriteria;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Collections;

/**
 * Created by dennis on 2017-06-27.
 */
@Controller
public class MccLogViewerController {

    private static final Logger LOG = LoggerFactory.getLogger(MccLogViewerController.class);

    @Autowired
    private ConfigProperties properties;

    @RequestMapping("/logviewer")
    public String logviewerPage(Model model){
        return "logviewer";
    }

    @PostMapping("/logs")
    public ResponseEntity<?> requestMcc(@Valid @RequestBody SearchCriteria message, Errors errors) {

        String jsonData ="";
        try {
            //RestClient Initial (HttpHost instances)
            RestClient restClient = RestClient.builder(
                    new HttpHost("192.168.1.41",9200,"http")
            ).setMaxRetryTimeoutMillis(10000).build();

            //index a document
            HttpEntity entity = new NStringEntity(
                    "{\n" +
                            "    \"query\" : {\"match_all\" : {} }, \n"+
                            "    \"from\" : 0 , \n"+
                            "    \"size\" : 100  \n"+
                            "}", ContentType.APPLICATION_JSON);
            Response indexResponse = restClient.performRequest(
                    "GET",
                    "/logstash-"+message.getDate_input()+"/_search",
                    Collections.<String, String>emptyMap(),
                    entity);

            jsonData =EntityUtils.toString(indexResponse.getEntity());

            JSONObject obj = new JSONObject(jsonData);
            JSONObject hitsObj = new JSONObject(obj.getString("hits"));
            JSONArray hitsHitsArray = hitsObj.getJSONArray("hits");
            jsonData="<H2>TOTAL : <b>" + hitsObj.getString("total") +"</b></H2><br/><br/>";

            LOG.info("total:" + hitsObj.getString("total"));
            for (int i = 0; i < hitsHitsArray.length(); i++) {
                JSONObject iObj = hitsHitsArray.getJSONObject(i);
                JSONObject source = new JSONObject(iObj.getString("_source"));

                StringBuilder sb = new StringBuilder();
                sb.append("<font color='yellow'>[");
                sb.append(source.getString("timeStamp"));
                sb.append("]</font> ");
                sb.append(getComponentCodeString(source.getString("componentCode")));
                sb.append(" ");
                sb.append(getProgramCodeString(source.getString("programCode")));
                sb.append(" ");
                sb.append(getSeverityCodeString(source.getString("severityCode")));
                sb.append(" <font color='blue'>[MSGCODE:");
                sb.append(source.getString("msgCode"));
                sb.append("]</font> <font color='white'>");
                sb.append(source.getString("logMsg"));
                sb.append("</font> <br/>\n");

                LOG.info("====================================================");
                LOG.info("timeStamp :" + source.getString("timeStamp"));
                LOG.info("componentCode :" + source.getString("componentCode"));
                LOG.info("programCode :" + getProgramCodeString(source.getString("programCode")));
                LOG.info("severityCode :" + getSeverityCodeString(source.getString("severityCode")));
                LOG.info("msgCode :" + source.getString("msgCode"));
                LOG.info("logMsg :" + source.getString("logMsg"));
                LOG.info("host :" + source.getString("host"));
                LOG.info("source :" + source.getString("source"));
                LOG.info("input_type :" + source.getString("input_type"));

                jsonData += sb.toString();
            }
            restClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(jsonData);
    }

    public String getComponentCodeString(String val){
        String msg = "";
        if(val.equals("0"))
            msg= "[CONTROL]";
        else if(val.equals("1"))
            msg= "[NETWORK]";
        else if(val.equals("2"))
            msg= "[WRITE]";
        else if(val.equals("3"))
            msg= "[READ]";
        else if(val.equals("4"))
            msg= "[ACCESS]";
        else
            msg="[NULL";
        return msg;
    }

    public String getProgramCodeString(String val){
        String msg = "";
        if(val.equals("0"))
            msg= "[DLL]";
        else if(val.equals("1"))
            msg= "[JAR]";
        else
            msg="[NULL]";
        return msg;
    }

    public String getSeverityCodeString(String val){
        String msg = "";
        if(val.equals("0"))
            msg= "[FATAL]";
        else if(val.equals("1"))
            msg= "[ERROR]";
        else if(val.equals("2"))
            msg= "[WARN]";
        else if(val.equals("3"))
            msg= "[INFO]";
        else if(val.equals("4"))
            msg= "[DEBUG]";
        else
            msg="[NULL]";
        return msg;
    }

}
