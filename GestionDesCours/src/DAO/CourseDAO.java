package DAO;

import Models.Course;
import Models.CourseType;
import Application.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CourseDAO 
{
    
    private final CourseTypeDAO typeDAO = new CourseTypeDAO();
    
    public boolean add(Course course) 
    {
        String sql = "INSERT INTO courses (title, subject, type_id, teacher_id, course_file_path, deadline) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) 
        {
            
            stmt.setString(1, course.getTitle());
            stmt.setString(2, course.getSubject());
            stmt.setInt(3, course.getType().getId()); 
            stmt.setInt(4, course.getTeacherNcin());
            stmt.setString(5, course.getCourseFilePath());
            
            if (course.getDeadline() != null) 
            {
                stmt.setTimestamp(6, Timestamp.valueOf(course.getDeadline()));
            } 
            else {
                stmt.setTimestamp(6, null);
            }
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0) 
            {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) 
                {
                    if (generatedKeys.next()) 
                    {
                        course.setId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
            return false;
            
        }
        catch (SQLException e) 
        {
            System.err.println("Erreur SQL lors de l'ajout du cours: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public List<Course> getCoursesByTeacher(int teacherNcin) 
    {
        List<Course> courses = new ArrayList<>();
        
        String sql = "SELECT c.*, ct.name AS type_name, ct.requires_submission " +
                     "FROM courses c JOIN course_types ct ON c.type_id = ct.id " +
                     "WHERE c.teacher_id = ? ";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) 
        {
            
            stmt.setInt(1, teacherNcin);
            try (ResultSet rs = stmt.executeQuery()) 
            {
                while (rs.next()) 
                {
                    
                    CourseType type = new CourseType(rs.getInt("type_id"), rs.getString("type_name"), rs.getBoolean("requires_submission"));
                    
                    Timestamp deadlineTs = rs.getTimestamp("deadline");
                    LocalDateTime deadline = (deadlineTs != null) ? deadlineTs.toLocalDateTime() : null;
                    
                    Timestamp createdAtTs = rs.getTimestamp("created_at");
                    LocalDateTime createdAt = (createdAtTs != null) ? createdAtTs.toLocalDateTime() : null;
                    
                    Course course = new Course(rs.getInt("id"), rs.getString("title"), rs.getString("subject"), type, rs.getInt("teacher_id"), rs.getString("course_file_path"), deadline, createdAt);
                    courses.add(course);
                }
            }
        } catch (SQLException e) 
        {
            System.err.println("Erreur SQL lors de la récupération des cours: " + e.getMessage());
        }
        return courses;
    }
    
    public Course getById(int id) 
    {
    	//complete
        return null; 
    }
}