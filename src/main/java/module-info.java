module a.gcuopticians {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.xerial.sqlitejdbc;


    opens a3.gcuopticians to javafx.fxml;
    exports a3.gcuopticians;
    exports a3.Controllers;
    exports a3.Models;
    opens a3.Controllers to javafx.fxml;
    opens a3.Models to javafx.fxml;
}