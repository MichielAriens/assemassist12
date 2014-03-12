package logic.car;

/**
 * An enum used to represent the possible car models.
 */
public enum CarModel {
		MODEL1 ("Model1", new CarPart[]{	CarPart.BODY_SEDAN,
										  	CarPart.AIRCO_AUTO,
										  	CarPart.AIRCO_MANUAL,
										  	CarPart.BODY_BREAK,
										  	CarPart.COLOUR_BLACK,
										  	CarPart.COLOUR_BLUE,
										  	CarPart.COLOUR_RED,
										  	CarPart.COLOUR_WHITE,
										  	CarPart.ENGINE_4,
										  	CarPart.ENGINE_6,
										  	CarPart.GEARBOX_5AUTO,
										  	CarPart.GEARBOX_6MANUAL,
										  	CarPart.SEATS_LEATHER_BLACK,
										  	CarPart.SEATS_LEATHER_WHITE,
										  	CarPart.SEATS_VINYL_GRAY,
										  	CarPart.WHEELS_COMFORT,
										  	CarPart.WHEELS_SPORTS});
		
		/**
		 * The name of this car model.
		 */
		public final String name;
		
		/**
		 * The valid car parts that can be installed in this car model.
		 */
		public CarPart[] possibleParts;
		
		/**
		 * Make a car model with given name and list of valid car parts.
		 * @param name	The name of this car model.
		 * @param parts	The list of valid car parts of this car model.
		 */
		private CarModel(String name, CarPart[] parts){
			this.name = name;
			this.possibleParts = parts;
		}
		
		/**
		 * A method that checks if the given car part is a valid car part for this car model.
		 * @param part	The car part that has to be checked.
		 * @return	Returns true if the given car part is a valid car part for this car model.
		 * 			Returns false otherwise.
		 */
		public boolean validPart(CarPart part){
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
		public static CarModel getModelFromString(String name){
			CarModel model = null;
			for(CarModel m : CarModel.values()){
				if(m.toString().equals(name)){
					model = m;
					break;
				}
			}
			return model;
		}
}
