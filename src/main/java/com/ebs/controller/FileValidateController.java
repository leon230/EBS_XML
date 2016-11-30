package com.ebs.controller;

import com.ebs.model.Shipment;
import com.ebs.service.storage.transmission.Transmission;
import com.ebs.service.storage.transmission.TransmissionImpl;
import com.ebs.tools.CreateShipmentList;
import com.ebs.tools.GenerateXML;
import com.ebs.validation.FileValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

@Controller
public class FileValidateController {

    @Autowired
    private FileValidation fileValidation;
    @Autowired
    Transmission transmission;

    @Autowired
    GenerateXML generateXML;

    private ArrayList<Shipment> tableList;
    private CreateShipmentList shList;

    @RequestMapping(value = "/validateFile", method = RequestMethod.GET)
    public Model validateFile(Model model, HttpServletRequest request,RedirectAttributes redirectAttributes) {
        String fileName = request.getParameter("filename");

/**
 * Validate if template has correct template name and perform Shipment object validation
 */
        if (fileValidation.validateTemplate(fileName)){
            setValidationList(fileName);
            redirectAttributes.addFlashAttribute("tableList",tableList);
            model.addAttribute("tableList",tableList);
            model.addAttribute("message", "Validation");
        }
        else
        {
            model.addAttribute("message", "Error: Incorrect template type");
        }
        return model;
    }
    @RequestMapping(value = "/sendTransmission", method = RequestMethod.GET)
    public String sendTransmission(RedirectAttributes redirectAttributes) {

        TransmissionImpl transmission = new TransmissionImpl();

        String xmlTransmissionText;

        for (Shipment sh: tableList) {
            System.out.println("Sending " + sh.getId());
            xmlTransmissionText = generateXML.XMLgenerate("ULE/PR.HOMIK_L_EBS","L123","LUKASZ.HOMIK@UNILEVER.COM","ULE" + "." + sh.getServiceProvider(),
                    "ULE/PR",sh.getId(),"0002","20161221000000", "0001","","Stop Num","1","ULE CSS");

            System.out.println(xmlTransmissionText);
            System.out.println("\n");
            try {
                sh.setAck(transmission.sendTransmission(xmlTransmissionText));
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }
        redirectAttributes.addFlashAttribute("tableList",tableList);
        return "redirect:/transmissionReport";
    }
    @RequestMapping(value = "/transmissionReport", method = RequestMethod.GET)
    public String transmissionReport(RedirectAttributes redirectAttributes) {

        return "transmissionReport";
    }


    public void setValidationList(String fileName){
        shList = new CreateShipmentList();
        shList.generateShipmentList(fileName);
        tableList = shList.getShipmentList();
    }

}
