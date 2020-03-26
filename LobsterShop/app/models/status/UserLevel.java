package models.status;


/**
 * The status of account.
 * 
 *
 */
public enum UserLevel {

	UNREGISTERED(0, "UNREGISTERED"),
	REGISTERED(1, "REGISTERED"),
    SILVER(2, "SILVER"),
    GOLDEN(3, "GOLDEN");

    private int value = 0;

	private UserLevel(int value, String displayName) {
        this.value = value;
        this.displayName = displayName;
    }

	String displayName;

	public String getDisplayName() {
		return this.displayName;
	}

    public int value() {
        return this.value;
    }
}
