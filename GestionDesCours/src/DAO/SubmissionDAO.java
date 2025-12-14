package DAO;

import Models.Submission;
import Application.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SubmissionDAO {

    public List<Submission> getSubmissionsByCourse(int courseId) {
        List<Submission> submissions = new ArrayList<>();
        String sql = "SELECT s.*, u.full_name AS student_name " +
        
                     "FROM submissions s " +
                     "JOIN users u ON s.student_id = u.NCIN " +  // Use NCIN (uppercase as shown in your image)
                     "WHERE s.course_id = ? AND u.role = 'etudiant'";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, courseId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Submission sub = new Submission();
                sub.setId(rs.getInt("id"));
                sub.setCourseId(rs.getInt("course_id"));
                sub.setStudentId(rs.getInt("student_id"));
                sub.setStudentName(rs.getString("student_name"));
                sub.setSubmissionFilePath(rs.getString("submission_file_path"));
                sub.setSubmittedAt(rs.getTimestamp("submitted_at").toLocalDateTime());
                sub.setGrade(rs.getObject("grade") != null ? rs.getFloat("grade") : null);
                sub.setFeedback(rs.getString("feedback"));
                sub.setLate(rs.getBoolean("is_late"));

                submissions.add(sub);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return submissions;
    }
    
    public boolean updateSubmissionGrade(Submission submission) {
        String sql = "UPDATE submissions SET grade = ?, feedback = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            if (submission.getGrade() != null) {
                stmt.setFloat(1, submission.getGrade());
            } else {
                stmt.setNull(1, java.sql.Types.FLOAT);
            }
            
            stmt.setString(2, submission.getFeedback());
            stmt.setInt(3, submission.getId());
            
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}