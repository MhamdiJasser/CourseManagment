package Models;

public class User 
{
	private int NCIN;
	private String full_name;
	private String birthday;
	private String email;
	private String password;
	private String role;
	
	public User(int nCIN, String full_name, String birthday, String email, String password, String role) 
	{
		NCIN = nCIN;
		this.full_name = full_name;
		this.birthday = birthday;
		this.email = email;
		this.password = password;
		this.role = role;
	}

	public int getNCIN() 
	{
		return NCIN;
	}
	
	public void setNCIN(int nCIN) 
	{
		NCIN = nCIN;
	}
	
	public String getFull_name() 
	{
		return full_name;
	}
	
	public void setFull_name(String full_name) 
	{
		this.full_name = full_name;
	}
	
	public String getBirthday()
	{
		return birthday;
	}
	
	public void setBirthday(String birthday) 
	{
		this.birthday = birthday;
	}
	
	public String getEmail() 
	{
		return email;
	}
	
	public void setEmail(String email) 
	{
		this.email = email;
	}
	
	public String getPassword()
	{
		return password;
	}
	
	public void setPassword(String password) 
	{
		this.password = password;
	}
	
	public String getRole() 
	{
		return role;
	}
	
	public void setRole(String role) 
	{
		this.role = role;
	}
}
