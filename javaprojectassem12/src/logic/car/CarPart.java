package logic.car;


public enum CarPart {
	BODY_SEDAN 			(CarPartType.Body, 		"Sedan",						1),
	BODY_BREAK			(CarPartType.Body, 		"Break", 						2),
	COLOUR_RED			(CarPartType.Colour, 	"Red",							1),							
	COLOUR_BLUE			(CarPartType.Colour, 	"Blue",							2),
	COLOUR_BLACK		(CarPartType.Colour, 	"Black",						3),
	COLOUR_WHITE		(CarPartType.Colour, 	"White",						4),
	ENGINE_4			(CarPartType.Engine, 	"Standard 2l 4 cilinders",		1),
	ENGINE_6			(CarPartType.Engine, 	"performance 2.5l 6 cilinders",	2),
	GEARBOX_6MANUAL		(CarPartType.Gearbox, 	"6 speed manual",				1),
	GEARBOX_5AUTO		(CarPartType.Gearbox, 	"5 speed automatic",			2),
	SEATS_LEATHER_BLACK	(CarPartType.Seats, 	"leather black",				1),
	SEATS_LEATHER_WHITE	(CarPartType.Seats,		"leather white",				2),
	SEATS_VINYL_GRAY	(CarPartType.Seats, 	"vinyl grey",					3),
	AIRCO_MANUAL		(CarPartType.Airco,		"manual",						1),	
	AIRCO_AUTO			(CarPartType.Airco,		"automatic climate control",	2),
	WHEELS_COMFORT		(CarPartType.Wheels,	"comfort",						1), 
	WHEELS_SPORTS		(CarPartType.Wheels, 	"sports (low profile)",			2);
	
	
	public final CarPartType type;
	public final String fullName;
	public final int choiceNo;
	
	CarPart(CarPartType type, String fullName, int choiceNo){
		this.type = type;
		this.fullName = fullName;
		this.choiceNo = choiceNo;
	}
	
	@Override
	public String toString(){
		return this.fullName;
	}

}
