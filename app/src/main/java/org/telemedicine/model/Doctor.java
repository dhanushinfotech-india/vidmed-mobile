package org.telemedicine.model;

/**
 * Created by Naveen.k on 2/15/2017.
 */

public class Doctor {
    private String docUserId, docWSID, docFullName;

    public Doctor(String docUserId, String docWSID, String docFullName) {
        this.docUserId = docUserId;
        this.docWSID = docWSID;
        this.docFullName = docFullName;
    }

    public String getDocUserId() {
        return docUserId;
    }

    public String getDocWSID() {
        return docWSID;
    }

    public String getDocFullName() {
        return docFullName;
    }

    public Doctor appendWSID(String docWSID) {
        if (!getDocWSID().contains(docWSID))
            this.docWSID = this.getDocWSID() + "," + docWSID;
        return this;
    }

    public boolean isValidDoctor(String pharmacistUserName) {
        return docUserId != null && !docUserId.equalsIgnoreCase(pharmacistUserName);
    }

    public boolean isSamePharamacist(String pharmacistUserName) {
        return docUserId.equalsIgnoreCase(pharmacistUserName);
    }
}

