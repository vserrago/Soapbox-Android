package com.example.soapbox;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;


public class HTTPTest extends AsyncTask<URI, Void, String> 
{
	public static String getRequest() {
        StringBuffer stringBuffer = new StringBuffer("");
        BufferedReader bufferedReader = null;
        System.out.println("HELLO");
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet();
//            HttpPost httpPost = new HttpPost();

            URI uri = new URI("http://www.reddit.com/user/Da-V-Man.json");
            httpGet.setURI(uri);
//            httpPost.setURI(uri);
            //httpGet.addHeader(BasicScheme.authenticate(
            //new UsernamePasswordCredentials("user", "password"),
            //HTTP.UTF_8, false));
            
            
            HttpResponse httpResponse = httpClient.execute(httpGet);
//            HttpResponse httpResponse = httpClient.execute(httpPost);            
            InputStream inputStream = httpResponse.getEntity().getContent();
            bufferedReader = new BufferedReader(new InputStreamReader(
                    inputStream));

            String readLine = bufferedReader.readLine();
            while (readLine != null) {
                stringBuffer.append(readLine);
                stringBuffer.append("\n");
                readLine = bufferedReader.readLine();
            }
        } catch (Exception e) {
            // TODO: handle exception
        	e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    // TODO: handle exception
                }
            }
        }
        System.out.println(stringBuffer.toString());
        return stringBuffer.toString();
    }

	public static String postRequest() {
        StringBuffer stringBuffer = new StringBuffer("");
        BufferedReader bufferedReader = null;
        System.out.println("HELLO");
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet();
//            HttpPost httpPost = new HttpPost();

            URI uri = new URI("http://www.reddit.com/user/Da-V-Man.json");
            httpGet.setURI(uri);
//            httpPost.setURI(uri);
            //httpGet.addHeader(BasicScheme.authenticate(
            //new UsernamePasswordCredentials("user", "password"),
            //HTTP.UTF_8, false));
            
            
            HttpResponse httpResponse = httpClient.execute(httpGet);
//            HttpResponse httpResponse = httpClient.execute(httpPost);            
            InputStream inputStream = httpResponse.getEntity().getContent();
            bufferedReader = new BufferedReader(new InputStreamReader(
                    inputStream));

            String readLine = bufferedReader.readLine();
            while (readLine != null) {
                stringBuffer.append(readLine);
                stringBuffer.append("\n");
                readLine = bufferedReader.readLine();
            }
        } catch (Exception e) {
            // TODO: handle exception
        	e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    // TODO: handle exception
                }
            }
        }
        System.out.println(stringBuffer.toString());
        return stringBuffer.toString();
    }
	
	@Override
	protected String doInBackground(URI... arg0) {
		return HTTPTest.getRequest();
	}
}
