package interfaces;

public interface Printable<S>{
	
	public String getStringRepresentation();
	
	public String getExtraInformation();
	
	public String getStatus();
	
	public boolean equals(Object obj);
}
