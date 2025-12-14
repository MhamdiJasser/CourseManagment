package DAO;

import Models.Matiere;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Application.DatabaseConnection;

public class MatiereDAO 
{
    public MatiereDAO() 
    {
        
    }

    public static List<Matiere> getAllMatieres() 
    {
        List<Matiere> matieres = new ArrayList<>();
        String query = "SELECT * FROM Matiere";
		
        try (Connection conn = DatabaseConnection.getConnection()) 
        {
        	Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) 
            {
                Matiere matiere = new Matiere(rs.getInt("id"), rs.getString("nom"));
                matieres.add(matiere);
            }

        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
        return matieres;
    }


    public boolean addMatiere(String nom) 
    {
        String query = "INSERT INTO Matiere(nom) VALUES (?)";
		
        try (Connection conn = DatabaseConnection.getConnection()) 
        {
        	PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, nom);
            return pst.executeUpdate() > 0;
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
            return false;
        }
    }
}
