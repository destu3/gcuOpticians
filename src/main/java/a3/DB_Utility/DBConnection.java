package a3.DB_Utility;

import java.sql.*;

public class DBConnection {

    //establishes connection
    public static Connection connectionToDatabase(){
        try {
            Class.forName("org.sqlite.JDBC");//connects the sql driver link/library
            Connection con = DriverManager.getConnection("jdbc:sqlite:gcuOpticians.db");
            System.out.println("Connection was successful");
            return con;

        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Connection was a failure");
            return null;
        }
    }


}
