package Application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection 
{
    public static Connection getConnection() throws SQLException 
    {
        String url = "jdbc:mysql://localhost:3306/cours_manager";
        String user = "root";
        String password = "";
        return DriverManager.getConnection(url, user, password);
    }
}