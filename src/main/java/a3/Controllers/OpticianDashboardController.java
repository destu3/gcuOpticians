package a3.Controllers;

import a3.DB_Utility.DBConnection;
import a3.Models.Appointment;
import a3.gcuopticians.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Optional;
import java.util.ResourceBundle;

public class OpticianDashboardController implements Initializable {
    @FXML
    private TextField appointment_id;
    @FXML
    private Button close_btn;
    @FXML
    private TableColumn<Appointment, String> col_date;
    @FXML
    private TableColumn<Appointment, String> col_id;
    @FXML
    private TableColumn<Appointment, String> col_optician;
    @FXML
    private TableColumn<Appointment, String> col_patient;
    @FXML
    private TableColumn<Appointment, String>col_service;
    @FXML
    private TableColumn<Appointment, String> col_status;
    @FXML
    private TableColumn<Appointment, String> col_time;
    @FXML
    private ImageView logoImageView;
    @FXML
    private Button logout_btn;
    @FXML
    private Label optician_id;
    @FXML
    private Label optician_name;
    @FXML
    private TextField status_txt;
    @FXML
    private TableView<Appointment> table_appointments;
    @FXML
    private Button terminate_btn;
    @FXML
    private ComboBox<String> update_options;
    @FXML
    private TextField updated_status;

    Connection connection = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    int index;
    String opticianID;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        File logoFile = new File("Images/gculogo.png");
        Image logoImage = new Image(logoFile.toURI().toString());
        logoImageView.setImage(logoImage);
        connection = DBConnection.connectionToDatabase();

