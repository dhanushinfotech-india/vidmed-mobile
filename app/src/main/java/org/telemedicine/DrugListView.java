package org.telemedicine;

/**
 *Created by hareesh on 8/18/16.
*/

public class DrugListView {


    String drug,direct,dosage,frequency,instruction,days;



    public DrugListView(String drug, String direct, String dosage, String frequency,String instruction,String days){

        this.drug=drug;
        this.direct=direct;
        this.dosage=dosage;
        this.frequency=frequency;
        this.instruction=instruction;
        this.days=days;


    }

    public String getDrug() {
        return drug;
    }

    public String getDirect() {
        return direct;
    }

    public String getDosage() {
        return dosage;
    }

    public String getFrequency() {
        return frequency;
    }

    public String getInstruction() {
        return instruction;
    }

    public String getDays() {
        return days;
    }


}
