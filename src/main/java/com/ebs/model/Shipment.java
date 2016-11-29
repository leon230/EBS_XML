package com.ebs.model;

/**
 * Created by lukasz.homik on 2016-09-13.
 */
public class Shipment {
    private String id;
    private String ServiceProvider;
    private String ReasonCode;
    private String stopNumber;
    private String uploadStatus;
    private String reasonCodeDate;
    private String ack;
    private String transmission;
    private String errorMsg;

    public Shipment(String id, String serviceProvider, String reasonCode, String stopNumber, String uploadStatus, String reasonCodeDate) {
        this.id = id;
        this.ServiceProvider = serviceProvider;
        this.ReasonCode = reasonCode;
        this.stopNumber = stopNumber;
        this.uploadStatus = uploadStatus;
        this.reasonCodeDate = reasonCodeDate;
    }

    public Shipment(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getServiceProvider() {
        return ServiceProvider;
    }

    public void setServiceProvider(String serviceProvider) {
        ServiceProvider = serviceProvider;
    }

    public String getReasonCode() {
        return ReasonCode;
    }

    public void setReasonCode(String reasonCode) {
        ReasonCode = reasonCode;
    }

    public String getStopNumber() {
        return stopNumber;
    }

    public void setStopNumber(String stopNumber) {
        this.stopNumber = stopNumber;
    }

    public String getUploadStatus() {
        return uploadStatus;
    }

    public void setUploadStatus(String uploadStatus) {
        this.uploadStatus = uploadStatus;
    }

    public String getReasonCodeDate() {
        return reasonCodeDate;
    }

    public void setReasonCodeDate(String reasonCodeDate) {
        this.reasonCodeDate = reasonCodeDate;
    }

    public String getAck() {
        return ack;
    }

    public void setAck(String ack) {
        this.ack = ack;
    }

    public String getTransmission() {
        return transmission;
    }

    public void setTransmission(String transmission) {
        this.transmission = transmission;
    }

    public String getErrorMsg() {
        return errorMsg;
    }
    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }


}
