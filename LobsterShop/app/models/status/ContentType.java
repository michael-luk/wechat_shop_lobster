package models.status;


/**
 * The status of account.
 * 
 * @author yanxin
 * @since 1.0.0
 * 
 */
public enum ContentType {

    PRODUCT(0, "PRODUCT"),
    HOTEL(1, "HOTEL"),
    COMPANYINFO(2, "COMPANYINFO");

	private ContentType(int value, String displayName) {
        this.value = value;
        this.displayName = displayName;
    }

    private int value = 0;
    private String displayName;

    public String getDisplayName() {
        return this.displayName;
    }

    public int value() {
        return this.value;
    }
}
