package edu.npu.capstone.capstone.Data;

/**
 * Created by Nick on 11/1/15.
 */
public class Contact {
    private String id;
    private String number;
    private String udid;
    private String firstName;
    private String lastName;


    public Contact(String id, String number, String udid, String firstName,String lastName) {
        this.id = id;
        this.number = number;
        this.udid =udid;
        this.firstName=firstName;
        this.lastName=lastName;
    }

    public String getName() {
        return firstName+" "+lastName;
    }

    public String getNumber() {
        return number;
    }

    public String getId() {
        return id;
    }

    public String getUdid() {
        return udid;
    }

}
