package logic.car;


public enum CarPart {
	BODY_SEDAN 			(CarPartType.Body, 		"Sedan"),
	BODY_BREAK			(CarPartType.Body, 		"Break"),
	COLOUR_RED			(CarPartType.Colour, 	"Red"),
	COLOUR_BLUE			(CarPartType.Colour, 	"Blue"),
	COLOUR_BLACK		(CarPartType.Colour, 	"Black"),
	COLOUR_WHITE		(CarPartType.Colour, 	"White"),
	ENGINE_4			(CarPartType.Engine, 	"Standard 2l 4 cilinders"),
	ENGINE_6			(CarPartType.Engine, 	"performance 2.5l 6 cilinders"),
	GEARBOX_6MANUAL		(CarPartType.Gearbox, 	"6 speed manual"),
	GEARBOX_5AUTO		(CarPartType.Gearbox, 	"5 speed automatic"),
	SEATS_LEATHER_BLACK	(CarPartType.Seats, 	"leather black"),
	SEATS_LEATHER_WHITE	(CarPartType.Seats,		"leather white"),
	SEATS_VINYL_GRAY	(CarPartType.Seats, 	"vinyl grey"),
	AIRCO_MANUAL		(CarPartType.Airco,		"manual"),	
	AIRCO_AUTO			(CarPartType.Airco,		"automatic climate control"),
	WHEELS_COMFORT		(CarPartType.Wheels,	"comfort"), 
	WHEELS_SPORTS		(CarPartType.Wheels, 	"sports (low profile)");
	
	
	public final CarPartType type;
	public final String fullName;
	
	
	CarPart(CarPartType type, String fullName){
		this.type = type;
		this.fullName = fullName;
	}
	
	@Override
	public String toString(){
		return this.fullName;
	}

}
