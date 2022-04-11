package a3.DB_Utility;

import java.sql.*;

public class DBConnection {

    //establishes connection to database(Embedded)
    public static Connection connectionToDatabase(){
        try {
            Class.forName("org.sqlite.JDBC");//connects the sql driver link/library
            Connection con = DriverManager.getConnection("jdbc:sqlite:databases/OpticiansDB/gcuOpticians.db");
            return con;

        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Connection was a failure");
            return null;
        }
    }
}
