package Models;

import java.time.LocalDateTime;

public class Submission 
{
    private int id;
    private int courseId;      
    private int studentNcin;   
    private String submissionFilePath; 
    private LocalDateTime submissionDate;
    private Double grade;
    private String teacherFeedback; 
    
    public Submission(int id, int courseId, int studentNcin, String submissionFilePath, LocalDateTime submissionDate, Double grade, String teacherFeedback) 
    {
        this.id = id;
        this.courseId = courseId;
        this.studentNcin = studentNcin;
        this.submissionFilePath = submissionFilePath;
        this.submissionDate = submissionDate;
        this.grade = grade;
        this.teacherFeedback = teacherFeedback;
    }

    public Submission(int courseId, int studentNcin, String submissionFilePath) 
    {
        this.courseId = courseId;
        this.studentNcin = studentNcin;
        this.submissionFilePath = submissionFilePath;
        this.grade = null;
        this.teacherFeedback = null;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getCourseId() { return courseId; }
    public void setCourseId(int courseId) { this.courseId = courseId; }

    public int getStudentNcin() { return studentNcin; }
    public void setStudentNcin(int studentNcin) { this.studentNcin = studentNcin; }

    public String getSubmissionFilePath() { return submissionFilePath; }
    public void setSubmissionFilePath(String submissionFilePath) { this.submissionFilePath = submissionFilePath; }

    public LocalDateTime getSubmissionDate() { return submissionDate; }
    public void setSubmissionDate(LocalDateTime submissionDate) { this.submissionDate = submissionDate; }

    public Double getGrade() { return grade; }
    public void setGrade(Double grade) { this.grade = grade; }

    public String getTeacherFeedback() { return teacherFeedback; }
    public void setTeacherFeedback(String teacherFeedback) { this.teacherFeedback = teacherFeedback; }
    
    public boolean isGraded() 
    {
        return grade != null;
    }
}