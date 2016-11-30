package com.ebs.tools;

import com.ebs.model.Shipment;
import org.springframework.beans.factory.annotation.Autowired;
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


    @Autowired
    public void saveFile()
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

}
