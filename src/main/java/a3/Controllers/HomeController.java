package a3.Controllers;

import a3.gcuopticians.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class HomeController {
    @FXML
    private Button close_btn;

    @FXML
    private Button login_btn;

    @FXML
    private Button registerNow_Btn;

    public void openRegisterWindow() throws Exception
    {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("register.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 451, 655);
        Stage registerStage = new Stage();
        registerStage.setResizable(false);
        registerStage.setScene(scene);
        registerStage.show();
    }

    public void openLoginWindow() throws Exception
    {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 451, 469);
        Stage loginStage = new Stage();
        loginStage.setResizable(false);
        loginStage.setScene(scene);
        loginStage.show();
    }

    public void closeWindow(){
        Stage stage = (Stage) close_btn.getScene().getWindow();
        stage.close();
    }


}