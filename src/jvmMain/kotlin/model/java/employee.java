package model.java;

public class employee {


	@Override
	public String toString() {
		return "employee [id=" + id + ", FirstName=" + FirstName + ", total=" + total + ", ag=" + ag + ", salary="
				+ salary + ", department=" + department + ", vacation=" + vacation + ", vbalance=" + vbalance + "]";
	}

	private int id;
    private String FirstName;
    private int total;
    private int ag;
    private float salary;
    private String department;
    private int vacation;
    private int vbalance;
    
    public employee()
    {
    	//this.id = 0;
	FirstName = "";
	this.department = "";
	this.total = 21;
	this.vbalance = total;
	/*
	
	this.ag = ag;
	this.salary = 0;
	
	this.vacation = 0;
	
    	*/
	
    }

	public employee(int id, String firstName, int total, int ag, float salary, String department,
                    int vacation, int vbalance) {
		super();
		this.id = id;
		FirstName = firstName;
		this.total = total;
		this.ag = ag;
		this.salary = salary;
		this.department = department;
		this.vacation = vacation;
		this.vbalance = vbalance ;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstName() {
		return FirstName;
	}

	public void setFirstName(String firstName) {
		FirstName = firstName;
	}

	public int getTotalbalance() {
		return total;
	}

	public void settotal(int totalbalance) {
		this.total = totalbalance;
	}

	public int getAg() {
		return ag;
	}

	public void setAg(int ag) {
		this.ag = ag;
	}

	public float getSalary() {
		return salary;
	}

	public void setSalary(float salary) {
		this.salary = salary;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public int getVacation() {
		return vacation;
	}

	public void setVacation(int vacation) {
		this.vacation = vacation;
	}

	public int getVbalance() {
		return vbalance;
	}

	public void setVbalance(int vbalance) {
		this.vbalance = vbalance;
	}
	    
	
}
