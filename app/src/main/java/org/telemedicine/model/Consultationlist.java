package org.telemedicine.model;

/**
 * Created by bhavana on 3/15/17.
 */
public class Consultationlist {
    String  srno,patientname,typeofconsultation,docName , pocDate;

    public Consultationlist(String srno, String patientname, String typeofconsultation, String docName,  String pocdate) {
        this.srno = srno;
        this.patientname = patientname;
        this.typeofconsultation = typeofconsultation;
        this.docName = docName;
        this.pocDate=pocdate;
    }

    public String getSrno() {
        return srno;
    }


    public String getPatientname() {
        return patientname;
    }

    public String getTypeofconsultation() {
        return typeofconsultation;
    }



    public String getDocName() {
        return docName;
    }

    public String getPocDate() {
        return pocDate;
    }
}
