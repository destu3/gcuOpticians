package a3.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class PatientDashboardController  {

    @FXML
    private Label fname_label;
    @FXML
    private Label id_label;
    @FXML
    private Label lname_label;

    public void retrievePatientInfo(String id, String fname, String lname){
        fname_label.setText(fname);
        lname_label.setText(lname);
        id_label.setText(id);
    }

}
