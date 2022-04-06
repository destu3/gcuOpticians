package a3.Controllers;

import a3.gcuopticians.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class HomeController implements Initializable {
    @FXML
    private Button close_btn;
    @FXML
    private Button login_btn;
    @FXML
    private Button registerNow_Btn;
    @FXML
    private ImageView logoImageView;


    //logos are created and displayed.
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        File logoFile = new File("Images/gculogo.png");
        Image logoImage = new Image(logoFile.toURI().toString());
        logoImageView.setImage(logoImage);
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

        Stage stage = (Stage) close_btn.getScene().getWindow();
        stage.close();
    }

    //method opens login window
    public void openLoginWindow(ActionEvent event) throws Exception
    {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 445, 500);
        Stage loginStage = new Stage();
        loginStage.initStyle(StageStyle.UNDECORATED);
        loginStage.setScene(scene);
        loginStage.show();

        Stage stage = (Stage) close_btn.getScene().getWindow();
        stage.close();
    }

    //method closes window
    public void closeWindow(ActionEvent event){
        Stage stage = (Stage) close_btn.getScene().getWindow();
        stage.close();
    }


}