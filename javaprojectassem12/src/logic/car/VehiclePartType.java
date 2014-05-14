package logic.car;

/**
 * An enum to represent the possible car part types.
 */
public enum VehiclePartType {
	Body("Body"),
	Colour("Colour"),
	Engine("Engine"),
	Gearbox("Gearbox"),
	Seats("Seats"),
	Airco("Airco"),
	Wheels("Wheels"),
	Spoiler("Spoiler"),
	ToolStorage("Tool Storage"),
	CargoProtection("Cargo Protection"),
	Certification("Maximum cargo load certification");
	
	/**
	 * The name of this car part type.
	 */
	public String name;
	
	/**
	 * Make a car part type with a given name.
	 * @param name
	 */
	VehiclePartType(String name){
		this.name = name;
	}
	
	/**
	 * Returns a string representation of this car part type.
	 */
	@Override
	public String toString(){
		return this.name;
	}
	
	/**
	 * Returns the CarPartType that corresponds with a given name.
	 * @param name	The name of the car part type that has to be searched.
	 * @return	Null if the given name does not correspond with the full name of any car part type.
	 * 			The car part type whose full name corresponds with the given name otherwise.
	 */
	public static VehiclePartType getTypefromString(String name){
		VehiclePartType type = null;
		for(VehiclePartType t : VehiclePartType.values()){
			if(t.toString().equals(name)){
				type = t;
				break;
			}
		}
		return type;
	}
}

