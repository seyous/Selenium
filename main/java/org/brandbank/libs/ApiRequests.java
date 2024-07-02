package org.brandbank.libs;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.util.HashMap;
import java.util.Map;

public class ApiRequests {
    public CloseableHttpResponse getRequest(String url, HashMap<String,String>headerMap) throws Exception{
        try{
            CloseableHttpClient httpClient = HttpClients.createDefault();//connecting to server
            HttpGet httpget = new HttpGet(url); //http get request

            // for headers
            for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                httpget.addHeader(entry.getKey(), entry.getValue());
            }
            CloseableHttpResponse response=httpClient.execute(httpget);
            return response;
        }
        catch (Exception e){
            e.printStackTrace();
            throw  e;
        }
    }
    public CloseableHttpResponse postRequest(String url, String entityString,HashMap<String,String>headerMap) throws Exception{
        try{
            CloseableHttpClient httpClient = HttpClients.createDefault();//connecting to server
            HttpPost httpPost = new HttpPost(url);// https post request
            httpPost.setEntity(new StringEntity(entityString));//for payload

            //headers
            for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                httpPost.addHeader(entry.getKey(), entry.getValue());
            }
            CloseableHttpResponse response=httpClient.execute(httpPost);
            return response;


        }
        catch (Exception e){
            e.printStackTrace();
            throw  e;
        }
    }
    public CloseableHttpResponse deleteRequest(String url, HashMap<String,String>headerMap) throws Exception{
        try{
            CloseableHttpClient httpClient = HttpClients.createDefault();//connecting to server
            HttpDelete httpDelete = new HttpDelete(url); //http delete request

            // for headers
            for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                httpDelete.addHeader(entry.getKey(), entry.getValue());
            }
            CloseableHttpResponse response=httpClient.execute(httpDelete);
            return response;
        }
        catch (Exception e){
            e.printStackTrace();
            throw  e;
        }
    }



}
