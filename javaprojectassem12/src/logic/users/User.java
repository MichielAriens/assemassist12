package logic.users;

/**
 * Class used to describe a user for a system of a car manufacturing company.
 */
public abstract class User {
	
	/**
	 * The user name for this user.
	 */
	private String userName;
	
	/**
	 * Creates a new user initializing the user name to the given user name.
	 * @param userName	The user name for the new user.
	 */
	public User(String userName){
		this.userName = userName;
	}
	
	/**
	 * Returns the user name.
	 * @return	The user name.
	 */
	public String getUserName(){
		return this.userName;
	}

}
