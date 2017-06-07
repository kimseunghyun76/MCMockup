package com.neonex.mc.application;

import com.neonex.mc.model.AjaxResponseBody;
import com.neonex.mc.model.MccParameter;
import com.neonex.mc.model.MessageCriteria;
import com.neonex.mc.model.ResponseVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.stream.Collectors;

/**
 * Created by dennis on 2017-05-23.
 */
@Controller
public class MccController {

    private static final Logger LOG = LoggerFactory.getLogger(MccController.class);

    @Autowired
    private ConfigProperties properties;

    @PostMapping("/mcc")
    public ResponseEntity<?> requestMcc(@Valid @RequestBody MessageCriteria message, Errors errors) {
        ResponseVo response = new ResponseVo();
        MccParameter param = new MccParameter(message);
        AjaxResponseBody result = new AjaxResponseBody();

        //If error, just return a 400 bad request, along with the error message
        if(errors.hasErrors()){
            result.setMsg(errors.getAllErrors()
            .stream().map(x -> x.getDefaultMessage())
            .collect(Collectors.joining(",")));
            return ResponseEntity.badRequest().body(result);
        }

        try {
            //creating a request with a header, a body to  server
            URL myURL = new URL(properties.getUrl());
            HttpURLConnection connection = (HttpURLConnection)myURL.openConnection();

            //add request header
            connection.setRequestMethod("POST");
            connection.setRequestProperty("User-Agent",properties.getUser_agent());
            connection.setRequestProperty("Accept-Charset",properties.getCharset());
            connection.setRequestProperty("dstMRN",param.getDstMRN());
            connection.setRequestProperty("srcMRN",param.getSrcMRN());

            //Send post request
            connection.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(wr,properties.getCharset()));
            wr.writeBytes(param.getS100());
            wr.flush();
            wr.close();

            //return
            int responseCode = connection.getResponseCode();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer stringBuffer = new StringBuffer();
            while((inputLine = in.readLine()) != null){
                stringBuffer.append(inputLine);
            }
            in.close();

            result.setMsg("success");
            response.setResponseCode(responseCode);
            response.setResponseMsg(stringBuffer.toString());

        }catch (Exception e){
            e.printStackTrace();
            result.setMsg("error");
            response.setResponseCode(400);
            response.setResponseMsg(e.getMessage());
        }
        result.setParam(param);
        result.setResult(response);

        return ResponseEntity.ok(result);
    }

    @PostMapping("/result")
    public ResponseEntity<?> handleRequest(RequestEntity<String> requestEntity) {
        HttpMethod method = requestEntity.getMethod();
        HttpHeaders headers = requestEntity.getHeaders();

        LOG.info("--------------------------------------------------------------------------");
        LOG.info("request method : {}",method);
        LOG.info("request url:  {}", requestEntity.getUrl());
        LOG.info("request body :  {}", requestEntity.getBody());
        LOG.info("request headers dstmrn :  {}", headers.get("dstmrn"));
        LOG.info("request headers srcmrn :  {}", headers.get("srcmrn"));
        LOG.info("--------------------------------------------------------------------------");

        StringBuilder sb = new StringBuilder();
        sb.append("dstmrn=").append(headers.get("dstmrn")).append("\n");
        sb.append("srcmrn=").append(headers.get("srcmrn")).append("\n");
        sb.append("s100=").append(requestEntity.getBody()).append("\n");
        ResponseEntity<String> responseEntity = new ResponseEntity<>("OK", HttpStatus.OK);
        return responseEntity;
    }

    @RequestMapping("/readLog")
    public ResponseEntity<?>  readRequestLog(@RequestParam("readCount") int readCount){
        String filename = "MccRequest.log";
        int allLineCount =0;
        StringBuilder sbLog = new StringBuilder();
        try {
            LineNumberReader  lnr = new LineNumberReader(new FileReader(new File(filename)));
            lnr.skip(Long.MAX_VALUE);
            allLineCount = lnr.getLineNumber() + 1;
            //Add 1 because line index starts at 0
            // Finally, the LineNumberReader object should be closed to prevent resource leak
            lnr.close();
        }catch (Exception e){
            e.printStackTrace();
        }

        if(readCount < allLineCount){
            try{
                FileInputStream fs = new FileInputStream(filename);
                BufferedReader br = new BufferedReader(new InputStreamReader(fs));
                String lineStr;
                int cnt =0 ;
                while((lineStr = br.readLine()) != null){
                    if(cnt > readCount) {
                        sbLog.append(lineStr).append("\n");
                    }
                    cnt++;
                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }

        ResponseEntity<String> responseEntity = new ResponseEntity<>(allLineCount+"|"+sbLog.toString(), HttpStatus.OK);
        return responseEntity ;
    }



    @RequestMapping("/removeLog")
    public ResponseEntity<?>  removeLogFile(){
        removeLog();
        ResponseEntity<String> responseEntity = new ResponseEntity<>("OK", HttpStatus.OK);
        return responseEntity ;
    }


    private void removeLog(){
        try{
            File file= new File(properties.getFilename());
            PrintWriter writer = new PrintWriter(file);
            writer.print("");
            writer.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
