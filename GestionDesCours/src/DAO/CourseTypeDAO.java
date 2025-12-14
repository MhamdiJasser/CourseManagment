package DAO;

import Models.CourseType;
import Application.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CourseTypeDAO 
{
    public boolean add(CourseType type) 
    {
        String sql = "INSERT INTO course_types (name, requires_submission) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) 
        {
            
            stmt.setString(1, type.getName());
            stmt.setBoolean(2, type.isRequiresSubmission());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0) 
            {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) 
                {
                    if (generatedKeys.next()) 
                    {
                        type.setId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
            return false;
            
        } catch (SQLException e) 
        {
            System.err.println("Erreur SQL lors de l'ajout du type de cours: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public List<CourseType> getAll() 
    {
        List<CourseType> types = new ArrayList<>();
        String sql = "SELECT id, name, requires_submission FROM course_types ORDER BY name";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql))
        {
            
            while (rs.next()) 
            {
                CourseType type = new CourseType(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getBoolean("requires_submission")
                );
                types.add(type);
            }
            
        } catch (SQLException e) 
        {
            System.err.println("Erreur SQL lors de la récupération des types de cours: " + e.getMessage());
        }
        return types;
    }
  
    public boolean update(CourseType type) 
    {
        String sql = "UPDATE course_types SET name = ?, requires_submission = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) 
        {
            
            stmt.setString(1, type.getName());
            stmt.setBoolean(2, type.isRequiresSubmission());
            stmt.setInt(3, type.getId());
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) 
        {
            System.err.println("Erreur SQL lors de la mise à jour du type de cours: " + e.getMessage());
            return false;
        }
    }

    public boolean delete(int id) 
    {
        String sql = "DELETE FROM course_types WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) 
        {
            
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
            
        } 
        catch (SQLException e) 
        {
            System.err.println("Erreur SQL lors de la suppression du type de cours. Est-il lié à des cours existants ? " + e.getMessage());
            return false;
        }
    }
}