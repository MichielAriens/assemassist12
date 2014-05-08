package logic.car;

/**
 * An enum used to represent the possible car models.
 */
public enum VehicleModel {
		CARMODELA ("Car Model A", 50, new VehiclePart[]{	VehiclePart.BODY_SEDAN,
												VehiclePart.BODY_BREAK,
											  	VehiclePart.COLOUR_RED,
											  	VehiclePart.COLOUR_BLUE,
											  	VehiclePart.COLOUR_BLACK,
											  	VehiclePart.COLOUR_WHITE,
											  	VehiclePart.ENGINE_4,
											  	VehiclePart.ENGINE_6,
											  	VehiclePart.GEARBOX_6MANUAL,
											  	VehiclePart.GEARBOX_5MANUAL,
											  	VehiclePart.GEARBOX_5AUTO,
											  	VehiclePart.SEATS_LEATHER_WHITE,
											  	VehiclePart.SEATS_LEATHER_BLACK,
											  	VehiclePart.SEATS_VINYL_GRAY,
											  	VehiclePart.AIRCO_MANUAL,
											  	VehiclePart.AIRCO_AUTO,
											  	VehiclePart.AIRCO_NONE,
											  	VehiclePart.WHEELS_WINTER,
											  	VehiclePart.WHEELS_COMFORT,
											  	VehiclePart.WHEELS_SPORTS,
											  	VehiclePart.SPOILER_NONE}),
											  	
		CARMODELB ("Car Model B", 70, new VehiclePart[]{	VehiclePart.BODY_SEDAN,
												VehiclePart.BODY_BREAK,
												VehiclePart.BODY_SPORT,
											  	VehiclePart.COLOUR_RED,
											  	VehiclePart.COLOUR_BLUE,
											  	VehiclePart.COLOUR_GREEN,
											  	VehiclePart.COLOUR_YELLOW,
											  	VehiclePart.ENGINE_4,
											  	VehiclePart.ENGINE_6,
											  	VehiclePart.ENGINE_8,
											  	VehiclePart.GEARBOX_6MANUAL,
											  	VehiclePart.GEARBOX_5AUTO,
											  	VehiclePart.SEATS_LEATHER_WHITE,
											  	VehiclePart.SEATS_LEATHER_BLACK,
											  	VehiclePart.SEATS_VINYL_GRAY,
											  	VehiclePart.AIRCO_MANUAL,
											  	VehiclePart.AIRCO_AUTO,
											  	VehiclePart.AIRCO_NONE,
											  	VehiclePart.WHEELS_WINTER,
											  	VehiclePart.WHEELS_COMFORT,
											  	VehiclePart.WHEELS_SPORTS,
											  	VehiclePart.SPOILER_LOW,
											  	VehiclePart.SPOILER_NONE}),
											  	
	  	CARMODELC ("Car Model C", 60, new VehiclePart[]{	VehiclePart.BODY_SPORT,
											  	VehiclePart.COLOUR_BLACK,
											  	VehiclePart.COLOUR_WHITE,
											  	VehiclePart.ENGINE_6,
											  	VehiclePart.ENGINE_8,
											  	VehiclePart.GEARBOX_6MANUAL,
											  	VehiclePart.SEATS_LEATHER_WHITE,
											  	VehiclePart.SEATS_LEATHER_BLACK,
											  	VehiclePart.AIRCO_MANUAL,
											  	VehiclePart.AIRCO_AUTO,
											  	VehiclePart.AIRCO_NONE,
											  	VehiclePart.WHEELS_WINTER,
											  	VehiclePart.WHEELS_SPORTS,
											  	VehiclePart.SPOILER_HIGH,
											  	VehiclePart.SPOILER_LOW});
											  	
		//TRUCKMODELX("Truck Model X", )
											  										  	
											  	
										
		
		/**
		 * The name of this car model.
		 */
		public final String name;
		
		/**
		 * The valid car parts that can be installed in this car model.
		 */
		public VehiclePart[] possibleParts;
		
		/**
		 * The default time it takes to install parts on this model.
		 */
		public final int phaseDuration;
		
		/**
		 * Make a car model with given name and list of valid car parts.
		 * @param name	The name of this car model.
		 * @param phaseDuration  The default time it takes to install parts on this model.
		 * @param parts	 The list of valid car parts of this car model.
		 */
		private VehicleModel(String name, int phaseDuration, VehiclePart[] parts){
			this.name = name;
			this.phaseDuration = phaseDuration;
			this.possibleParts = parts;
		}
		
		/**
		 * A method that checks if the given car part is a valid car part for this car model.
		 * @param part	The car part that has to be checked.
		 * @return	Returns true if the given car part is a valid car part for this car model.
		 * 			Returns false otherwise.
		 */
		public boolean validPart(VehiclePart part){
			for(int i = 0; i < possibleParts.length; i++){
				if(possibleParts[i] == part)
					return true;
			}
			return false;
		}
		
		/**
		 * Returns a string representation of this car model.
		 */
		@Override
		public String toString(){
			return this.name;
		}
		
		/**
		 * Returns the CarModel that corresponds with a given name.
		 * @param name	The name of the car model that has to be searched.
		 * @return	Returns null if the given name does not correspond with the name of any car model.
		 * 			Returns the car model whose name corresponds with the given name otherwise.
		 */
		public static VehicleModel getModelFromString(String name){
			VehicleModel model = null;
			for(VehicleModel m : VehicleModel.values()){
				if(m.toString().equals(name)){
					model = m;
					break;
				}
			}
			return model;
		}
}
