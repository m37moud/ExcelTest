package model.java;

public class Depart {
	private int Id;
	private String Department;
	
	public Depart()
	{
		
	}
	public Depart(String Department)
	{
		
		this.Department = Department;
	}
	
	public Depart(int id , String Department)
	{
		this.Id = id;
		this.Department = Department;
	}
	
	public int getId() {
		return Id;
	}
	public void setId(int id) {
		Id = id;
	}
	
	public String getDepartment() {
		return Department;
	}
	public void setDepartment(String department) {
		Department = department;
	}
	
	

}
