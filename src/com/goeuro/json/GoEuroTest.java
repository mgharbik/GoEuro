package com.goeuro.json;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class GoEuroTest {

    public static void main(String [] args) throws Exception{
    	
    	// number of arguments should be 1 
    	if (args.length != 1 ){
    		System.err.println("Usage: java -jar GoEuroTest.jar \"STRING\"");
    	}else{
    		// configure the SSLContext with a TrustManager
            SSLContext ctx = SSLContext.getInstance("TLS");
            ctx.init(new KeyManager[0], new TrustManager[] {new DefaultTrustManager()}, new SecureRandom());
            SSLContext.setDefault(ctx);

            String urlString = "https://api.goeuro.com/api/v1/suggest/position/en/name/" + args[0];
            
            /*
             * Other tests
             */
            // test with args[0]=empty.json
            //String urlString = "https://gist.github.com/simo163/55c19142b8d714ac1a50/raw/899d1bee02c7151d83bca1f67bf455689d82b22f/empty.json" + args[0];
            
            // test with args[0]=goeuro.json
            //String urlString = "https://gist.github.com/simo163/31948ce10970dbefdd5c/raw/ad45fa375ff041fc6a073c6e65d48ddddfc47932/" + args[0];
            
            
            // set the url and run the connection
            final URL url = new URL(urlString);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String arg0, SSLSession arg1) {	    
                    return true;
                }
            });
            
            
            JSONArray array = getJsonArray(url);
            
            generateCsvFile("test.csv", array);

            printArrayInConsole(array);
                
            conn.disconnect();
    	}
        
    }

    
	/*
	 * method: JSONArray
	 * 
	 */
    private static JSONArray getJsonArray(URL url) throws IOException {
    	Scanner scan;
		
		scan = new Scanner(url.openStream());
		String str = new String();
    	while (scan.hasNext())
    		str += scan.nextLine();
    	scan.close();
    	    
        JSONObject obj = (JSONObject) JSONValue.parse(str); 
        JSONArray array = (JSONArray)obj.get("results"); 
        return array;
	}    

    
	/*
	 * method: generateCsvFile
	 * 
	 */
	private static void generateCsvFile(String fileName, JSONArray array) {
		try
		{
		    FileWriter writer = new FileWriter(fileName);
	 
		    // the header
		    writer.append("_type");
		    writer.append(',');
		    writer.append("_id");
		    writer.append(',');
		    writer.append("name");
		    writer.append(',');
		    writer.append("type");
		    writer.append(',');
		    writer.append("latitude");
		    writer.append(',');
		    writer.append("longitude");
		    writer.append('\n');
	 
		    // the content
		    for (int i=0; i < array.size(); i++) { 
	            JSONObject country = (JSONObject) ((JSONObject)array.get(i)); 
	            writer.append(country.get("_type").toString());
			    writer.append(',');
			    writer.append(country.get("_id").toString());
			    writer.append(',');
			    writer.append(country.get("name").toString());
			    writer.append(',');
			    writer.append(country.get("type").toString());
			    writer.append(',');
			    
			    // extract the geo_position values
			    String geo = country.get("geo_position").toString();       
			    geo = geo.replaceAll("[^0-9.]+", " ");
			    List<String> data = Arrays.asList(geo.trim().split(" "));
			    writer.append(data.get(0));
			    writer.append(',');
			    writer.append(data.get(1));
			    writer.append('\n');
	          } 
		    writer.flush();
		    writer.close();
		}
		catch(IOException e)
		{
		     e.printStackTrace();
		}    
	}
	
	
	/*
	 * method: printArrayInConsole
	 * 
	 */
	private static void printArrayInConsole(JSONArray array) {
		if (array.size() == 0)
            System.out.println("The value of the results key is null or the url is incorrect"); 

		for (int i=0; i < array.size(); i++) { 
            JSONObject country = (JSONObject) ((JSONObject)array.get(i)); 
            System.out.println(country.get("_id")+": "+country.get("name")); 
          } 		
	}

	
	/*
	 * method: DefaultTrustManager
	 * 
	 */
	private static class DefaultTrustManager implements X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}

        @Override
        public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    }
}