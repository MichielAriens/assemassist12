package logic.users;

public class Manager extends User{
	private CarManufacturingCompany company;
	public Manager(CarManufacturingCompany comp, String UserName){
		super(UserName);
		this.company = comp;
	}
}
