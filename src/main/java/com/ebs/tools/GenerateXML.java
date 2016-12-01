package com.ebs.tools;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Leon on 2016-11-29.
 */
public class GenerateXML {

    private String xmlheader;
    private String xmlbody;
    private String xmlfooter;
    Calendar calnow;
    DateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss");
    private final String domain = "ULE/PR";
    private final String stoptype = "Stop Num";
    private final String userrole = "ULE CSS";
    private final String mail = "LUKASZ.HOMIK@UNILEVER.COM";
    private final String user = "ULE/PR.HOMIK_L_EBS";
    private final String pass = "L123";
    private final String rcid = "0001";
    //calnow = Calendar.getInstance();

    public String XMLgenerate(String tsp, String shid, String eventdate, String statuscode, String stopvalue){
        calnow = Calendar.getInstance();
        xmlheader = "";
        xmlbody = "";
        xmlfooter = "";

        xmlheader = xmlheader + "<?xml version=\"1.0\" encoding=\"UTF-8\"?><Transmission xmlns=\"http://xmlns.oracle.com/apps/otm\">";
        xmlheader = xmlheader + "<TransmissionHeader><UserName>" + user + "</UserName><Password>" + pass + "</Password><SenderTransmissionNo>" +
                dateFormat.format(calnow.getTime()) + "</SenderTransmissionNo>";
        xmlheader = xmlheader + "<AckSpec><ComMethodGid><Gid><Xid>EMAIL</Xid></Gid></ComMethodGid><EmailAddress>" + mail
                + "</EmailAddress><AckOption>ERROR</AckOption></AckSpec></TransmissionHeader><TransmissionBody>";
        xmlbody = xmlbody + "<GLogXMLElement><ShipmentStatus>";
        xmlbody = xmlbody + "<ServiceProviderAlias><ServiceProviderAliasQualifierGid><Gid><Xid>GLOG</Xid></Gid></ServiceProviderAliasQualifierGid><ServiceProviderAliasValue>";
        xmlbody = xmlbody + "ULE." + tsp + "</ServiceProviderAliasValue></ServiceProviderAlias>";
        xmlbody = xmlbody + "<ShipmentRefnum><ShipmentRefnumQualifierGid><Gid><Xid>GLOG</Xid></Gid></ShipmentRefnumQualifierGid>" + "<ShipmentRefnumValue>" +
                domain + "." + shid + "</ShipmentRefnumValue></ShipmentRefnum>";
        xmlbody = xmlbody + "<ShipmentStatusType>Shipment</ShipmentStatusType><StatusLevel>SHIPMENT</StatusLevel>";
        xmlbody = xmlbody + "<StatusCodeGid><Gid><DomainName>" + domain + "</DomainName><Xid>" + statuscode + "</Xid></Gid></StatusCodeGid>" +
                "<EventDt><GLogDate>" + eventdate + "</GLogDate></EventDt>";

        xmlbody = xmlbody + "<StatusReasonCodeGid><Gid><DomainName>" + domain + "</DomainName><Xid>" + rcid + "</Xid></Gid></StatusReasonCodeGid>" +
                "<SSRemarks>" + "" + "</SSRemarks>" + "<SSStop>";

        if (stoptype == "Stop Num") {
            xmlbody = xmlbody + "<SSStopSequenceNum>" + stopvalue + "</SSStopSequenceNum>";
        } else if (stoptype == "Location ID") {
            xmlbody = xmlbody + "<SSLocation><LocationRefnumQualifierGid><Gid><Xid>GLOG</Xid></Gid></LocationRefnumQualifierGid>";
            xmlbody = xmlbody + "<LocationID>" + stopvalue + "</LocationID>";
            xmlbody = xmlbody + "</SSLocation>";
        }

        xmlbody = xmlbody + "</SSStop>";
        xmlbody = xmlbody + "<ResponsiblePartyGid><Gid><DomainName>" + domain + "</DomainName><Xid>" + userrole + "</Xid></Gid></ResponsiblePartyGid>";
        xmlbody = xmlbody + "</ShipmentStatus></GLogXMLElement>";


        xmlfooter = xmlfooter + "</TransmissionBody></Transmission>";

        return xmlheader + xmlbody + xmlfooter;
    }
}
//TODO keep variables in properties file
//TODO create drop down lists in GUI