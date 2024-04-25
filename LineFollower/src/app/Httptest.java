package app;

import java.io.BufferedReader;
import java.util.HashMap;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.sql.Date;
import java.sql.Time;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.HashMap;

//import javax.ws.rs.client.Client;
//import javax.ws.rs.client.ClientBuilder;
//import javax.ws.rs.client.Entity;
//import javax.ws.rs.client.Invocation.Builder;
//import javax.ws.rs.client.WebTarget;
//import javax.ws.rs.core.MediaType;

import lejos.hardware.Button;
import lejos.hardware.Sound;

public class Httptest {

	public static void main(String[] args) {
		
        System.out.println("Read some text from URL\n");
        System.out.println("Press any key to start");
        
        Button.waitForAnyPress();

        URL url = null;
  		HttpURLConnection conn = null;
  		InputStreamReader isr = null;
  		BufferedReader br=null;

  		String s=null;
		try {
			url = new URL("http://192.168.84.5:8080/rest/lego/getvalues");
			conn = (HttpURLConnection)url.openConnection();
  			System.out.println(conn.toString()); //Tulostaa vain URLin
			InputStream is=null;
			try {
				is=conn.getInputStream();
			}
			catch (Exception e) {
	  			System.out.println("Exception conn.getInputSteam()");
	  			e.printStackTrace();
	            System.out.println("Cannot get InputStream!");
			}
			isr = new InputStreamReader(is);
      		br=new BufferedReader(isr);
//			while ((s=br.readLine())!=null){
//				System.out.println(s);
//			}
      		String data = br.readLine();
      		System.out.println(data);
      		String dataVals[] = data.split("#");
      		int maxSpeed = Integer.parseInt(dataVals[0]);
      		double turningPercentage = Double.parseDouble(dataVals[1]);
      		float lowerThresh = Float.parseFloat(dataVals[2]);
      		float upperThresh = Float.parseFloat(dataVals[3]);
      		System.out.println(maxSpeed);
      		System.out.println(turningPercentage);
      		System.out.println(lowerThresh);
      		System.out.println(upperThresh);
      		System.out.println("End of data");
      		
		}
  		catch(Exception e) {
  			e.printStackTrace();
            System.out.println("Some problem!");
  		}
        System.out.println("Press any key to FINISH");
        Button.waitForAnyPress();
	}

}