        update_options.getItems().add("On-going");
        update_options.getItems().add("Complete");
        update_options.setValue("On-going");
        update_options.setOnAction(this::getUpdateOption); // "::" is an operator that references a method

    }

    //shows optician account information and appointments they have been scheduled for based on logged in information
    public void retrieveOpticianInfo(String id,String name){

        opticianID = id;
        optician_name.setText("Hi there" + " " + name );
        optician_id.setText(id);

        col_patient.setCellValueFactory(new PropertyValueFactory<Appointment,String>("patient_id"));
        col_optician.setCellValueFactory(new PropertyValueFactory<Appointment,String>("optician_id"));
        col_date.setCellValueFactory(new PropertyValueFactory<Appointment,String>("date"));
        col_id.setCellValueFactory(new PropertyValueFactory<Appointment,String>("appointment_id"));
        col_service.setCellValueFactory(new PropertyValueFactory<Appointment,String>("service"));
        col_status.setCellValueFactory(new PropertyValueFactory<Appointment,String>("status"));
        col_time.setCellValueFactory(new PropertyValueFactory<Appointment,String>("time"));

        String getPatientAppointments = "SELECT appointment_id, Patients.fname, Opticians.name, status, service, time, date FROM Appointments INNER JOIN Patients ON Appointments.patient_id = Patients.patient_id INNER JOIN Opticians ON Appointments.optician_id = Opticians.optician_id WHERE Appointments.optician_id = ? ;";
        ObservableList<Appointment> opticiansScheduledAppointments = FXCollections.observableArrayList();

        try {
            pst = connection.prepareStatement(getPatientAppointments);
            pst.setString(1,id);
            rs = pst.executeQuery();

            while (rs.next()){
                opticiansScheduledAppointments.add(new Appointment(rs.getString("appointment_id"),rs.getString("fname"),rs.getString("name"),rs.getString("status"), rs.getString("service"),rs.getString("time"),rs.getString("date")));
                table_appointments.setItems(opticiansScheduledAppointments);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //method for selecting appointment that you want to terminate
    public void getSelectedAppointment(MouseEvent event) {
        index = table_appointments.getSelectionModel().getSelectedIndex();

        appointment_id.setText(col_id.getCellData(index));
        status_txt.setText(col_status.getCellData(index));
    }

    //method for terminating selected appointment
    public void terminateTreatment(ActionEvent event){
        String terminateQuery = "UPDATE Appointments SET status = ? WHERE ? = appointment_id;";
        String terminatedStatus = "Not-complete";

        if (!(appointment_id.getText().isEmpty() || status_txt.getText().isEmpty()))
        {
            try {
                PreparedStatement pst = connection.prepareStatement(terminateQuery);
                pst.setString(1,terminatedStatus);
                pst.setString(2,appointment_id.getText());

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Dialog");
                alert.setHeaderText("Treatment status will be terminated");
                alert.setContentText("Are you sure you want to do this?");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK){
                    pst.execute();
                    load();

                    Alert confirmed = new Alert(Alert.AlertType.INFORMATION);
                    confirmed.setTitle("Termination successful");
                    confirmed.setHeaderText("Treatment has been terminated");
                    confirmed.setContentText("Hope your feeling much better (*^‿^*) ");
                    confirmed.showAndWait();
                } else
                {
                    if (result.get() == ButtonType.CANCEL){
                        Alert cancelled = new Alert(Alert.AlertType.INFORMATION);
                        cancelled.setTitle("Termination unsuccessful");
                        cancelled.setHeaderText("Termination has been cancelled");
                        cancelled.setContentText("The show must go on (づ ◕‿◕ )づ ");
                        cancelled.showAndWait();
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }else {
            Alert emptyFields = new Alert(Alert.AlertType.WARNING);
            emptyFields.setTitle("Warning Dialog");
            emptyFields.setHeaderText("Unselected Appointment ٩(×̯×)۶");
            emptyFields.setContentText("Please select which appointment you want to terminate ");
            emptyFields.showAndWait();
        }
    }

    //method closes window
    public void close(ActionEvent event){
        Stage stage = (Stage)close_btn.getScene().getWindow();
        stage.close();
    }

    //method returns user back to the log in window
    public void logOut(ActionEvent event) throws Exception
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

    //method sets updated status value to selected drop-down value
    public void getUpdateOption(ActionEvent event){
        String option = update_options.getValue();
        updated_status.setText(option);
    }

    //method updates appointment status based on the selected update value and selected appointment
    public void update(ActionEvent event){
        String updateQuery = "UPDATE Appointments SET status = ? WHERE ? = appointment_id;";

        if ( !(appointment_id.getText().isEmpty() || status_txt.getText().isEmpty() || update_options.getValue() == null || updated_status.getText().isEmpty())){
            try {
                pst = connection.prepareStatement(updateQuery);
                pst.setString(1, updated_status.getText());
                pst.setString(2,appointment_id.getText());

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setHeaderText("Treatment status will be updated");
                alert.setTitle("Confirm update");
                alert.setContentText("Are you sure you want to update");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK){
                    pst.execute();
                    load();

                    Alert confirmed = new Alert(Alert.AlertType.INFORMATION);
                    confirmed.setTitle("Status update was successful");
                    confirmed.setHeaderText("Appointment has been updated");
                    confirmed.setContentText("Hope your feeling much better (*^‿^*) ");
                    confirmed.showAndWait();
                } else {
                    if (result.get() == ButtonType.CANCEL){
                        Alert cancelled = new Alert(Alert.AlertType.INFORMATION);
                        cancelled.setTitle("Update unsuccessful");
                        cancelled.setHeaderText("Update has been cancelled");
                        cancelled.setContentText("The show must go on (づ ◕‿◕ )づ ");
                        cancelled.showAndWait();
                    }
                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }
        else
        {
            Alert emptyFields = new Alert(Alert.AlertType.WARNING);
            emptyFields.setTitle("Warning Dialog");
            emptyFields.setHeaderText("Unselected Appointment and update value missing ٩(×̯×)۶");
            emptyFields.setContentText("Please select which appointment you want to update and choose an option ");
            emptyFields.showAndWait();
        }
    }

    //this method loads the table the view with up-to-date data after any changes to the data
    public void load(){

        col_patient.setCellValueFactory(new PropertyValueFactory<Appointment,String>("patient_id"));
        col_optician.setCellValueFactory(new PropertyValueFactory<Appointment,String>("optician_id"));
        col_date.setCellValueFactory(new PropertyValueFactory<Appointment,String>("date"));
        col_id.setCellValueFactory(new PropertyValueFactory<Appointment,String>("appointment_id"));
        col_service.setCellValueFactory(new PropertyValueFactory<Appointment,String>("service"));
        col_status.setCellValueFactory(new PropertyValueFactory<Appointment,String>("status"));
        col_time.setCellValueFactory(new PropertyValueFactory<Appointment,String>("time"));

        String getPatientAppointments = "SELECT appointment_id, Patients.fname, Opticians.name, status, service, time, date FROM Appointments INNER JOIN Patients ON Appointments.patient_id = Patients.patient_id INNER JOIN Opticians ON Appointments.optician_id = Opticians.optician_id WHERE Appointments.optician_id = ? ;";
        ObservableList<Appointment> opticiansScheduledAppointments = FXCollections.observableArrayList();

        try {
            pst = connection.prepareStatement(getPatientAppointments);
            pst.setString(1,opticianID);
            rs = pst.executeQuery();

            while (rs.next()){
                opticiansScheduledAppointments.add(new Appointment(rs.getString("appointment_id"),rs.getString("fname"),rs.getString("name"),rs.getString("status"), rs.getString("service"),rs.getString("time"),rs.getString("date")));
                table_appointments.setItems(opticiansScheduledAppointments);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
