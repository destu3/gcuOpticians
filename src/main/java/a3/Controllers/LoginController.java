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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
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
    @FXML
    private ImageView logoImageView;
    @FXML
    private Button close_btn;

    Connection connection = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        connection = DBConnection.connectionToDatabase(); //establishes connection to db

        ToggleGroup roles = new ToggleGroup();
        patient_radio_btn.setToggleGroup(roles);
        optician_radio_btn.setToggleGroup(roles);

        File logoFile = new File("Images/gculogo.png");
        Image logoImage = new Image(logoFile.toURI().toString());
        logoImageView.setImage(logoImage);
    }

    //method authenticates users to access their accounts based on their roles (Patient and optician)
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
                        Alert doesNotExist = new Alert(Alert.AlertType.WARNING);
                        doesNotExist.setTitle("No User found");
                        doesNotExist.setHeaderText("User was not found ٩(×̯×)۶");
                        doesNotExist.setContentText("Register Now");
                        doesNotExist.showAndWait();
                    }
                    else {
                        do {
                            String username = rs.getString("username");
                            String password = rs.getString("password");
                            String role = rs.getString("role");
                            String fname  = rs.getString("fname");
                            String lname = rs.getString("lname");
                            String patient_id = rs.getString("patient_id");
                            String dob = rs.getString("dob");
                            int mobile_number = rs.getInt("mobile_number");

                            try {
                                FXMLLoader loader= new FXMLLoader(Main.class.getResource("patientDashboard.fxml"));
                                Parent root = loader.load();

                                PatientDashboardController patientDashboardController = loader.getController();
                                patientDashboardController.retrievePatientInfo(patient_id,fname,lname,dob,mobile_number,username,password,role);

                                Stage pDashBrd = new Stage();
                                pDashBrd.setScene(new Scene(root));
                                pDashBrd.initStyle(StageStyle.UNDECORATED);
                                pDashBrd.setTitle("Your Appointments");
                                pDashBrd.show();

                                Stage stage = (Stage) login_btn.getScene().getWindow();
                                stage.close();

                            }catch (IOException e)
                            {
                                e.printStackTrace();
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
                Alert emptyFields = new Alert(Alert.AlertType.WARNING);
                emptyFields.setTitle("Warning Dialog");
                emptyFields.setHeaderText("You left some fields empty ٩(×̯×)۶");
                emptyFields.setContentText("Careful with the next step!");
                emptyFields.showAndWait();
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
                        Alert doesNotExist = new Alert(Alert.AlertType.WARNING);
                        doesNotExist.setTitle("Member of staff not found");
                        doesNotExist.setHeaderText("Optician was not found ٩(×̯×)۶");
                        doesNotExist.showAndWait();
                    }
                    else {
                        do {
                            String username = rs.getString("username");
                            String password = rs.getString("password");
                            String role = rs.getString("role");
                            String name = rs.getString("name");
                            String optician_id  = rs.getString("optician_id");

                            try {
                                FXMLLoader fxmlloader= new FXMLLoader(Main.class.getResource("opticianDashboard.fxml"));
                                Parent root1 = fxmlloader.load();

                                OpticianDashboardController opticianDashboardController = fxmlloader.getController();
                                opticianDashboardController.retrieveOpticianInfo(optician_id,name);

                                Stage oDashBrd = new Stage();
                                oDashBrd.setScene(new Scene(root1));
                                oDashBrd.initStyle(StageStyle.UNDECORATED);
                                oDashBrd.setTitle("Optician Dashboard");
                                oDashBrd.show();

                            }catch (Exception e){
                                e.printStackTrace();
                            }

                            if (Objects.equals(role,"Optician")){
                                System.out.println("An Optician logged in");
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
                Alert emptyFields = new Alert(Alert.AlertType.WARNING);
                emptyFields.setTitle("Warning Dialog");
                emptyFields.setHeaderText("You left some fields empty ٩(×̯×)۶");
                emptyFields.setContentText("Careful with the next step!");
                emptyFields.showAndWait();
            }
        }else {
            Alert emptyFields = new Alert(Alert.AlertType.WARNING);
            emptyFields.setTitle("No role selected");
            emptyFields.setHeaderText("You must pick 1 role ٩(×̯×)۶");
            emptyFields.setContentText("!");
            emptyFields.showAndWait();
        }
    }

    //method opens home window
    public void openHomeWindow(ActionEvent event) throws Exception
    {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("home.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 705, 458);
        Stage homeStage = new Stage();
        homeStage.initStyle(StageStyle.UNDECORATED);
        homeStage.setScene(scene);
        homeStage.show();

        Stage stage = (Stage) login_btn.getScene().getWindow();
        stage.close();
    }

    //method opens register window
    public void openRegisterWindow(ActionEvent event) throws Exception
    {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("register.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 558, 644);
        Stage registerStage = new Stage();
        registerStage.initStyle(StageStyle.UNDECORATED);
        registerStage.setScene(scene);
        registerStage.show();

        Stage stage = (Stage) login_btn.getScene().getWindow();
        stage.close();
    }

    //method closes window
    public void close(ActionEvent event){
        Stage stage = (Stage)close_btn.getScene().getWindow();
        stage.close();
    }
}
