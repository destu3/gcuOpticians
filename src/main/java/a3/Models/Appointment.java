package a3.Models;

public class Appointment {
    String appointment_id;
    String patient_id;
    String optician_id;
    String status;
    String service;
    String time;
    String date;

    public Appointment() {
    }

    public Appointment(String appointment_id, String patient_id, String optician_id, String status, String service, String time, String date) {
        this.appointment_id = appointment_id;
        this.patient_id = patient_id;
        this.optician_id = optician_id;
        this.status = status;
        this.service = service;
        this.time = time;
        this.date = date;
    }

    public String getAppointment_id() {
        return appointment_id;
    }

    public String getPatient_id() {
        return patient_id;
    }

    public String getOptician_id() {
        return optician_id;
    }

    public String getStatus() {
        return status;
    }

    public String getService() {
        return service;
    }

    public String getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }
}
