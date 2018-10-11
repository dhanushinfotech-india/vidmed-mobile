package org.telemedicine.model;

/**
 * Created by Naveen.k on 8/10/2016.
 */
public class Patient {
    String firstName, lastName, age, phoneNo, emailId, address1, address2, town, state, gender, patientServerId, poc,lastupdated,valid_date,renewal_date;
    byte[] photo;


    public Patient(String firstName, String lastName, String age, String phoneNo, String emailId, String address1, String address2, String town, String state, String gender, byte[] photo, String patientServerId,String lastup,String valid_date,String renewal_date) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.phoneNo = phoneNo;
        this.emailId = emailId;
        this.address1 = address1;
        this.address2 = address2;
        this.town = town;
        this.state = state;
        this.gender = gender;
        this.photo = photo;
        this.patientServerId = patientServerId;
        this.lastupdated=lastup;
        this.valid_date=valid_date;
        this.renewal_date=renewal_date;

    }

    public String getRenewal_date() {
        return renewal_date;
    }

    public String getValid_date() {
        return valid_date;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAge() {
        return age;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public String getEmailId() {
        return emailId;
    }

    public String getAddress1() {
        return address1;
    }

    public String getAddress2() {
        return address2;
    }

    public String getTown() {
        return town;
    }

    public String getState() {
        return state;
    }

    public String getGender() {
//        Log.d("gendervalue","==="+gender);
        return gender;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public String getPatientServerId() {
        return patientServerId;
    }

    public Patient withPoc(String poc) {
        this.poc = poc;
        return this;
    }

    public String getPoc() {
        return poc;
    }

    public String getLastupdated() {
        return lastupdated;
    }
}
