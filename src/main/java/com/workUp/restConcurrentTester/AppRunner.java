package com.workUp.restConcurrentTester;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;

/**
 * Hello world!
 *
 */
public class AppRunner 
{
	String baseUrl;
    public static void main( String[] args )
    {
    	String input = "{\r\n" + 
				"        \"ISBN\":12345,\r\n" + 
				"        \"title\":\"title1\",\r\n" + 
				"        \"author\":\"author1\",\r\n" + 
				"        \"price\":100.00\r\n" + 
				"    } ";
    }
    
    class PostPutThread implements Callable<String>{

    	String requestBody,resourcePath,method;
    	public PostPutThread(String pResourcePath,String pRequestBody,String pMethod) {
    		
    		this.requestBody=pRequestBody;
    		this.resourcePath=pResourcePath;
    		this.method=pMethod;
    		
    	}
		public String call() throws Exception {
			
			//Setting request Url
			URL url = new URL(baseUrl+resourcePath);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			
			
			//Setting Request parameters
			conn.setRequestMethod(method);
			conn.setRequestProperty("Content-Type", "application/json");
			
			//Actual Sending to the destination
			if(requestBody != null)
			{
				OutputStream os = conn.getOutputStream();
				os.write(requestBody.getBytes());
				os.flush();
			}
			
			
			if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ conn.getResponseCode());
				}

			//Reading response 
			BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));

			String output;
			System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {
				System.out.println(output);
			}

			conn.disconnect();
			return output;
		}
    	
    }
    class GetThread implements Callable<String>{

    	String resourcePath;
    	public GetThread(String pResourcePath) {
    		
    		this.resourcePath=pResourcePath;
    		
    	}
		public String call() throws Exception {
			URL url = new URL(baseUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(
				(conn.getInputStream())));

			String output;
			System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {
				System.out.println(output);
			}

			conn.disconnect();
			return output;
		}
    	
    }
}
