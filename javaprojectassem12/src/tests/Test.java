package tests;

public class Test {

	public static void main(String[] args) {
		//"HH:mm dd/MM/yyyy"
		String test = "10:39 03/03/2014";
		String simple = "09:5";
		String pattern = "([01][0-9]|2[0-3]):([0-5][0-9]) (";
		System.out.println(simple.matches(pattern));
	}
}
