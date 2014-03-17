package logic.car;

/**
 * An enum to represent the possible car part types.
 */
public enum CarPartType {
	Body("Body"),
	Colour("Colour"),
	Engine("Engine"),
	Gearbox("Gearbox"),
	Seats("Seats"),
	Airco("Airco"),
	Wheels("Wheels");
	
	/**
	 * The name of this car part type.
	 */
	public String name;
	
	/**
	 * Make a car part type with a given name.
	 * @param name
	 */
	CarPartType(String name){
		this.name = name;
	}
	
	/**
	 * Returns a string representation of this car part type.
	 */
	@Override
	public String toString(){
		return this.name;
	}
}

