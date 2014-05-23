package interfaces;

public interface Printable<S>{
	
	/**
	 * Returns a string representation of the encapsuated object.
	 * @return		string representation
	 */
	public String getStringRepresentation();
	
	/**
	 * Returns any extra information for the encapsulated object. (can be null).
	 * @return		string with extra info
	 */
	public String getExtraInformation();
	
	/**
	 * Returns the status of the encapsulated object. (can be null).
	 * @return
	 */
	public String getStatus();
	
	/**
	 * Defines an equals relationship. Two printable objects which encapsulate the same object equal eachother.
	 * Further implementation constraints from Object.equals(Object):boolean.
	 * @param 	obj		The other object to test against
	 * @return			True if the param and "this" encapsulate the same object.
	 */
	public boolean equals(Object obj);
}
