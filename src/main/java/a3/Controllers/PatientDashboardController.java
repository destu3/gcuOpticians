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
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class PatientDashboardController implements Initializable  {

    @FXML
    private TableColumn<Appointment, String> col_date;
    @FXML
    private TableColumn<Appointment, String> col_id;
    @FXML
    private TableColumn<Appointment, String> col_optician;
    @FXML
    private TableColumn<Appointment, String> col_patient;
    @FXML
    private TableColumn<Appointment, String> col_service;
    @FXML
    private TableColumn<Appointment, String> col_status;
    @FXML
    private TableColumn<Appointment, String> col_time;
    @FXML
    private Label fullname_label;
    @FXML
    private Label id_label;
    @FXML
    private TableView<Appointment> table_appointments;
    @FXML
    private Button terminate_btn;
    @FXML
    private TextField time_txt;
    @FXML
    private ImageView logoImageView;
    @FXML
    private Button logout_btn;
    @FXML
    private TextField service_txt;
    @FXML
    private DatePicker date_picker;
    @FXML
    private Button book_btn;
    @FXML
    private Button close_btn;
    @FXML
    private TextField appointmentid_txt;
    @FXML
    private TextField status_txt;

    Connection connection = null;
    int index ;

    String patientId;
    final String defaultStatus = "Pending";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        File logoFile = new File("Images/gculogo.png");
        Image logoImage = new Image(logoFile.toURI().toString());
        logoImageView.setImage(logoImage);
        connection = DBConnection.connectionToDatabase();
    }

    //shows patient account information and appointments they have, based on logged in information
    public void retrievePatientInfo(String id, String fname, String lname, String dob, int mobile_number, String username, String password, String role){
        fullname_label.setText(fname + " " + lname);
        id_label.setText(patientId);
        patientId = id;

        col_patient.setCellValueFactory(new PropertyValueFactory<Appointment,String>("patient_id"));
        col_optician.setCellValueFactory(new PropertyValueFactory<Appointment,String>("optician_id"));
        col_date.setCellValueFactory(new PropertyValueFactory<Appointment,String>("date"));
        col_id.setCellValueFactory(new PropertyValueFactory<Appointment,String>("appointment_id"));
        col_service.setCellValueFactory(new PropertyValueFactory<Appointment,String>("service"));
        col_status.setCellValueFactory(new PropertyValueFactory<Appointment,String>("status"));
        col_time.setCellValueFactory(new PropertyValueFactory<Appointment,String>("time"));

        String getPatientAppointments = "SELECT appointment_id, Patients.fname, Opticians.name, status, service, time, date FROM Appointments INNER JOIN Patients ON Appointments.patient_id = Patients.patient_id INNER JOIN Opticians ON Appointments.optician_id = Opticians.optician_id WHERE Appointments.patient_id = ? ;";
        ObservableList<Appointment> patientAppointmentsList = FXCollections.observableArrayList();

        try {
            PreparedStatement pst = connection.prepareStatement(getPatientAppointments);
            pst.setString(1,id);
            ResultSet rs = pst.executeQuery();

            while (rs.next()){
                patientAppointmentsList.add(new Appointment(rs.getString("appointment_id"),rs.getString("fname"),rs.getString("name"),rs.getString("status"), rs.getString("service"),rs.getString("time"),rs.getString("date")));
                table_appointments.setItems(patientAppointmentsList);

                id_label.setText(id);

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //method returns random value from array list of opticians, used for scheduling opticians to appointments
    public String randomOptician(){
        ArrayList<String> opticians = new ArrayList<>(3);
        opticians.add("o1");
        opticians.add("o2");
        opticians.add("o3");

        int random = (int) (Math.random()*3);

        return opticians.get(random);
    }

    //method generates a random appointment id
    public String generateAppointmentID(){

        int random = (int) (Math.random()*100000);

        return "ap" + random;
    }

    //method allows users to book appointments
    public void bookAppointment(ActionEvent event){

        String bookAppointmentQuery = "INSERT INTO Appointments (appointment_id,patient_id,optician_id,status,service,time,date) VALUES (?,?,?,?,?,?,?);";

        if (date_picker.getValue() == null || time_txt.getText().isEmpty() || service_txt.getText().isEmpty())
        {
            Alert emptyFields = new Alert(Alert.AlertType.WARNING);
            emptyFields.setTitle("Warning Dialog");
            emptyFields.setHeaderText("You left some fields empty ٩(×̯×)۶");
            emptyFields.setContentText("Careful with the next step!");
            emptyFields.showAndWait();
        }else {
            try {
                PreparedStatement pst = connection.prepareStatement(bookAppointmentQuery);
                pst.setString(1,generateAppointmentID());
                pst.setString(2,patientId);
                pst.setString(3,randomOptician());
                pst.setString(4,defaultStatus);
                pst.setString(5,service_txt.getText());
                pst.setString(6,time_txt.getText());
                pst.setString(7,date_picker.getValue().toString());
                pst.execute();
                load();

                Alert bookingSuccessful = new Alert(Alert.AlertType.INFORMATION);
                bookingSuccessful.setTitle("Success");
                bookingSuccessful.setHeaderText("New Appointment Successfully booked");
                bookingSuccessful.setContentText("Please reload to see new appointment");
                bookingSuccessful.show();

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    //method for selecting appointment that you want to terminate
    public void getSelectedAppointment(MouseEvent event) {
        index = table_appointments.getSelectionModel().getSelectedIndex();

        appointmentid_txt.setText(col_id.getCellData(index));
        status_txt.setText(col_status.getCellData(index));
    }

    //method for terminating selected appointment
    public void terminateTreatment(ActionEvent event){
        String terminateQuery = "UPDATE Appointments SET status = ? WHERE ? = appointment_id;";
        String terminatedStatus = "Not-complete";

        if (!(appointmentid_txt.getText().isEmpty() || status_txt.getText().isEmpty()))
        {
            try {
                PreparedStatement pst = connection.prepareStatement(terminateQuery);
                pst.setString(1,terminatedStatus);
                pst.setString(2,appointmentid_txt.getText());

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
                } else {
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

    //method for closing window
    public void close(ActionEvent event){
        Stage stage = (Stage)close_btn.getScene().getWindow();
        stage.close();
    }

    //method returns patient back to log in window
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

    public void load(){

        try {
            String loadPatientAppointments = "SELECT appointment_id, Patients.fname, Opticians.name, status, service, time, date FROM Appointments INNER JOIN Patients ON Appointments.patient_id = Patients.patient_id INNER JOIN Opticians ON Appointments.optician_id = Opticians.optician_id WHERE Appointments.patient_id = ? ;";
            PreparedStatement pst = connection.prepareStatement(loadPatientAppointments);
            pst.setString(1,patientId);
            ResultSet rs = pst.executeQuery();

            col_patient.setCellValueFactory(new PropertyValueFactory<Appointment,String>("patient_id"));
            col_optician.setCellValueFactory(new PropertyValueFactory<Appointment,String>("optician_id"));
            col_date.setCellValueFactory(new PropertyValueFactory<Appointment,String>("date"));
            col_id.setCellValueFactory(new PropertyValueFactory<Appointment,String>("appointment_id"));
            col_service.setCellValueFactory(new PropertyValueFactory<Appointment,String>("service"));
            col_status.setCellValueFactory(new PropertyValueFactory<Appointment,String>("status"));
            col_time.setCellValueFactory(new PropertyValueFactory<Appointment,String>("time"));

            ObservableList<Appointment> patientAppointmentsList = FXCollections.observableArrayList();

            while (rs.next()){
                patientAppointmentsList.add(new Appointment(rs.getString("appointment_id"),rs.getString("fname"),rs.getString("name"),rs.getString("status"), rs.getString("service"),rs.getString("time"),rs.getString("date")));
                table_appointments.setItems(patientAppointmentsList);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
