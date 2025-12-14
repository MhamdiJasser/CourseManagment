package Models;

import java.time.LocalDateTime;

public class Course 
{
    private int id;
    private String title;
    
    private CourseType type; 
    private int teacherNcin;
    
    private Matiere matiere;
    
    private String courseFilePath; // Chemin d'accès au fichier du cours (PDF, DOC)
    private LocalDateTime deadline;
    
    private LocalDateTime createdAt;

    public Course(int id, String title, Matiere selectedMatiere, CourseType type, int teacherNcin, String courseFilePath, LocalDateTime deadline, LocalDateTime createdAt) 
    {
        this.id = id;
        this.title = title;
        this.matiere = selectedMatiere; 
        this.type = type;
        this.teacherNcin = teacherNcin;
        this.courseFilePath = courseFilePath;
        this.deadline = deadline;
        this.createdAt = createdAt;
    }

    public Course(String title, Matiere selectedMatiere, CourseType selectedType, int currentTeacherNcin,String selectedFilePath, LocalDateTime deadline) 
    {
    	 this.title = title;
         this.matiere = selectedMatiere;
         this.type = selectedType;
         this.teacherNcin = currentTeacherNcin;
         this.courseFilePath = selectedFilePath;
         this.deadline = deadline;
	}

	public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Matiere getMatiere() { return matiere; }
    public void setMatiere(Matiere matiere) { this.matiere = matiere; }

    public CourseType getType() { return type; }
    public void setType(CourseType type) { this.type = type; }

    public int getTeacherNcin() { return teacherNcin; }
    public void setTeacherNcin(int teacherNcin) { this.teacherNcin = teacherNcin; }

    public String getCourseFilePath() { return courseFilePath; }
    public void setCourseFilePath(String courseFilePath) { this.courseFilePath = courseFilePath; }

    public LocalDateTime getDeadline() { return deadline; }
    public void setDeadline(LocalDateTime deadline) { this.deadline = deadline; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public boolean requiresSubmission() 
    {
        return this.type != null && this.type.isRequiresSubmission();
    }
}