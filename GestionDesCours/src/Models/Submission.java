package Models;

import java.time.LocalDateTime;

public class Submission 
 {
    private int id;
    private int courseId;
    private int studentId;
    private String studentName;
    private String submissionFilePath;
    private LocalDateTime submittedAt;
    private Float grade;
    private String feedback;
    private boolean isLate;
    
	public int getId() 
    {
		return id;
	}
	public void setId(int id) 
    {
		this.id = id;
	}
	public int getCourseId() 
    {
		return courseId;
	}
	public void setCourseId(int courseId) 
    {
		this.courseId = courseId;
	}
	public int getStudentId() 
    {
		return studentId;
	}
	public void setStudentId(int studentId) 
    {
		this.studentId = studentId;
	}
	public String getStudentName() 
    {
		return studentName;
	}
	public void setStudentName(String studentName) 
    {
		this.studentName = studentName;
	}
	public String getSubmissionFilePath() 
    {
		return submissionFilePath;
	}
	public void setSubmissionFilePath(String submissionFilePath) 
    {
		this.submissionFilePath = submissionFilePath;
	}
	public LocalDateTime getSubmittedAt() 
    {
		return submittedAt;
	}
	public void setSubmittedAt(LocalDateTime submittedAt) 
    {
		this.submittedAt = submittedAt;
	}
	public Float getGrade() 
    {
		return grade;
	}
	public void setGrade(Float grade) 
    {
		this.grade = grade;
	}
	public String getFeedback() 
    {
		return feedback;
	}
	public void setFeedback(String feedback) 
    {
		this.feedback = feedback;
	}
	public boolean isLate() 
    {
		return isLate;
	}
	public void setLate(boolean isLate) 
    {
		this.isLate = isLate;
	}

    
}
