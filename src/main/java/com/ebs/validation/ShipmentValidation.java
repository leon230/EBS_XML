package com.ebs.validation;

import com.ebs.model.Shipment;
import com.ebs.tools.DateUtil;
import static java.time.LocalDate.*;

public class ShipmentValidation {

    private String errorMsg = "";

    public String FindError(Shipment sh){

        errorMsg = "";

/**
    EARLY PICKUP DATE VALIDATION
 */

         if (CheckDateFormat(sh.getReasonCodeDate())){
            errorMsg = errorMsg + "Date incorrect format, ";
        }
        else if (DateUtil.parse(sh.getReasonCodeDate()).isAfter(now())){
             errorMsg = errorMsg + "Sate in the future, ";
         }

/**
 *  ADDITIONAL COST VALIDATION
 */
        if(sh.getServiceProvider() == null){
                errorMsg = errorMsg + "TSP incorrect, ";

         }

        return errorMsg;
    }

    private boolean CheckDateFormat(String dateString){

        if (dateString.length() < 10 ){
            return true;
        }
        try {
            for (int i = 0, n = dateString.length(); i < n; i++) {
                char c = dateString.charAt(i);

                if ((i != 4 && i != 7) && (Integer.parseInt(String.valueOf(c)) < 0 || Integer.parseInt(String.valueOf(c)) > 9)) {
                    return true;
                }
                if ((i == 4 || i == 7) && (c != '-')) {
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
