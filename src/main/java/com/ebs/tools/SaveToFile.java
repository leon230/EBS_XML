package com.ebs.tools;

import com.ebs.model.Shipment;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

/**
 * Created by Lukasz.Homik on 2016-09-06.
 */
public class SaveToFile {
    private String fileTo;
    private List<Shipment> listToSave;

    public SaveToFile(List<Shipment> listToSave, String fileTo) {
        this.listToSave = listToSave;
        this.fileTo = fileTo;
    }


    public void createExcel()
    {
        String str = "Shipment id, TSP, RC, Stop number, Status, Status date, Error message, Ack, Transmission" + "\n";
        try
        {
            FileWriter fw = new FileWriter(fileTo);
            fw.write(str); //Header

            for (Shipment shipment: listToSave
                    ) {
                str = shipment.getId() + "," + shipment.getServiceProvider() + "," + shipment.getReasonCode() + "," + shipment.getStopNumber()
                        + "," + shipment.getUploadStatus() + "," + shipment.getReasonCodeDate() + "," + "\"" + shipment.getErrorMsg() +"\"" + "," + "\"" + shipment.getAck() + "\""
                        + "," + shipment.getTransmission();
                str = str +  "\n";
                fw.write(str);
            }

            fw.close();

        } catch(IOException exc) {
            System.out.println("I/O Error: " + exc);
        }

    }
    @Autowired
    public void saveToFiles(String mimeType, HttpServletResponse response){

        int BUFFER_SIZE = 4096;

        try {
            createExcel();
            File downloadFile = new File(fileTo);
            FileInputStream inputStream = new FileInputStream(downloadFile);

            // get MIME type of the file
            if (mimeType == null) {
                // set to binary type if MIME mapping not found
                mimeType = "application/octet-stream";
            }

            // set content attributes for the response
            response.setContentType(mimeType);
            response.setContentLength((int) downloadFile.length());

            // set headers for the response
            String headerKey = "Content-Disposition";
            String headerValue = String.format("attachment; filename=\"%s\"",
                    downloadFile.getName());
            response.setHeader(headerKey, headerValue);

            // get output stream of the response
            OutputStream outStream = response.getOutputStream();

            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead = -1;

            // write bytes read from the input stream into the output stream
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, bytesRead);
            }

            inputStream.close();
            outStream.close();
        }
        catch(Exception exc){
            System.out.println(exc.toString());
        }

    }


}
