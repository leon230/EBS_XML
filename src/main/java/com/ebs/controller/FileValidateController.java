package com.ebs.controller;

import com.ebs.model.Shipment;
import com.ebs.service.transmission.Transmission;
import com.ebs.service.transmission.TransmissionImpl;
import com.ebs.tools.CreateShipmentList;
import com.ebs.tools.GenerateXML;
import com.ebs.tools.SaveToFile;
import com.ebs.validation.FileValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

@Controller
public class FileValidateController {

    @Autowired
    private FileValidation fileValidation;
    @Autowired
    Transmission transmission;
    @Autowired
    private org.springframework.core.env.Environment environment;
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
            if (sh.getErrorMsg() == null || sh.getErrorMsg().isEmpty()) {
                xmlTransmissionText = generateXML.XMLgenerate(sh.getServiceProvider(), sh.getId(), sh.getReasonCodeDate(), sh.getReasonCode(), sh.getStopNumber());
                try {
                    sh.setAck(transmission.sendTransmission(xmlTransmissionText));
                    sh.setTransmission("Transmission successful");
                } catch (Exception e) {
                    sh.setAck("Transmission Error");
                }
            }
            else{
                sh.setTransmission("Transmission not sent due to template errors");
            }
        }
        redirectAttributes.addFlashAttribute("tableList",tableList);
        return "redirect:/transmissionReport";
    }
    @RequestMapping(value = "/transmissionReport", method = RequestMethod.GET)
    public String transmissionReport(RedirectAttributes redirectAttributes) {

        return "transmissionReport";
    }

    /**
     Export to excel file
     */
    @RequestMapping(value = "**/exportData", method = RequestMethod.GET)
    public String exportData(HttpServletRequest request,
                          HttpServletResponse response) {

        ServletContext context = request.getSession().getServletContext();
        String appPath = context.getRealPath("");
        String filePath = environment.getRequiredProperty("file.download.filename");
        String fullPath = appPath + filePath;
        String mimeType = context.getMimeType(fullPath);

        SaveToFile sv = new SaveToFile(tableList,fullPath);
        sv.saveToFiles(mimeType, response);

        return "transmissionReport";
    }
    public void setValidationList(String fileName){
        shList = new CreateShipmentList();
        shList.generateShipmentList(fileName);
        tableList = shList.getShipmentList();
    }

}
