package com.ebs.validation;

import com.ebs.model.Shipment;
import com.ebs.tools.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import static java.time.LocalDate.*;

public class ShipmentValidation {

    @Autowired
    private Environment environment;

    public String FindError(Shipment sh){
        String errorMsg = "";

/**
    STATUS DATE VALIDATION
 */
        if (CheckDateFormat(sh.getReasonCodeDate())){
            errorMsg = errorMsg + "Date incorrect format (yyyyMMddHHmm), ";
        }
        else if (DateUtil.parse(sh.getReasonCodeDate()).isAfter(now())){
             errorMsg = errorMsg + "Date in the future, ";
         }

/**
 *  SERVICE PROVIDER VALIDATION
 */
        if(sh.getServiceProvider() == null || sh.getServiceProvider().isEmpty()){
                errorMsg = errorMsg + "TSP field empty, ";
         }
/**
 *  REASON CODE VALIDATION
 */
        if(sh.getReasonCode() == null || sh.getReasonCode().isEmpty()){
            errorMsg = errorMsg + "RC field empty, ";
        }
        if(sh.getUploadStatus() == null || sh.getUploadStatus().isEmpty()){
            errorMsg = errorMsg + "Status name field empty, ";
        }
/**
 *  SHIPMENT VALIDATION
 */
        if(sh.getId() == null || sh.getId().isEmpty()){
            errorMsg = errorMsg + "Shipment Id field empty, ";
        }
/**
 *  STOP NUMBER VALIDATION
 */
        if(sh.getStopNumber() == null || sh.getStopNumber().isEmpty()){
            errorMsg = errorMsg + "Stop number field empty, ";
        }

         if (errorMsg.isEmpty()){
            return errorMsg;
         }
         else{
             return errorMsg.substring(0,errorMsg.length()-2);
         }
    }

    private boolean CheckDateFormat(String dateString){

        if (dateString.length() != 12 ){
            return true;
        }
        try {
            for (int i = 0, n = dateString.length(); i < n; i++) {
                char c = dateString.charAt(i);

                if (c < 48 || c > 57) {
                    return true;
                }
            }
        }
        catch(Exception e){
            return true;
        }
        return false;
    }
}
