package logic.car;

public enum CarPartType {
	Body("Body"),
	Colour("Colour"),
	Engine("Engine"),
	Gearbox("Gearbox"),
	Seats("Seats"),
	Airco("Airco"),
	Wheels("Wheels");
	
	public String name;
	CarPartType(String name){
		this.name = name;
	}
	
	@Override
	public String toString(){
		return this.name;
	}
}

