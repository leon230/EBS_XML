package com.ebs.service.transmission;

import com.ebs.tools.GenerateXML;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.util.Calendar;

/**
 * Created by Leon on 2016-11-29.
 */
public class TransmissionImpl implements Transmission {
    @Autowired
    GenerateXML generateXML;
    private Calendar calendar;
    private final String USER_AGENT = "Mozilla/5.0";
    private String username = "ULE.INTEGRATION";
    private String pass = "CHANGEME";
    private String serverURL = "https://otmunlvdev4pub.oracleoutsourcing.com/GC3/glog.integration.servlet.WMServlet";
    private File fileto = new File("XMLtransmission.xml");

    @Override
    public String sendTransmission(String urlParameters) throws Exception{

        Authenticator.setDefault (new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication (username, pass.toCharArray());
            }
        });

        URL obj = new URL(serverURL);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        //add reuqest header
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Content-Type","application/xml");
        con.setRequestProperty("Connection", "Keep-Alive");
        con.setRequestProperty("Accept-Language", "en");

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(String.valueOf(urlParameters));
        wr.flush();
        wr.close();
//        int responseCode = con.getResponseCode();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        sendTransmissionfILE(urlParameters);
        //print result
//        System.out.println(response.toString());
        return response.toString();
    }

    @Override
    public void sendTransmissionfILE(String urlParameters) throws Exception {

        try (FileWriter fon = new FileWriter(fileto)) {
            fon.write(urlParameters);
        } catch (IOException exc) {
            System.out.println("File Error");
        }
    }
}
