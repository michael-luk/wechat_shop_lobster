package models.status;


/**
 * The status of account.
 * 
 * @author chenyu
 * @since 1.0.0
 * 
 */
public enum UserStatus {

	ACTIVE(0, "ACTIVE"),
	HOLD(1, "HOLD"),
    INACTIVE(2, "DISABLE");

    private int value = 0;

	private UserStatus(int value, String displayName) {
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
