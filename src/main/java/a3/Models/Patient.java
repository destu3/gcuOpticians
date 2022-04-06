package a3.Models;

public class Patient {
    String patient_id;
    String fname;
    String lname;
    String dob;
    int mobile_number;
    String username;
    String password;
    String role;

    public Patient() {
    }

    public Patient(String patient_id, String fname, String lname, String dob, int mobile_number, String username, String password, String role) {
        this.patient_id = patient_id;
        this.fname = fname;
        this.lname = lname;
        this.dob = dob;
        this.mobile_number = mobile_number;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public String getPatient_id() {
        return patient_id;
    }

    public String getFname() {
        return fname;
    }

    public String getLname() {
        return lname;
    }

    public String getDob() {
        return dob;
    }

    public int getMobile_number() {
        return mobile_number;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }
}
