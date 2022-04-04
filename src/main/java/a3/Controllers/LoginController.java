package a3.Controllers;

import a3.DB_Utility.DBConnection;
import a3.gcuopticians.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Objects;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML
    private Button home_btn;
    @FXML
    private Button login_btn;
    @FXML
    private TextField password_txt;
    @FXML
    private Button register_btn;
    @FXML
    private TextField username_txt;
    @FXML
    private RadioButton patient_radio_btn;
    @FXML
    private RadioButton optician_radio_btn;

    Connection connection = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        connection = DBConnection.connectionToDatabase(); //establishes connection to db

        ToggleGroup roles = new ToggleGroup();
        patient_radio_btn.setToggleGroup(roles);
        optician_radio_btn.setToggleGroup(roles);
    }

    @FXML
    public void loginVerification(ActionEvent event)
    {
        String sqlQuerry = "SELECT * FROM Patients WHERE username = ? AND password = ?;";
        if(patient_radio_btn.isSelected()) {
            if (!username_txt.getText().isEmpty() && !password_txt.getText().isEmpty()){

                try{
                    pst = connection.prepareStatement(sqlQuerry);
                    pst.setString(1,username_txt.getText()); // 1st argument which is the first "?" in the querry, will be coming from the 2nd parameter(username) in the method.
                    pst.setString(2,password_txt.getText()); // 2nd argument which is the second "?" in the querry, will be coming from the 2nd parameter(password) in the method.
                    rs = pst.executeQuery(); //prepared querry is passed to reultset and executed

                    if (!rs.next()){
                        System.out.println("user does not exist, Register now");
                    }
                    else {
                        do {
                            String username = rs.getString("username");
                            String password = rs.getString("password");
                            String role = rs.getString("role");
                            String fname  = rs.getString("fname");
                            String lname = rs.getString("lname");
                            String patient_id = rs.getString("patient_id");

                            try {
                                FXMLLoader loader= new FXMLLoader(Main.class.getResource("patientDashboard.fxml"));
                                Parent root = loader.load();

                                PatientDashboardController patientDashboardController = loader.getController();
                                patientDashboardController.retrievePatientInfo(patient_id,fname,lname);

                                Stage pDashBrd = new Stage();
                                pDashBrd.setScene(new Scene(root));
                                pDashBrd.setResizable(false);
                                pDashBrd.setTitle("Your Appointments");
                                pDashBrd.show();

                                System.out.println(username + " " + password + " " + role);

                            }catch (IOException e)
                            {
                                e.printStackTrace();
                            }

                            if (Objects.equals(role, "Patient")){
                                System.out.println("user is a patient");
                            }
                        } while (rs.next());
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            else {
                System.out.println("All field need to be filled in");
            }
        }else if (optician_radio_btn.isSelected()){
            if (!username_txt.getText().isEmpty() && !password_txt.getText().isEmpty()){

                String OpticiansSqlQuerry = "SELECT * FROM Opticians WHERE username = ? AND password = ?;";
                try{
                    pst = connection.prepareStatement(OpticiansSqlQuerry);
                    pst.setString(1,username_txt.getText()); // 1st argument which is the first "?" in the querry, will be coming from the 2nd parameter(username) in the method.
                    pst.setString(2,password_txt.getText()); // 2nd argument which is the second "?" in the querry, will be coming from the 2nd parameter(password) in the method.
                    rs = pst.executeQuery(); //prepared querry is passed to reultset and executed

                    if (!rs.next()){
                        System.out.println("Optician does not exist");
                    }
                    else {
                        do {
                            String username = rs.getString("username");
                            String password = rs.getString("password");
                            String role = rs.getString("role");
                            String name = rs.getString("name");

                            System.out.println("Optician exists in database. Hooray!!");
                            System.out.println(name + " " + username + " " + password + " " + role);

                            if (Objects.equals(role,"Optician")){
                                System.out.println("An Optician has accessed the system");
                            }
                        } while (rs.next());
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            else {
                System.out.println("All field need to be filled in");
            }
        }else {
            System.out.println("Please select a role");
        }
    }
}
