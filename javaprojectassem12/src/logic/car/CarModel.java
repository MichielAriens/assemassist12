package logic.car;



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
		
		public final String name;
		public CarPart[] possibleParts;
		private CarModel(String name, CarPart[] parts){
			this.name = name;
			this.possibleParts = parts;
		}
		
		public boolean validPart(CarPart part){
			for(int i = 0; i < possibleParts.length; i++){
				if(possibleParts[i] == part)
					return true;
			}
			return false;
		}
		
		@Override
		public String toString(){
			return this.name;
		}
}
