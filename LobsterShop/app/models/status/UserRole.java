/**
 * 
 */
package models.status;

/**
 * Stores a list of well-known roles in the system.
 * 
 */
public enum UserRole {

	/**
	 * Super administrator.
	 */
	SUPERADMIN(0, "SUPERADMIN"),

	/**
	 * group administrator.
	 */
	ADMIN(1, "ADMIN"),

	/**
	 * Ordinary member.
	 */
	MEMBER(2, "MEMBER");

    private int value = 0;

    private UserRole(int value, String displayName) {
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
