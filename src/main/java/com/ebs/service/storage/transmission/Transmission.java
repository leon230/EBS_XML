package com.ebs.service.storage.transmission;

/**
 * Created by Leon on 2016-11-29.
 */
public interface Transmission {
    public String sendTransmission(String urlParameters) throws Exception; //Returning http transmission status code
    public void sendTransmissionfILE(String urlParameters) throws Exception;
}
