package com.ebs.tools;

import com.ebs.model.Shipment;
import com.ebs.model.Template;
import com.ebs.validation.ShipmentValidation;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by lukasz.homik on 2016-10-07.
 */
public class CreateShipmentList {
    private ArrayList<Shipment> shipmentList;

    /**
     *  Generates list of shipments according to Excel file
     */

    public void generateShipmentList(String fileName){
        Shipment sh;
        ShipmentValidation shValid = new ShipmentValidation();
        Template rf = new Template();
        rf.setTemplateFileName(fileName);
        rf.generateListExcel();
        ArrayList<Shipment> arrTemp = new ArrayList<>();

        for (Object item: rf.getArrList()
             ) {
                sh = new Shipment(((HashMap<Integer, String>) item).get(0),((HashMap<Integer, String>) item).get(1),((HashMap<Integer, String>) item).get(2)
                        ,((HashMap<Integer, String>) item).get(3),((HashMap<Integer, String>) item).get(4),((HashMap<Integer, String>) item).get(5)
                        ,((HashMap<Integer, String>) item).get(6),((HashMap<Integer, String>) item).get(7));
                sh.setErrorMsg(shValid.FindError(sh));

            arrTemp.add(sh);
        }

        this.setShipmentList(arrTemp);
    }

    public ArrayList<Shipment> getShipmentList() {
        return shipmentList;
    }

    public void setShipmentList(ArrayList<Shipment> shipmentList) {
        this.shipmentList = shipmentList;
    }
}
