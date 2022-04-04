package a3.Controllers;

import a3.DB_Utility.DBConnection;
import a3.gcuopticians.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ResourceBundle;

public class RegisterController implements Initializable {

    @FXML
    private TextField dob_txt;
    @FXML
    private TextField fname_txt;
    @FXML
    private Button home_btn;
    @FXML
    private TextField lname_txt;
    @FXML
    private Button login_btn;
    @FXML
    private TextField mobile_txt;
    @FXML
    private PasswordField password_txt;
    @FXML
    private TextField patientid_txt;
    @FXML
    private Button register_btn;
    @FXML
    private TextField username_txt;
    @FXML
    private TextField role_txt;

    Connection connection = null;
    PreparedStatement pst = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        connection = DBConnection.connectionToDatabase(); //establishes connection to db
    }

    public void openLoginWindow() throws Exception
    {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        Stage loginStage = new Stage();
        loginStage.setResizable(false);
        loginStage.setScene(scene);
        loginStage.show();
    }

    public void openHomeWindow() throws Exception
    {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("home.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 728, 458);
        Stage homeStage = new Stage();
        homeStage.setResizable(false);
        homeStage.setScene(scene);
        homeStage.show();
    }

    public void registerPatient(){

        String insertSQL = "INSERT INTO Patients VALUES (?,?,?,?,?,?,?,?);";
        try{
            if (!(patientid_txt.getText().isEmpty() || fname_txt.getText().isEmpty() || lname_txt.getText().isEmpty() || dob_txt.getText().isEmpty() || mobile_txt.getText().isEmpty() || username_txt.getText().isEmpty() || password_txt.getText().isEmpty() || role_txt.getText().isEmpty()))
            {
                pst = connection.prepareStatement(insertSQL);
                pst.setString(1, patientid_txt.getText()); //What ever is typed into the username text field is passed to the prepare statement as the first arg for the sql querry
                pst.setString(2, fname_txt.getText()); //What ever is typed into the password text field is passed to the prepare statement as the second arg for the sql querry
                pst.setString(3,lname_txt.getText());
                pst.setString(4,dob_txt.getText());
                pst.setString(5,mobile_txt.getText());
                pst.setString(6,username_txt.getText());
                pst.setString(7,password_txt.getText());
                pst.setString(8,role_txt.getText());
                pst.execute();

                System.out.println("Successfully registered. Log in Now");
            }
            else {
                System.out.println("All fields must be filled");
            }

        }
        catch (Exception e)
        {
            e.getCause();
            e.printStackTrace();
        }

    }
}
