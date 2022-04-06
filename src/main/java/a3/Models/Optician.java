package a3.Models;

public class Optician {
    String opticina_id;
    String name;
    String speciality;
    String username;
    String password;
    String role;

    public Optician() {
    }

    public Optician(String opticina_id, String name, String speciality, String username, String password, String role) {
        this.opticina_id = opticina_id;
        this.name = name;
        this.speciality = speciality;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public String getOpticina_id() {
        return opticina_id;
    }

    public String getName() {
        return name;
    }

    public String getSpeciality() {
        return speciality;
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
