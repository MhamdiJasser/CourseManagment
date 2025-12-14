package Models;

public class CourseType 
{
    private int id;
    private String name; // Ex: CM, TD, TP, Projet
    private boolean requiresSubmission; 

    public CourseType(String name, boolean requiresSubmission) 
    {
        this.name = name;
        this.requiresSubmission = requiresSubmission;
    }

    public CourseType(int id, String name, boolean requiresSubmission) 
    {
        this.id = id;
        this.name = name;
        this.requiresSubmission = requiresSubmission;
    }


    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public boolean isRequiresSubmission() { return requiresSubmission; }
    public void setRequiresSubmission(boolean requiresSubmission) { this.requiresSubmission = requiresSubmission; }

    @Override
    public String toString() 
    {
        return name + (requiresSubmission ? " (Remise)" : " (Info)");
    }
}