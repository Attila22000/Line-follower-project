package app;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;


//import javax.ws.rs.client.Client;
//import javax.ws.rs.client.ClientBuilder;
//import javax.ws.rs.client.Entity;
//import javax.ws.rs.client.Invocation.Builder;
//import javax.ws.rs.client.WebTarget;
//import javax.ws.rs.core.MediaType;

import lejos.hardware.Button;

public class Httptest {

	public static void main(String[] args) {
		
        System.out.println("Read some text from URL\n");
        System.out.println("Press any key to start");
        
        Button.waitForAnyPress();
        
        
        System.out.println("Press any key to FINISH");
        Button.waitForAnyPress();
	}

}