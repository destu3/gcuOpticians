package a3.Controllers;

import a3.DB_Utility.DBConnection;
import a3.gcuopticians.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
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
    @FXML
    private DatePicker date_picker;
    @FXML
    private ImageView logoImageView;
    @FXML
    private Button close_btn;

    Connection connection = null;
    PreparedStatement pst = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        connection = DBConnection.connectionToDatabase(); //establishes connection to db

        File logoFile = new File("Images/gculogo.png");
        Image logoImage = new Image(logoFile.toURI().toString());
        logoImageView.setImage(logoImage);
    }

    //method opens log in window
    public void openLoginWindow(ActionEvent event) throws Exception
    {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 445, 500);
        Stage loginStage = new Stage();
        loginStage.initStyle(StageStyle.UNDECORATED);
        loginStage.setScene(scene);
        loginStage.show();

        Stage stage = (Stage)close_btn.getScene().getWindow();
        stage.close();
    }

    //method return user bac kto the home page
    public void openHomeWindow(ActionEvent event) throws Exception
    {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("home.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 705, 458);
        Stage homeStage = new Stage();
        homeStage.initStyle(StageStyle.UNDECORATED);
        homeStage.setScene(scene);
        homeStage.show();

        Stage stage = (Stage)close_btn.getScene().getWindow();
        stage.close();
    }

    //this is the method that allows for patient registration, opticians follow a different procedure
    public void registerPatient(ActionEvent event){

        String insertSQL = "INSERT INTO Patients VALUES (?,?,?,?,?,?,?,?);";
        try{
            if (!(patientid_txt.getText().isEmpty() || fname_txt.getText().isEmpty() || lname_txt.getText().isEmpty() || date_picker.getValue() == null || mobile_txt.getText().isEmpty() || username_txt.getText().isEmpty() || password_txt.getText().isEmpty() || role_txt.getText().isEmpty()))
            {
                pst = connection.prepareStatement(insertSQL);
                pst.setString(1, patientid_txt.getText()); //What ever is typed into the username text field is passed to the prepare statement as the first arg for the sql querry
                pst.setString(2, fname_txt.getText()); //What ever is typed into the password text field is passed to the prepare statement as the second arg for the sql querry
                pst.setString(3,lname_txt.getText());
                pst.setString(4,date_picker.getValue().toString());
                pst.setString(5,mobile_txt.getText());
                pst.setString(6,username_txt.getText());
                pst.setString(7,password_txt.getText());
                pst.setString(8,role_txt.getText());
                pst.execute();

                Alert confirmed = new Alert(Alert.AlertType.INFORMATION);
                confirmed.setTitle("Registration successful");
                confirmed.setHeaderText("Welcome to the family. Log in Now");
                confirmed.setContentText(" (*^‿^*) ");
                confirmed.showAndWait();
            }
            else {
                Alert emptyFields = new Alert(Alert.AlertType.WARNING);
                emptyFields.setTitle("Warning Dialog");
                emptyFields.setHeaderText("You left some fields empty ٩(×̯×)۶");
                emptyFields.setContentText("Careful with the next step!");
                emptyFields.showAndWait();
            }
        }
        catch (Exception e)
        {
            e.getCause();
            e.printStackTrace();
        }
    }

    //method for closing window
    public void close(ActionEvent event){
        Stage stage = (Stage)close_btn.getScene().getWindow();
        stage.close();
    }
}
