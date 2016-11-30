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
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
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
            if (sh.getErrorMsg() == null || sh.getErrorMsg().isEmpty()) {
                xmlTransmissionText = generateXML.XMLgenerate("ULE/PR.HOMIK_L_EBS", "L123", "LUKASZ.HOMIK@UNILEVER.COM", "ULE" + "." + sh.getServiceProvider(),
                        "ULE/PR", sh.getId(), "0002", "20161221000000", "0001", "", "Stop Num", "1", "ULE CSS");
                try {
                    sh.setAck(transmission.sendTransmission(xmlTransmissionText));
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
                          HttpServletResponse response) throws IOException {
        int BUFFER_SIZE = 4096;
        ServletContext context = request.getSession().getServletContext();
        String appPath = context.getRealPath("");
        String filePath = "/Export.csv";
        String fullPath = appPath + filePath;

        SaveToFile sv = new SaveToFile(tableList,fullPath);
        sv.saveFile();

        File downloadFile = new File(fullPath);
        FileInputStream inputStream = new FileInputStream(downloadFile);

        // get MIME type of the file
        String mimeType = context.getMimeType(fullPath);
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

        return "transmissionReport";
    }
    public void setValidationList(String fileName){
        shList = new CreateShipmentList();
        shList.generateShipmentList(fileName);
        tableList = shList.getShipmentList();
    }

}
